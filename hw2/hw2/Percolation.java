package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int size;
    private final boolean[][] grid;
    private int numberOfOpenSites;
    private boolean percolates;
    private final  WeightedQuickUnionUF disjointSet;
    private final WeightedQuickUnionUF blockBackWash;

    public Percolation (int N) {
        size = N;
        grid = new boolean[N][N]; //Default value of bool is FALSE
        numberOfOpenSites = 0;
        percolates = false;
        //Let the N^2 + 1 element be the source and N^2 + 1 element be the sink
        disjointSet = new WeightedQuickUnionUF(N * N + 2);
        //duplicate disjoint set without sink
        blockBackWash = new WeightedQuickUnionUF(N * N + 1);
    }

    private int xyTo1D (int row, int col) {
        return row * size + col;
    }

    public void open (int row, int col) {
        if (!isOpen(row, col)){
            grid[row][col] = true;
            numberOfOpenSites += 1;
            if (row == 0) {
                disjointSet.union(xyTo1D(row, col), size * size); //connect to source if we open element in first row
                blockBackWash.union(xyTo1D(row, col), size * size);
            }
            if (row == size - 1) {
                disjointSet.union(xyTo1D(row, col), size * size + 1); //connect to sink if open element in last row
            }
            if (row - 1 != -1 && isOpen(row - 1, col)) {
                disjointSet.union(xyTo1D(row, col), xyTo1D(row - 1, col));
                blockBackWash.union(xyTo1D(row, col), xyTo1D(row - 1, col));
            }
            if (row + 1 != size && isOpen(row + 1, col)) {
                disjointSet.union(xyTo1D(row, col), xyTo1D(row + 1, col));
                blockBackWash.union(xyTo1D(row, col), xyTo1D(row + 1, col));
            }
            if (col - 1 != -1 && isOpen(row, col - 1)) {
                disjointSet.union(xyTo1D(row, col), xyTo1D(row, col - 1));
                blockBackWash.union(xyTo1D(row, col), xyTo1D(row, col - 1));
            }
            if (col + 1 != size && isOpen(row, col + 1)) {
                disjointSet.union(xyTo1D(row, col), xyTo1D(row, col + 1));
                blockBackWash.union(xyTo1D(row, col), xyTo1D(row, col + 1));
            }
            if (disjointSet.connected(size * size, size * size + 1)) {
                percolates = true;
            }
        }
    }

    public boolean isOpen (int row, int col) {
        return grid[row][col];
    }

    public boolean isFull(int row, int col) {
        if (!isOpen(row, col)) return false;
        boolean noBackWash = blockBackWash.connected(xyTo1D(row, col), size * size);
        return disjointSet.connected(xyTo1D(row, col), size * size) && noBackWash;
    }

    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    public boolean percolates() {
        return percolates;
    }
}
