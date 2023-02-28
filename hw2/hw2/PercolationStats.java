package hw2;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.introcs.StdRandom;
public class PercolationStats {

    private final double[] thresholds;
    private final int T;

    public PercolationStats(int N, int T, PercolationFactory pf){
        if (N <=0 || T <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        this.T = T;
        thresholds = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation percolation = pf.make(N);
            while (!percolation.percolates()) {
                percolation.open(StdRandom.uniform(N), StdRandom.uniform(N));
            }
            thresholds[i] = (double) percolation.numberOfOpenSites() / (double)(N * N);
        }
    }

    public double mean() {
        double sum = 0;
        for (double x: thresholds) {
            sum += x;
        }
        return sum / T;
    }

    public double stddev() {
        double mean = mean();
        double sum = 0;
        for (double x: thresholds) {
            sum += Math.pow(x - mean, 2);
        }
        return Math.pow(sum / (T - 1), 0.5);
    }

    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.pow(T, 0.5);
    }
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.pow(T, 0.5);
    }

    public static void main(String[] args) {
        PercolationFactory PF = new PercolationFactory();
        PercolationStats PS = new PercolationStats(20, 10, PF);
        StdOut.println(PS.mean());
        StdOut.println(PS.stddev());
        StdOut.println(PS.confidenceLow());
        StdOut.println(PS.confidenceHigh());
    }
}
