import java.util.*;
class Room {
    private String type;
    private double price;
    private List<String> amenities;

    public Room(String type, double price, List<String> amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Price: ₹" + price);
        System.out.println("Amenities: " + String.join(", ", amenities));
        System.out.println("---------------------------");
    }
}
class Inventory {
    private Map<String, Integer> roomAvailability;

    public Inventory() {
        roomAvailability = new HashMap<>();
    }

    public void addRoom(String type, int count) {
        roomAvailability.put(type, count);
    }
    public int getAvailability(String type) {
        return roomAvailability.getOrDefault(type, 0);
    }

    public Set<String> getAllRoomTypes() {
        return roomAvailability.keySet();
    }
}
class SearchService {
    private Inventory inventory;
    private Map<String, Room> roomCatalog;

    public SearchService(Inventory inventory, Map<String, Room> roomCatalog) {
        this.inventory = inventory;
        this.roomCatalog = roomCatalog;
    }

    public void searchAvailableRooms() {
        System.out.println("Available Rooms:\n");

        boolean found = false;

        for (String type : inventory.getAllRoomTypes()) {

            int available = inventory.getAvailability(type);
            if (available > 0) {
                Room room = roomCatalog.get(type);

                if (room != null) { // Defensive programming
                    room.displayDetails();
                    System.out.println("Available Count: " + available);
                    System.out.println();
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No rooms available at the moment.");
        }
    }
}
public class UseCase4RoomSearch {

    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 5);
        inventory.addRoom("Double", 0);
        inventory.addRoom("Suite", 2);
        Map<String, Room> roomCatalog = new HashMap<>();

        roomCatalog.put("Single", new Room(
                "Single",
                2000,
                Arrays.asList("WiFi", "AC", "TV")
        ));
        roomCatalog.put("Double", new Room(
                "Double",
                3500,
                Arrays.asList("WiFi", "AC", "TV", "Mini Bar")
        ));
        roomCatalog.put("Suite", new Room(
                "Suite",
                6000,
                Arrays.asList("WiFi", "AC", "TV", "Mini Bar", "Jacuzzi")
        ));
        SearchService searchService = new SearchService(inventory, roomCatalog);
        searchService.searchAvailableRooms();
    }
}