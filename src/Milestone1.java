import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

public class Milestone1 {
    public static void main(String[] args) {
        var items = new Inventory();
        items.add(new Item(new Date(2021, Calendar.DECEMBER, 1), StockLabel.New, "ABCTesting", "123", true));
        items.add(new Item(new Date(2020, Calendar.DECEMBER, 1), StockLabel.New, "BCDTesting", "123", true));
        items.add(new Item(new Date(2019, Calendar.DECEMBER, 1), StockLabel.New, "CDETesting", "123", true));

        var result = items.sortBy(SortBy.Brand);
        System.out.println(result);
    }
}

class Inventory {
    public LinkedList<Item> items = new LinkedList<>();

    public boolean add(Item item) {
        return items.add(item);
    }

    public boolean remove(Item item) {
        return items.remove(item);
    }

    public LinkedList<Item> sortBy(SortBy sortBy) {
        Comparator<Item> comparator = null;

        switch (sortBy) {
            case DateEntered -> comparator = Comparator.comparing((Item o) -> o.dateEntered());
            case Brand -> comparator = Comparator.comparing((Item o) -> o.brand());
        }

        var result = BubbleSort.sort((LinkedList<Item>) items.clone(), comparator);

        return result;
    }
}

class BubbleSort {
    public static LinkedList<Item> sort(LinkedList<Item> a, Comparator<Item> c) {
        for(int i = 0; i < a.size(); i++) {
            for(int j = 0; j < a.size() - 1; j++) {
                if (c.compare(a.get(j), a.get(j+1)) > 0) {
                    var temp = a.get(j);
                    a.set(j, a.get(j+1));
                    a.set(j+1, temp);
                }
            }
        }

        return a;
    }
}

// https://docs.google.com/spreadsheets/d/1ShE6rwq4VydxXPcIlTVEkQ5k94IW_1RvHBoQVQa-9s8/edit?gid=0#gid=0
record Item(Date dateEntered, StockLabel stockLabel, String brand, String engineNumber, boolean isOnHand) {
    // @isOnHand True - On Hand; False - Sold
    @Override
    public String toString() {
        var dateFormat = new SimpleDateFormat("MM-dd-yyyy");

        return "Item{" +
                "dateEntered=" + dateFormat.format(dateEntered) +
                ", stockLabel=" + stockLabel +
                ", brand='" + brand + '\'' +
                ", engineNumber='" + engineNumber + '\'' +
                ", isOnHand=" + isOnHand +
                '}';
    }
}

enum SortBy {
    DateEntered,
    Brand,
}

enum StockLabel {
    Old, New
}