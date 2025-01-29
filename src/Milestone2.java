import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

// Note: some of the class definitions is in Milestone1.java
public class Milestone2 {
    private static InventoryMS2 inventory = new InventoryMS2();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        if (inventory.loadData()) {
            System.out.println("[MS2 INVENTORY] Successfully loaded data");
        } else {
            System.out.println("[MS2 INVENTORY] Failed to load data");
        }
        while (true) {
            
            
            System.out.print("Enter command {Add, Remove, Update, PrintSortByBrand, PrintSortByDate, Save, (Q)uit}: ");
            var command = scanner.nextLine();
            command = command.toLowerCase();

            switch (command) {
                case "add":
                    addProcedure();
                    break;

                case "remove":
                    removeItemProcedure();
                    break;

                case "update":
                    updateItemProcedure();
                    break;

                case "printsortbybrand":
                    inventory.printSorted(SortBy.Brand);
                    break;
                case "printsortbydate":
                    inventory.printSorted(SortBy.DateEntered);
                    break;
                
                case "save":
                    inventory.saveData("MS2_INVENTORY.csv");
                    System.out.print("[MS2 INVENTORY] Data saved into MS2_INVENTORY.csv");
                    break;

                case "q":
                case "quit":
                    return;
            
                default:
                    System.out.println("[MS2 INVENTORY] Invalid Command");
                    break;
            }
        }
    }

    private static void addProcedure() {
        try {
            System.out.println("[MS2 INVENTORY] Adding a new Stock");
            System.out.print("[MS2 INVENTORY] Name: ");
            var name = scanner.nextLine();

            System.out.print("[MS2 INVENTORY] Engine Number: ");
            var engineNumber = scanner.nextLine();

            System.out.print("[MS2 INVENTORY] Stock Label {New, Old}: ");
            var stockLabel = scanner.nextLine();
            var parsedStockLabel = StockLabel.parse(stockLabel);

            System.out.print("[MS2 INVENTORY] Status {OnHand, Sold}: ");
            var status = scanner.nextLine();
            var parsedStatus = Status.parse(status);

            var newItem = new Item(
                LocalDate.now(),
                parsedStockLabel,
                name,
                engineNumber,
                parsedStatus
            );

            inventory.add(newItem);

            System.out.println("[MS2 INVENTORY] Succesfully added new stock - " + newItem);
        } catch(Exception ex) {
            System.out.println("[MS2 INVENTORY] An error occurred: "  + ex.toString());
        }
    }

    private static void updateItemProcedure() {
        try {
            System.out.print("[MS2 INVENTORY] Updating stock");
            System.out.print("Enter Engine Number: ");
            var engineNumber = scanner.nextLine();
            
            var itemToBeUpdated = inventory.get(engineNumber);
            if (itemToBeUpdated == null) {
                System.out.println("[MS2 INVENTORY] Invalid Engine Number");
                return;
            }

            System.out.print(String.format("[MS2 INVENTORY] New Value for the Brand; Leave blank if current is ok - Current {%s} : ", itemToBeUpdated.brand));
            var newBrand = scanner.nextLine();
            if (!newBrand.isEmpty()) {
                itemToBeUpdated.brand = newBrand;
            }

            System.out.print(String.format("[MS2 INVENTORY] New Value for the Status {Sold, OnHand}; Leave blank if current is ok - Current {%s} : ", itemToBeUpdated.brand));
            var newStatus = scanner.nextLine();
            if (!newStatus.isEmpty()) {
                itemToBeUpdated.status = Status.parse(newStatus);
            }

            System.out.print(String.format("[MS2 INVENTORY] New Value for the StockLabel {New, Old}; Leave blank if current is ok - Current {%s} : ", itemToBeUpdated.brand));
            var newStock = scanner.nextLine();
            if (!newStock.isEmpty()) {
                itemToBeUpdated.stockLabel = StockLabel.parse(newStock);
            }
        }
        catch (InvalidEngineNumberParameterException engineNumberExc) {
            System.out.println("[MS2 INVENTORY] EngineNumber not found");
        }
        catch (InvalidStatusStringParameterException statusExc) {
            System.out.println("[MS2 INVENTORY] Invalid input. The following are the only valid {OnHand, Sold}");
        }
        catch (InvalidStockLabelStringParameterException stockExc) {
            System.out.println("[MS2 INVENTORY] Invalid input. The following are the only valid {Old, New}");
        }
    }

    private static void removeItemProcedure() {
        try {
            System.out.print("Engine Number of the item to be removed: ");
            var engineNumber = scanner.nextLine();

            var result = inventory.remove(engineNumber);

            if (result) {
                System.out.println("[MS2 INVENTORY] Item has been removed");
            }
            else {
                System.out.println("[MS2 INVENTORY] Item has not been removed");
            }
        }
        catch (Exception e) {
            System.out.println("Engine Number is not valid");
        }
    }
}

