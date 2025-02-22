package hw3.hash;

import edu.princeton.cs.algs4.StdOut;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /* TODO:
         * Write a utility function that returns true if the given oomages
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFFF) % M.
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */
        int[] buckets = new int[M];
        int len = 0;
        for (Oomage o : oomages) {
            buckets[(o.hashCode() & 0x7FFFFFFF) % M] += 1;
            len ++;
        }
        double upper = ((double) len) / 2.5;
        double lower = ((double) len) / 50.0;
        for (int i = 0 ; i < M ; i ++) {
            if ((double)buckets[i] >= upper || (double)buckets[i] <= lower) return false;
        }
        return true;
    }
}
