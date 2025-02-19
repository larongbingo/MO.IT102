import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class MS1MS2Comparison {
    private static LinkedList<Item> smallLL = new LinkedList<>();
    private static LinkedList<Item> mediumLL = new LinkedList<>();
    private static LinkedList<Item> largeLL = new LinkedList<>();
    private static ArrayList<Item> smallAL = new ArrayList<>();
    private static ArrayList<Item> mediumAL = new ArrayList<>();
    private static ArrayList<Item> largeAL = new ArrayList<>();

    private static final int RUN = 100;

    private static LinkedList<Long> smallBSRuntimes = new LinkedList<>();
    private static LinkedList<Long> mediumBSRuntimes = new LinkedList<>();
    private static LinkedList<Long> largeBSRuntimes = new LinkedList<>();
    private static LinkedList<Long> smallMSRuntimes = new LinkedList<>();
    private static LinkedList<Long> mediumMSRuntimes = new LinkedList<>();
    private static LinkedList<Long> largeMSRuntimes = new LinkedList<>();

    private static final int SMALL = 100;
    private static final int MEDIUM = 500;
    private static final int LARGE = 1000;

    public static void main(String[] args) throws IOException {
        System.out.println("Press enter to start");
        System.in.read();

        for (int i = 0; i < 100; i++) {
            var item = generateTestItem(i);
            smallLL.add(item);
            smallAL.add(item);
        }

        for (int i = 0; i < 500; i++) {
            var item = generateTestItem(i);
            mediumLL.add(item);
            mediumAL.add(item);
        }

        for (int i = 0; i < 1000; i++) {
            var item = generateTestItem(i);
            largeLL.add(item);
            largeAL.add(item);
        }

        BubbleSortTests();
        MergeSortTests();

        System.out.println("Average Runtimes in ns - Same Random Items - Executed "+RUN+" Times - Bubble Sort");
        System.out.println(SMALL + " ITEMS = " + smallBSRuntimes.stream().mapToLong(Long::longValue).sum() / RUN);
        System.out.println(MEDIUM + " ITEMS = " + mediumBSRuntimes.stream().mapToLong(Long::longValue).sum() / RUN);
        System.out.println(LARGE + " ITEMS = " + largeBSRuntimes.stream().mapToLong(Long::longValue).sum() / RUN);

        System.out.println("Average Runtimes in ns - Same Random Items - Executed "+RUN+" Times - Merge Sort");
        System.out.println(SMALL + " ITEMS = " + smallMSRuntimes.stream().mapToLong(Long::longValue).sum() / RUN);
        System.out.println(MEDIUM + " ITEMS = " + mediumMSRuntimes.stream().mapToLong(Long::longValue).sum() / RUN);
        System.out.println(LARGE + " ITEMS = " + largeMSRuntimes.stream().mapToLong(Long::longValue).sum() / RUN);
    }

    private static Item generateTestItem(int i)
    {
        return new Item(
                LocalDate.now().minusDays((int) (i * Math.random())),
                i % 2 == 0 ? StockLabel.New : StockLabel.Old,
                "Brand" + (i * Math.random()),
                "EngineNumber" + (i * Math.random() * Math.random()),
                i % 2 == 0 ? Status.Sold : Status.OnHand);
    }


    private static void BubbleSortTests()
    {
        BubbleSortTest(smallLL, smallBSRuntimes);
        BubbleSortTest(mediumLL, mediumBSRuntimes);
        BubbleSortTest(largeLL, largeBSRuntimes);
    }

    private static void BubbleSortTest(LinkedList<Item> items, LinkedList<Long> runtimes) {
        for (int i = 0; i < RUN; i++) {
            var copy = (LinkedList<Item>) items.clone();
            var start = System.nanoTime();
            BubbleSort.sort(copy, Comparator.comparing((Item o) -> o.dateEntered));
            var elapsed = System.nanoTime() - start;
            runtimes.add(elapsed);
        }
        System.out.println(runtimes);
    }

    private static void MergeSortTests()
    {
        MergeSortTest(smallAL, smallMSRuntimes);
        MergeSortTest(mediumAL, mediumMSRuntimes);
        MergeSortTest(largeAL, largeMSRuntimes);
    }

    private static void MergeSortTest(ArrayList<Item> items, LinkedList<Long> runtimes) {
        for (int i = 0; i < RUN; i++) {
            var copy = (ArrayList<Item>) items.clone();
            var start = System.nanoTime();
            MergeSortMS2.sort(copy, Comparator.comparing((Item o) -> o.dateEntered));
            var elapsed = System.nanoTime() - start;
            runtimes.add(elapsed);
        }
        System.out.println(runtimes);
    }
}



