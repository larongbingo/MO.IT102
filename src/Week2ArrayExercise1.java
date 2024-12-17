public class Week2ArrayExercise1 {
    public static void main(String[] args) {
        int[] arr = {25, 4, 16, 9, 10};
        int sum = 0;

        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }

        System.out.println("The sum of the array's values is: " + sum);

    }
}