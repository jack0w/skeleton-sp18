/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        // find max length
        int max = Integer.MIN_VALUE;
        for (String s : asciis) {
            max = max > s.length() ? max : s.length();
        }
        String[] sorted = asciis.clone();

        for (int d = max - 1; d >= 0; d--) {
            sortHelperLSD(sorted, d);
        }
        return sorted;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        // find max
        int max = Integer.MIN_VALUE;
        for (String s : asciis) {
            int key = sortKey(s, index);
            max = max > key? max : key;
        }

        // gather all the counts for each value
        int[] counts = new int[max + 1];
        for (String s : asciis) {
            counts[sortKey(s, index)]++;
        }

        // however, below is a more proper, generalized implementation of
        // counting sort that uses start position calculation
        int[] starts = new int[max + 1];
        int pos = 0;
        for (int i = 0; i < starts.length; i += 1) {
            starts[i] = pos;
            pos += counts[i];
        }

        String[] sorted2 = new String[asciis.length];
        for (int i = 0; i < asciis.length; i += 1) {
            String item = asciis[i];
            int place = starts[sortKey(item, index)];
            sorted2[place] = item;
            starts[sortKey(item, index)] += 1;
        }


        for (int i = 0; i < asciis.length; i++) {
            asciis[i] = sorted2[i];
        }
    }

    private static int sortKey(String s, int index) {
        if (s.length()-1 < index) {
            return 0;
        }
        else {
            return s.charAt(index);
        }
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

    public static void main(String[] args) {
        String[] asciis = new String[3];
        asciis[0] = "z";
        asciis[1] = "ya";
        asciis[2] = "xbb";
        for (String s : asciis) {
            System.out.println(s);
        }

        String[] sorted = sort(asciis);
        for (String s : sorted) {
            System.out.println(s);
        }

    }
}
