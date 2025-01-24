import java.util.HashMap;

public class Milestone2 {

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
        var item = items.get(engineNumber);

        if (item != null) {
            return false;
        }

    }
}
