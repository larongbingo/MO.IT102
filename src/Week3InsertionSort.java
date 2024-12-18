import java.util.LinkedList;
import java.util.Random;

public class Week3InsertionSort {
    public static void main(String[] args) {
        var random = new Random();

        for (int a = 0; a < 10; a++) {
            var arr = new int[10];
            for (int i = 0; i < 10; i++) {
                arr[i] = random.nextInt(100);
            }
            Week3BubbleSort.printArray(arr);

            var linkedList = arrToLinkedList(arr.clone());

            arr = insertionSort(arr);

            linkedList = insertionSort(linkedList);

            System.out.print(" -> ");
            Week3BubbleSort.printArray(arr);
            System.out.print(" & " + linkedList);
            System.out.println();
        }
    }

    /**
     * Input - An array of integers
     * <p>
     * Output - Modifies the same integer; sorts from smallest to largest
     * <p>
     * Control Structures - Requires 1 if statement to check if the 2 numbers in the array aren't sorted and 2 while
     * loops; 1 loop to iterate for each number and 1 loop to "insert" the number into its position
     * <p>
     * Data Structures - Preferably an array
     * <p>
     * Variables - Uses 1 variable to hold a number while swapping the position
     * <p>
     * Comments - An array of integers
     * <p>
     * <code>
     * func insertionSort(A)
     *      i = 1
     *      while i < A.length
     *          j = i
     *          while j > 0 and A[j - 1] > A[j]
     *              Swap A[j] and A[j+1]
     *              j--
     *          i = i + 1
     *      return A
     * </code>
     */
    public static int[] insertionSort(int[] arr) {
        var i = 1;
        while (i < arr.length) {
            var j = i;

            while (j > 0 && arr[j-1] > arr[j]) {
                var temp = arr[j-1];
                arr[j-1] = arr[j];
                arr[j] = temp;

                j--;
            }

            i++;
        }

        return arr;
    }

    public static LinkedList<Integer> insertionSort(LinkedList<Integer> list) {
        var i = 1;
        while (i < list.size()) {
            var j = i;

            while(j > 0 && list.get(j-1) > list.get(j)) {
                var temp = list.get(j-1);
                list.set(j-1, list.get(j));
                list.set(j, temp);

                j--;
            }
            i++;
        }

        return list;
    }

    public static LinkedList<Integer> arrToLinkedList(int[] arr) {
        var list = new LinkedList<Integer>();

        for (int j : arr) {
            list.add(j);
        }

        return list;
    }
}
