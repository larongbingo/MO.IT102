import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedList;

public class Milestone1 {
    public static void main(String[] args) {
        var items = new Inventory();
        items.add(new Item(LocalDate.ofYearDay(2021, 31), StockLabel.New, "ABCTesting", "123", Status.Sold));
        items.add(new Item(LocalDate.ofYearDay(2020, 31), StockLabel.Old, "BCDTesting", "123", Status.Sold));
        items.add(new Item(LocalDate.ofYearDay(2019, 31), StockLabel.New, "CDETesting", "123", Status.OnHand));

        var result = items.sortBy(SortBy.DateEntered);
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
            case DateEntered -> comparator = Comparator.comparing((Item o) -> o.dateEntered);
            case Brand -> comparator = Comparator.comparing((Item o) -> o.brand);
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
class Item {
    public Item(LocalDate dateEntered, StockLabel stockLabel, String brand, String engineNumber, Status status) {
        this.dateEntered = dateEntered;
        this.stockLabel = stockLabel;
        this.brand = brand;
        this.engineNumber = engineNumber;
        this.status = status;
    }

    public LocalDate dateEntered;
    public StockLabel stockLabel;
    public String brand;
    public String engineNumber;
    public Status status;

    @Override
    public String toString() {
        return "Item{" +
                "dateEntered=" + dateEntered.format(DateTimeFormatter.ofPattern("MM-dd-yyyy")) +
                ", stockLabel=" + stockLabel +
                ", brand='" + brand + '\'' +
                ", engineNumber='" + engineNumber + '\'' +
                ", isOnHand=" + status +
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

enum Status {
    Sold, OnHand
}