import java.util.LinkedList;
import java.util.Objects;
import java.util.Scanner;

public class Week3AddDeleteAlgos {
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);

        var list = new UniqueNumbersLinkedList();

        while (true) {
            System.out.print("Enter a number or q for quit: ");
            var input = scanner.nextLine();

            if (Objects.equals(input, "q")) {
                return;
            }

            var num = Integer.parseInt(input);

            list.isUnique(num);
        }
    }
}

class UniqueNumbersLinkedList {
    private LinkedList<Integer> list = new LinkedList<>();

    /// Returns true if the number is unique and is added into the list
    /// False otherwise
    public boolean isUnique(int num) {
        var isUniqueInList = list.contains(num);

        if (isUniqueInList) {
            list.remove((Object)num);
        }
        else {
            list.add(num);
        }

        System.out.println(list);

        return isUniqueInList;
    }

}
