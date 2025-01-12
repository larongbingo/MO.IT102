import java.io.File;
import java.io.FileWriter;
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
            System.out.print("Enter command {Add, Remove, Update, SortByBrand, SortByDate, Print, Save, (Q)uit}: ");
            var command = scanner.next();
            command = command.toLowerCase();

            switch (command) {
                case "add": 
                    addItemProc(); 
                    break;
                case "remove":
                    removeItemProc();
                    break;
                case "update":
                    updateItemProc();
                    break;
                case "print":
                    items.printList();
                    break;
                case "sortbybrand":
                    items.sortBy(SortBy.Brand);
                    items.printList();
                    break;
                case "sortbydate":
                    items.sortBy(SortBy.DateEntered);
                    items.printList();
                    break;
                case "save":
                    saveItemsProc();
                    break;
                case "quit":
                case "q":
                    System.out.println("[INVENTORY] Exiting");
                    return;
                default:
                    System.out.println("[INVENTORY] Invalid Command");
            }
        }
    }

    private static void saveItemsProc() {
        try {
            System.out.print("Enter filename: ");
            var fileName = scanner.next();

            if (fileName.isEmpty()) {
                System.out.println("[INVENTORY] File name can't be empty");
                return;
            }

            items.safelySaveData(fileName);
        }
        catch (Exception e) {
            System.out.println("[INVENTORY] An error occurred: " + e);
        }
    }

    private static void updateItemProc() {
        try {
            System.out.print("Enter Engine Number: ");
            var engineNumber = scanner.next();

            var itemToBeUpdated = LinearSearch.findByEngineNumber(items.items, engineNumber);

            System.out.print(String.format("New Value for the Brand; Leave blank if current is ok - Current {%s} : ", itemToBeUpdated.brand));
            var newBrand = scanner.next();
            if (!newBrand.isEmpty()) {
                itemToBeUpdated.brand = newBrand;
            }

            System.out.print(String.format("New Value for the Status {Sold, OnHand}; Leave blank if current is ok - Current {%s} : ", itemToBeUpdated.brand));
            var newStatus = scanner.next();
            if (!newStatus.isEmpty()) {
                itemToBeUpdated.status = Status.parse(newStatus);
            }

            System.out.print(String.format("New Value for the StockLabel {New, Old}; Leave blank if current is ok - Current {%s} : ", itemToBeUpdated.brand));
            var newStock = scanner.next();
            if (!newStock.isEmpty()) {
                itemToBeUpdated.stockLabel = StockLabel.parse(newStock);
            }
        }
        catch (InvalidEngineNumberParameterException engineNumberExc) {
            System.out.println("[INVENTORY] EngineNumber not found");
        }
        catch (InvalidStatusStringParameterException statusExc) {
            System.out.println("[INVENTORY] Invalid input. The following are the only valid {OnHand, Sold}");
        }
        catch (InvalidStockLabelStringParameterException stockExc) {
            System.out.println("[INVENTORY] Invalid input. The following are the only valid {Old, New}");
        }
    }

    private static void removeItemProc() {
        try {
            System.out.print("Engine Number of the item to be removed: ");
            var engineNumber = scanner.next();

            var itemToBeRemoved = LinearSearch.findByEngineNumber(items.items, engineNumber);

            var result = items.remove(itemToBeRemoved);

            if (result) {
                System.out.println("[INVENTORY] Item has been removed");
            }
            else {
                System.out.println("[INVENTORY] Item has not been removed");
            }
        }
        catch (Exception e) {
            System.out.println("Engine Number is not valid");
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

            var result = items.add(newItem);
            if (result) {
                System.out.println("[INVENTORY] Item Added " + newItem.toString());
            }
            else {
                System.out.println("[INVENTORY] Engine Number already added used");
            }
        }
        catch(Exception ex) {
            System.out.println("[INVENTORY] An error occurred: "  + ex.toString());
        } 
    }
}

class Inventory {
    public LinkedList<Item> items = new LinkedList<>();

    public boolean add(Item item) {
        if (LinearSearch.containsByEngineNumber(items, item.engineNumber)) {
            return false;
        }

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

        var result = BubbleSort.sort(items, comparator);

        return result;
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

            System.out.println("[INVENTORY] Successfully loaded all data");

        }
        catch (Exception e) {
            System.out.println("An exception occurred while loading data: " + e.toString());
        }
    }

    public void safelySaveData(String fileName)
    {
        var file = new File(fileName);

        if (file.exists()) {
            System.out.println("File already exists");
            return;
        }

        try (var fileWriter = new FileWriter(file)) {
            for (var item : items) {
                fileWriter.write(item.toCsv() + "\n");
            }

            fileWriter.close();

            System.out.println("Successfully saved the file");
        } catch (Exception e) {
            System.out.println("An exception occurred while saving data: " + e.toString());
        }
    }

    public void printList() {
        for(var item : items) {
            System.out.println(item);
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
    public static Item findByEngineNumber(LinkedList<Item> a, String engineNumber) throws InvalidEngineNumberParameterException {
        for (var item : a) {
            if (item.engineNumber.equalsIgnoreCase(engineNumber)) {
                return item;
            }
        }

        throw new InvalidEngineNumberParameterException();
    }

    public static boolean containsByEngineNumber(LinkedList<Item> a, String engineNumber) {
        try {
            var item = findByEngineNumber(a, engineNumber);
            return item != null;
        }
        catch (Exception e) {
            return false;
        }
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

    public String toCsv() {
        return String.format("%s,%s,%s,%s,%s", dateEntered, stockLabel, brand, engineNumber, status);
    }
}

enum SortBy {
    DateEntered,
    Brand,
}

enum StockLabel {
    Old, New;

    public static StockLabel parse(String val) throws InvalidStockLabelStringParameterException {
        if (val.equalsIgnoreCase("old")) {
            return StockLabel.Old;
        }
        else if (val.equalsIgnoreCase("new")) {
            return StockLabel.New;
        }
        else {
            throw new InvalidStockLabelStringParameterException();
        }
    }
}

enum Status {
    Sold, OnHand;

    public static Status parse(String val) throws InvalidStatusStringParameterException {
        if (val.equalsIgnoreCase("sold")) {
            return Status.Sold;
        }
        else if (val.equalsIgnoreCase("on-hand") || val.equalsIgnoreCase("onhand")) {
            return Status.OnHand;
        }
        else {
            throw new InvalidStatusStringParameterException();
        }
    }
}

class InvalidEngineNumberParameterException extends InvalidParameterException {}
class InvalidStockLabelStringParameterException extends InvalidParameterException {}
class InvalidStatusStringParameterException extends InvalidParameterException {}