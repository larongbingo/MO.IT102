import java.util.Stack;

public class Week2StackExercise1 {
    public static void main(String[] args) {
        String[] inputs = { "((()))", "(()())", "(()", "())" };
        for (int i = 0; i < inputs.length; i++) {
            var result = isParenthesesBalanced(inputs[i]);
            System.out.println(inputs[i] + ": " + result);
        }
    }

    public static boolean isParenthesesBalanced(String input) {
        var stack = new Stack<Character>();
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '(') {
                stack.push(input.charAt(i));
            }
            else if (input.charAt(i) == ')') {
                if (stack.isEmpty()) {
                    return false;
                }

                stack.pop();
            }
        }

        return stack.isEmpty();
    }
}
