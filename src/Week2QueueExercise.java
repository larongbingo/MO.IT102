import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Week2QueueExercise {
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);

        Queue<String> queue = new LinkedList<>();

        while (true) {
            System.out.print("Enter an option (exit, pop, push): ");
            String input = scanner.nextLine();
            if (input.equals("exit")) {
                System.out.println("Customer's still in queue: " + queue);
                break;
            }
            else if (input.equals("pop")) {
                if (queue.isEmpty()) {
                    System.out.println("No customer in the queue");
                }
                else {
                    System.out.println("Customer up next: " + queue.poll());
                }
            }
            else if (input.equals("push")) {
                System.out.print("Customer's Name: ");
                String name = scanner.nextLine();
                queue.offer(name);
            }
        }
    }
}
