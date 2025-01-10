import java.io.File;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;

public class Milestone1 {
    private static Scanner scanner = new Scanner(System.in);
    private static Inventory items = new Inventory();

    public static void main(String[] args) {
        items.safelyLoadData();
        
        while (true) {
            System.out.print("Enter command {Add, Remove, Update, SortByBrand, SortByDate, Quit}: ");
            var command = scanner.next();
            command = command.toLowerCase();

            switch (command) {
                case "add": 
                    addItemProc(); 
                    break;
                case "quit":
                    System.out.println("[INVENTORY] Exiting");
                    return;
                default:
                    System.out.println("[INVENTORY] Invalid Command");
            }
        }
    }

    private static void addItemProc() {
        try {
            System.out.print("Name: ");
            var name = scanner.next();

            System.out.print("Engine Number: ");
            var engineNumber = scanner.next();

            System.out.print("Stock Label {New, Old}: ");
            var stockLabel = scanner.next();
            var parsedStockLabel = StockLabel.parse(stockLabel);

            System.out.print("Status {OnHand, Sold}: ");
            var status = scanner.next();
            var parsedStatus = Status.parse(status);

            var newItem = new Item(
                LocalDate.now(),
                parsedStockLabel,
                name,
                engineNumber,
                parsedStatus
            );

            items.add(newItem);
            System.out.println("[INVENTORY] Item Added " + newItem.toString());
        }
        catch(Exception ex) {
            System.out.println("[INVENTORY] An error occurred: "  + ex.toString());
        } 
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

    public LinkedList<Item> sortBy() {
        return this.sortBy(SortBy.Brand);
    }

    public void safelyLoadData()
    {
        var dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.US);
        try (var scanner = new Scanner(new File("MotorPH_Inventory_Data.csv"))) {
            while (scanner.hasNext()) {
                var values = scanner.next();
                var splitValues = values.split(",");
                var item = new Item(
                        LocalDate.parse(splitValues[0], dateFormatter),
                        StockLabel.parse(splitValues[1]),
                        splitValues[2],
                        splitValues[3],
                        Status.parse(splitValues[4]));
                items.add(item);
            }

            System.out.println("Successfully loaded all data");

        }
        catch (Exception e) {
            System.out.println("An exception occurred while loading data: " + e.toString());
        }
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

class LinearSearch {
    public static Item findByEngineNumber(LinkedList<Item> a, String engineNumber) throws InvalidParameterException {
        for (var item : a) {
            if (item.engineNumber.equalsIgnoreCase(engineNumber)) {
                return item;
            }
        }

        throw new InvalidParameterException("EngineNumber of " + engineNumber + " does not exist");
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
    Old, New;

    public static StockLabel parse(String val) throws InvalidParameterException {
        if (val.equalsIgnoreCase("old")) {
            return StockLabel.Old;
        }
        else if (val.equalsIgnoreCase("new")) {
            return StockLabel.New;
        }
        else {
            throw new InvalidParameterException();
        }
    }
}

enum Status {
    Sold, OnHand;

    public static Status parse(String val) throws InvalidParameterException {
        if (val.equalsIgnoreCase("sold")) {
            return Status.Sold;
        }
        else if (val.equalsIgnoreCase("on-hand")) {
            return Status.OnHand;
        }
        else {
            throw new InvalidParameterException();
        }
    }
}