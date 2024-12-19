import java.util.LinkedList;
import java.util.Objects;
import java.util.Scanner;

public class Week3AddDeleteAlgos {
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);

        var list = new UniqueNumbersLinkedList();

        while (true) {
            System.out.print("Enter a command(quit, add, remove): ");
            var input = scanner.nextLine();

            if (input.equals("quit")) {
                list.printList();
                return;
            }
            else if (input.equals("add")) {
                System.out.print("Enter a number: ");
                var num = scanner.nextInt();
                var result = list.add(num);

                if (result) {
                    System.out.println("Added the number to the list");
                }
                else {
                    System.out.println("Number in the list");
                }
            }
            else if (input.equals("remove")) {
                System.out.print("Enter a number: ");
                var num = scanner.nextInt();
                list.remove(num);
            }
        }
    }
}

class UniqueNumbersLinkedList {
    private LinkedList<Integer> list = new LinkedList<>();

    public boolean add(int num) {
        if (!list.contains(num)) {
            list.add(num);
            return true;
        }

        return false;
    }

    public boolean remove(int num) {
        return list.remove((Object)num);
    }

    public void printList() {
        System.out.println(list);
    }
}
