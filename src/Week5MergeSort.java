import java.util.ArrayList;
import java.util.Arrays;

public class Week5MergeSort {
    public static void main(String[] args) {
        var original = new ArrayList<Integer>(Arrays.asList(9,2,3,1,10,5));
        var test = MergeSort.sort(original);
        System.out.println(original);
        System.out.println(test);
    }
}

class MergeSort {

    public static ArrayList<Integer> sort(ArrayList<Integer> m) {
        int size = m.size();

        if (size <= 1) {
            return m;
        }

        // Split into 2 arrays (Divide)
        var mid = size / 2;
        var left = new ArrayList<Integer>();
        var right = new ArrayList<Integer>();
        for(int i = 0; i < size; i++) {
            if (i < mid) {
                left.add(m.get(i));
            } else {
                right.add(m.get(i));
            }
        }

        // Recursively sort (Conquer)
        left = sort(left);
        right = sort(right);

        return merge(left, right);
    }

    // Assumes both arrays are sorted ascending
    public static ArrayList<Integer> merge(ArrayList<Integer> a, ArrayList<Integer> b) {
        var merged = new ArrayList<Integer>();

        while(!a.isEmpty() && !b.isEmpty()) {
            if (a.getFirst() > b.getFirst()) {
                merged.add(b.removeFirst());
            } else {
                merged.add(a.removeFirst());
            }
        }

        while (!a.isEmpty()) {
            merged.add(a.removeFirst());
        }

        while (!b.isEmpty()) {
            merged.add(b.removeFirst());
        }

        return merged;
    }
}