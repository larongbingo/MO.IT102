import java.util.LinkedList;

public class Week2LinkedListExercise {
    public static void main(String[] args) {
        var list = new LinkedList<String>();
        list.add("Apple");
        list.add("Banana");
        list.add("Cherry");

        System.out.println(list);

        list.addFirst("Orange");

        list.remove("Banana");

        System.out.println(list);

        String item = list.get(1);

        System.out.println(list.size());
    }
}
