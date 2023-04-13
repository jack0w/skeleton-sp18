package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private boolean cycleFound = false;
    private int[] nodeTo;
    public MazeCycles(Maze m) {
        super(m);
        maze = m;
        nodeTo = new int[maze.N() * maze.N()];
    }

    @Override
    public void solve() {
        dfs_cycle(0);
    }

    // Helper methods go here
    private void dfs_cycle(int v) {
        if (cycleFound) {
            return;
        }

        marked[v] = true;
        announce();

        for (int w : maze.adj(v)) {
            if (!marked[w]) {
                nodeTo[w] = v;
                announce();
                dfs_cycle(w);
            }
            else{
                if (nodeTo[v] != w) {
                    cycleFound = true;
                    edgeTo[w] = v;
                    for (int x = v; x != w; x = nodeTo[x]) {
                        edgeTo[x] = nodeTo[x];
                    }
                    announce();
                    return;
                }
            }
        }
    }
}

