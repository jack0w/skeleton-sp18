package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {

    private class SearchNode implements Comparable<SearchNode>{
        private WorldState worldState;
        private int numMoves;
        private SearchNode prevNode;
        private int estimatedDistanceToGoal;

        public SearchNode(WorldState worldState, int numMoves, SearchNode prevNode) {
            this.worldState = worldState;
            this.numMoves = numMoves;
            this.prevNode = prevNode;
            this.estimatedDistanceToGoal = this.worldState.estimatedDistanceToGoal();
        }

        public int getNumMoves () {
            return numMoves;
        }

        public WorldState getWorldState () {
            return worldState;
        }

        public SearchNode getPrevNode () {
            return prevNode;
        }

        public int fullEstimatedDistance () {
            return numMoves + estimatedDistanceToGoal;
        }

        @Override
        public int compareTo (SearchNode node) {
            return fullEstimatedDistance() - node.fullEstimatedDistance();
        }
    }

    private int moves;

    private Stack<WorldState> solution = new Stack<>();

    public Solver (WorldState initial) {
        MinPQ<SearchNode> pq = new MinPQ<>();
        pq.insert(new SearchNode(initial, 0, null));
        SearchNode visit = pq.delMin();
        while(!visit.getWorldState().isGoal()) {
            for (WorldState adj : visit.getWorldState().neighbors()) {
                if (visit.getPrevNode() == null || !visit.getPrevNode().getWorldState().equals(adj)) {
                    pq.insert(new SearchNode(adj, visit.getNumMoves() + 1, visit));
                }
            }
            visit = pq.delMin();
        }
        moves = visit.getNumMoves();
        while (visit.getPrevNode() != null) {
            solution.push(visit.getWorldState());
            visit = visit.getPrevNode();
        }
        solution.push(initial);

    }

    public int moves() {
        return moves;
    }

    public Iterable<WorldState> solution() {
        return solution;
    }
}
