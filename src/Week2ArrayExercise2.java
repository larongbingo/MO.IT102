public class Week2ArrayExercise2 {
    public static void main(String[] args) {
        int[] arr = {420, 69, 12, 6, 18};
        int valueToCheck = 6;

        boolean isMatch = false;

        for(int i = 0; i < arr.length; i++) {
            if(arr[i] == valueToCheck) {
                isMatch = true;
                break;
            }
        }

        if (isMatch) {
            System.out.println("The array contains the value " + valueToCheck);
        }
        else {
            System.out.println("The array does not contain the value " + valueToCheck);
        }
    }
}
