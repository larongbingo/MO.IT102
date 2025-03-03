import java.io.File;
import java.io.FileWriter;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

// Note: some of the class definitions is in Milestone1.java
public class Milestone2 {
    private static InventoryMS2 inventory = new InventoryMS2();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        // Fetch the data from the CSV file
        if (inventory.loadData()) {
            System.out.println("[MS2 INVENTORY] Successfully loaded data");
        } else {
            System.out.println("[MS2 INVENTORY] Failed to load data");
        }

        while (true) {
            System.out.print("Enter command {Add, Remove, Update, PrintSortByBrand, PrintSortByDate, StatusFilter, BrandFilter, StockLabelFilter, ResetFilters, Save, (Q)uit}: ");
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
                case "statusfilter":
                    setStatusFilter();
                    break;
                case "brandfilter":
                    setBrandFilter();
                    break;
                case "stocklabelfilter":
                    setStockLabelFilter();
                    break;
                case "resetfilters":
                    LinearSearchMS2.brand = "";
                    LinearSearchMS2.stockLabelFilter = StockLabelFilter.None;
                    LinearSearchMS2.statusFilter = StatusFilter.None;
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

    /// Steps to add a new stock
    private static void addProcedure() {
        try {
            System.out.println("[MS2 INVENTORY] Adding a new Stock");

            // Fetch the Brand Name of the Stock
            System.out.print("[MS2 INVENTORY] Name: ");
            var name = scanner.nextLine();

            // Fetch the Engine Number of the Stock
            // This will serve as its Primary Id
            System.out.print("[MS2 INVENTORY] Engine Number: ");
            var engineNumber = scanner.nextLine();

            // Fetch and parse the Stock Label 
            System.out.print("[MS2 INVENTORY] Stock Label {New, Old}: ");
            var stockLabel = scanner.nextLine();
            var parsedStockLabel = StockLabel.parse(stockLabel);

            // Fetch and parse the Status
            System.out.print("[MS2 INVENTORY] Status {OnHand, Sold}: ");
            var status = scanner.nextLine();
            var parsedStatus = Status.parse(status);

            // Create the new Stock
            // The Date is set to today since it was created just now
            var newItem = new Item(
                LocalDate.now(),
                parsedStockLabel,
                name,
                engineNumber,
                parsedStatus
            );

            // Display if the stock is added or not
            var result = inventory.add(newItem);
            if (result) {
                System.out.println("[MS2 INVENTORY] Succesfully added new stock - " + newItem);
            } else {
                System.out.println("[MS2 INVENTORY] Engine Number already taken");
            }
        } catch(Exception ex) {
            System.out.println("[MS2 INVENTORY] An error occurred: "  + ex.toString());
        }
    }

    /// Steps to update a Stock from the Inventory
    private static void updateItemProcedure() {
        try {
            System.out.print("[MS2 INVENTORY] Updating stock");

            // Fetch the engine number
            System.out.print("Enter Engine Number: ");
            var engineNumber = scanner.nextLine();
            
            // Check if the engine number is valid
            var itemToBeUpdated = inventory.get(engineNumber);
            if (itemToBeUpdated == null) {
                System.out.println("[MS2 INVENTORY] Invalid Engine Number");
                return;
            }

            // Fetch the updated value for the Stock's Brand
            System.out.print(String.format("[MS2 INVENTORY] New Value for the Brand; Leave blank if current is ok - Current {%s} : ", itemToBeUpdated.brand));
            var newBrand = scanner.nextLine();
            if (!newBrand.isEmpty()) {
                itemToBeUpdated.brand = newBrand;
            }

            // Fetch the updated value for the Stock's Status
            System.out.print(String.format("[MS2 INVENTORY] New Value for the Status {Sold, OnHand}; Leave blank if current is ok - Current {%s} : ", itemToBeUpdated.brand));
            var newStatus = scanner.nextLine();
            if (!newStatus.isEmpty()) {
                itemToBeUpdated.status = Status.parse(newStatus);
            }

            // Fetch the updated value for the Stock's StockLabel
            System.out.print(String.format("[MS2 INVENTORY] New Value for the StockLabel {New, Old}; Leave blank if current is ok - Current {%s} : ", itemToBeUpdated.brand));
            var newStock = scanner.nextLine();
            if (!newStock.isEmpty()) {
                itemToBeUpdated.stockLabel = StockLabel.parse(newStock);
            }
        }
        catch (InvalidStatusStringParameterException statusExc) {
            // Failed to parse the string due to invalid string input
            System.out.println("[MS2 INVENTORY] Invalid input. The following are the only valid {OnHand, Sold}");
        }
        catch (InvalidStockLabelStringParameterException stockExc) {
            // Failed to parse the string due to invalid string input
            System.out.println("[MS2 INVENTORY] Invalid input. The following are the only valid {Old, New}");
        }
    }

    /// Steps to remove a Stock from the Inventory
    private static void removeItemProcedure() {
        try {
            System.out.println("[MS2 INVENTORY] Removing an item");

            // Fetch the engine number
            System.out.print("Engine Number of the item to be removed: ");
            var engineNumber = scanner.nextLine();

            // Try removing the item
            var result = inventory.remove(engineNumber);

            // Display result
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

    private static void setStatusFilter() {
        try {
            System.out.println("[MS2 INVENTORY] Setting Status Filter");

            System.out.print("Enter Filter {0 - None, 1 - Sold, 2 - OnHand}: ");
            var filterString = scanner.nextLine();
            LinearSearchMS2.statusFilter = StatusFilter.parse(filterString);

            System.out.println("[MS2 INVENTORY] Status Filter set");
        }
        catch (Exception e) {
            System.out.println("[MS2 INVENTORY] Invalid input");
        }
    }

    private static void setStockLabelFilter() {
        try {
            System.out.println("[MS2 INVENTORY] Setting Stock Label Filter");

            System.out.print("Enter Filter {0 - None, 1 - Old, 2 - New}: ");
            var filterString = scanner.nextLine();
            LinearSearchMS2.stockLabelFilter = StockLabelFilter.parse(filterString);

            System.out.println("[MS2 INVENTORY] Stock Label Filter set");
        }
        catch (Exception e) {
            System.out.println("[MS2 INVENTORY] Invalid input");
        }
    }

    private static void setBrandFilter() {
        try {
            System.out.println("[MS2 INVENTORY] Setting Brand Filter");

            System.out.print("Enter Brand: ");
            LinearSearchMS2.brand = scanner.nextLine();

            System.out.println("[MS2 INVENTORY] Brand Filter Set");
        }
        catch (Exception e) {
            System.out.println("[MS2 INVENTORY] An error occurred");
        }
    }
}

class InventoryMS2 {
    public HashMap<String, Item> items =  new HashMap<>();

    public boolean add(Item item) {
        // Confirm that the key/engine number is unique
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
        // Allows to change what property is order at runtime
        Comparator<Item> comparator = null;
        switch (sortBy) {
            case DateEntered -> comparator = Comparator.comparing((Item o) -> o.dateEntered);
            case Brand -> comparator = Comparator.comparing((Item o) -> o.brand, String::compareToIgnoreCase);
        }

        // Filter anything that matches the set filter options
        var filtered = LinearSearchMS2.filter(new ArrayList<Item>(items.values()));

        // Merge Sort
        var result = MergeSortMS2.sort(filtered, comparator);

        System.out.println("Filtered by StockLabel = " + LinearSearchMS2.stockLabelFilter + ", Status = " + LinearSearchMS2.statusFilter + ", Brand = " + LinearSearchMS2.brand);

        // Print all items
        for (Item item : result) {
            System.out.println(item);
        }
    }

    /// Fetches any data in the CSV file
    public boolean loadData() {
        // Date Formatter
        var dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.US);
        
        // Scanner to read individual lines in the CSV file
        try (var scanner = new Scanner(new File("MotorPH_Inventory_Data.csv"))) {
            
            // Iterate while it still has Lines it hasn't read
            while (scanner.hasNextLine()) {
                // Fetch the line
                var values = scanner.nextLine();

                // Split the CSV string
                var splitValues = values.split(",");
                
                // Parse and instantiate the object
                var item = new Item(
                        LocalDate.parse(splitValues[0], dateFormatter),
                        StockLabel.parse(splitValues[1]),
                        splitValues[2],
                        splitValues[3],
                        Status.parse(splitValues[4]));

                // Load into the List
                this.add(item);
            }

            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /// List into a CSV file
    public boolean saveData(String fileName) {
        var file = new File(fileName);

        // Prevent overwriting existing item
        if (file.exists()) {
            System.out.println("File already exists");
            return false;
        }

        // Print all items into the file
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

/// Implemented the out of place Merge Sort for code simplicity, but affects
/// performance due to mallocs
class MergeSortMS2 {
    public static ArrayList<Item> sort(ArrayList<Item> a, Comparator<Item> c) {
        int size = a.size();

        if (size <= 1) {
            return a;
        }

        // Split the array in half
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

        // Recursively split and sort both list halves
        left = sort(left, c);
        right = sort(right, c);

        // Merge both lists
        return merge(left, right, c);
    }

    public static ArrayList<Item> merge(ArrayList<Item> a, ArrayList<Item> b, Comparator<Item> c) {
        var merged = new ArrayList<Item>();

        while (!a.isEmpty() && !b.isEmpty()) {
            // Move the current largest on both lists
            if (c.compare(a.getFirst(), b.getFirst()) > 0) {
                merged.add(b.removeFirst());
            } else {
                merged.add(a.removeFirst());
            }    
        }

        // Move the remaining items of list A
        while (!a.isEmpty()) {
            merged.add(a.removeFirst());
        }

        // Move the remaining items of list B
        while (!b.isEmpty()) {
            merged.add(b.removeFirst());
        }

        return merged;
    }
}

/// Filters all Items linearly
class LinearSearchMS2 {
    public static String brand = "";
    public static StatusFilter statusFilter = StatusFilter.None;
    public static StockLabelFilter stockLabelFilter = StockLabelFilter.None;

    /// Possibly inefficient but it has the simplest code to filter out that doesn't match
    /// the criteria
    public static ArrayList<Item> filter(ArrayList<Item> items) {
        var firstFilter = filterByBrand(items);
        var secondFilter = filterByStockLabel(firstFilter);
        var thirdFilter = filterByStatus(secondFilter);

        return thirdFilter;
    }

    private static ArrayList<Item> filterByBrand(ArrayList<Item> items) {
        if (Objects.equals(brand, "")) {
            return items;
        }

        var filtered = new ArrayList<Item>();

        for(var item : items) {
            if (item.brand.toLowerCase().contains(brand.toLowerCase())) {
                filtered.add(item);
            }
        }

        return filtered;
    }

    private static ArrayList<Item> filterByStockLabel(ArrayList<Item> items) {
        if (stockLabelFilter == StockLabelFilter.None) {
            return items;
        }

        var filtered = new ArrayList<Item>();

        var stockLabel = StockLabelFilter.convert(stockLabelFilter);

        for (var item : items) {
            if (item.stockLabel == stockLabel) {
                filtered.add(item);
            }
        }

        return filtered;
    }

    private static ArrayList<Item> filterByStatus(ArrayList<Item> items) {
        if (statusFilter == StatusFilter.None) {
            return items;
        }

        var filtered = new ArrayList<Item>();

        var status = StatusFilter.convert(statusFilter);

        for(var item : items) {
            if (item.status == status) {
                filtered.add(item);
            }
        }

        return filtered;
    }
}

enum StatusFilter {
    None,
    Sold,
    OnHand;

    public static StatusFilter parse(String str) throws InvalidStatusFilterParameterException {
        return switch (str) {
            case "0" -> StatusFilter.None;
            case "1" -> StatusFilter.Sold;
            case "2" -> StatusFilter.OnHand;
            default -> throw new InvalidStatusFilterParameterException();
        };
    }

    public static Status convert(StatusFilter filter) {
        return switch(filter) {
            case StatusFilter.Sold -> Status.Sold;
            case StatusFilter.OnHand -> Status.OnHand;
            default -> throw new InvalidStatusFilterParameterException();
        };
    }
}

enum StockLabelFilter {
    None,
    Old,
    New;

    public static StockLabelFilter parse(String str) throws InvalidStockLabelFilterParameterException {
        return switch (str) {
            case "0" -> StockLabelFilter.None;
            case "1" -> StockLabelFilter.Old;
            case "2" -> StockLabelFilter.New;
            default -> throw new InvalidStockLabelStringParameterException();
        };
    }

    public static StockLabel convert(StockLabelFilter filter) throws InvalidStockLabelFilterParameterException {
        return switch (filter) {
            case StockLabelFilter.Old -> StockLabel.Old;
            case StockLabelFilter.New -> StockLabel.New;
            default -> throw new InvalidStockLabelFilterParameterException();
        };
    }
}

class InvalidStatusFilterParameterException extends InvalidParameterException {}
class InvalidStockLabelFilterParameterException extends InvalidParameterException {}