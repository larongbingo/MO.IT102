public class Week6Search {
    public static void main(String[] args) {
        System.out.println(maxNumberLinearSearch(new int[] {9,8,7,6,5,4,3,2,1}));
        System.out.println(maxNumberLinearSearch(new int[] {0,0,0,0,0,0,0,0,-1}));
        System.out.println(maxNumberLinearSearch(new int[] {-9,-8,-7,-6,-5,-3,-1}));
    }

    public static int maxNumberLinearSearch(int[] nums) {
        var max = nums[0];

        for(var num : nums) {
            if (num > max) {
                max = num;
            }
        }

        return max;
    }
}