class InventoryMS2 {
    public HashMap<String, Item> items =  new HashMap<>();

    public boolean add(Item item) {
        if (items.containsKey(item.engineNumber)) {
            return false;
        }

        items.put(item.engineNumber, item);
        return true;
    }

    public boolean remove(String engineNumber) {
        var item = items.remove(engineNumber);
        return item != null;
    }

    public Item get(String engineNumber) {
        var item = items.get(engineNumber);
        return item;
    }

    public void printSorted(SortBy sortBy) {
        Comparator<Item> comparator = null;
        switch (sortBy) {
            case DateEntered -> comparator = Comparator.comparing((Item o) -> o.dateEntered);
            case Brand -> comparator = Comparator.comparing((Item o) -> o.brand, String::compareToIgnoreCase);
        }

        var result = MergeSortMS2.sort(new ArrayList<Item>(items.values()), comparator);

        for (Item item : result) {
            System.out.println(item);
        }
    }

    public boolean loadData() {
        var dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.US);
        try (var scanner = new Scanner(new File("MotorPH_Inventory_Data.csv"))) {
            while (scanner.hasNextLine()) {
                var values = scanner.nextLine();
                var splitValues = values.split(",");
                var item = new Item(
                        LocalDate.parse(splitValues[0], dateFormatter),
                        StockLabel.parse(splitValues[1]),
                        splitValues[2],
                        splitValues[3],
                        Status.parse(splitValues[4]));
                this.add(item);
            }

            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public boolean saveData(String fileName) {
        var file = new File(fileName);

        if (file.exists()) {
            System.out.println("File already exists");
            return false;
        }

        try (var fileWriter = new FileWriter(file)) {
            var values = items.values();
            for (var item : values) {
                fileWriter.write(item.toCsv() + "\n");
            }

            fileWriter.close();

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

class MergeSortMS2 {
    public static ArrayList<Item> sort(ArrayList<Item> a, Comparator<Item> c) {
        int size = a.size();

        if (size <= 1) {
            return a;
        }

        var mid = size/2;
        var left = new ArrayList<Item>();
        var right = new ArrayList<Item>();
        for (int i = 0; i < size; i++) {
            if (i < mid) {
                left.add(a.get(i));
            } else {
                right.add(a.get(i));
            }
        }

        left = sort(left, c);
        right = sort(right, c);

        return merge(left, right, c);
    }

    public static ArrayList<Item> merge(ArrayList<Item> a, ArrayList<Item> b, Comparator<Item> c) {
        var merged = new ArrayList<Item>();

        while (!a.isEmpty() && !b.isEmpty()) {
            if (c.compare(a.getFirst(), b.getFirst()) > 0) {
                merged.add(b.removeFirst());
            } else {
                merged.add(a.removeFirst());
            }    
        }

        while (!a.isEmpty()) {
            merged.add(a.removeFirst());
        }

        while (!b.isEmpty()) {
            merged.add(b.removeFirst());
        }

        return merged;
    }
}
