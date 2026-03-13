import java.util.HashMap;
import java.util.Map;

/**
 * UseCase3InventorySetup
 *
 * Demonstrates centralized room inventory management
 * using HashMap to maintain room availability.
 *
 * @author Student
 * @version 3.1
 */

/* Inventory class responsible for managing room availability */
class RoomInventory {

    private HashMap<String, Integer> inventory;

    // Constructor initializes inventory
    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 10);
        inventory.put("Double Room", 5);
        inventory.put("Suite Room", 2);
    }

    // Get availability of a room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability when booking or releasing rooms
    public void updateAvailability(String roomType, int count) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, inventory.get(roomType) + count);
        }
    }

    // Display full inventory
    public void displayInventory() {
        System.out.println("\n--- Current Room Inventory ---");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " rooms available");
        }
    }
}

public class UseCase3InventorySetup {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("      Book My Stay Application      ");
        System.out.println("            Version 3.1             ");
        System.out.println("====================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display current availability
        inventory.displayInventory();

        // Check availability
        System.out.println("\nChecking availability for Double Room...");
        System.out.println("Available: " + inventory.getAvailability("Double Room"));

        // Update inventory (simulate booking)
        System.out.println("\nBooking 1 Double Room...");
        inventory.updateAvailability("Double Room", -1);

        // Display updated inventory
        inventory.displayInventory();
    }
}