import java.util.Stack;

public class Week2StackExercise2 {
    public static void main(String[] args) {
        Stack<String> stack = new Stack<>();
        stack.push("Alice");
        stack.push("Bob");
        stack.push("Charlie");

        while(!stack.isEmpty()) {
            System.out.println(stack.pop());
        }

        System.out.println(stack.isEmpty());
    }
}
