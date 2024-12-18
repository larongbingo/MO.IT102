import java.util.Random;

public class Week3BubbleSort {

    public static void main(String[] args) {
        var random = new Random();

        for (int a = 0; a < 10; a++) {
            var arr = new int[10];
            for (int i = 0; i < 10; i++) {
                arr[i] = random.nextInt(100);
            }
            printArray(arr);

            arr = bubbleSort(arr);
            System.out.print(" -> ");
            printArray(arr);
            System.out.println();
        }
    }

    /**
     * Input - An array of integers
     * <p>
     * Output - Modifies the same integer; sorts from smallest to largest
     * <p>
     * Control Structures - Requires 1 if statement to check if the 2 numbers in the array aren't sorted and 2 for
     * loops; 1 loop to iterate each number and 1 loop to move/bubble the biggest number
     * <p>
     * Data Structures - Preferably an array
     * <p>
     * Variables - Uses 1 variable to hold a number while swapping the position
     * <p>
     * Comments - An array of integers
     * <p>
     * <code>
     * func bubbleSort(A)
     *      for i = 0 .. A.length
     *          for j = 0 .. (A.length - 1)
     *              if A[j] > A[j+1]
     *                  Swap A[j] and A[j+1]
     *      return A
     * </code>
     */
    public static int[] bubbleSort(int[] arr) {
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr.length - 1; j++) {
                if (arr[j] > arr[j+1]) {
                    var temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }

        return arr;
    }

    public static void printArray(int[] arr) {
        System.out.print("[ ");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.print("]");
    }
}
