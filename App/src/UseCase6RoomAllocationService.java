import java.util.*;

// Reservation (Booking Request)
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory Service (State Holder)
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void decrementRoom(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }

    public void displayInventory() {
        System.out.println("\n📦 Current Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " -> " + inventory.get(type));
        }
        System.out.println();
    }
}

// Booking Queue (FIFO)
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
        System.out.println("✅ Request added for " + r.getGuestName());
    }

    public Reservation getNextRequest() {
        return queue.poll(); // dequeue
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public void viewQueue() {
        System.out.println("\n📋 Booking Queue:");
        if (queue.isEmpty()) {
            System.out.println("No pending requests.\n");
            return;
        }
        for (Reservation r : queue) {
            System.out.println(r.getGuestName() + " -> " + r.getRoomType());
        }
        System.out.println();
    }
}

// Booking Service (Core Allocation Logic)
class BookingService {

    private InventoryService inventoryService;

    // Track all allocated room IDs (global uniqueness)
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Map room type -> allocated room IDs
    private Map<String, Set<String>> roomAllocations = new HashMap<>();

    public BookingService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void processRequest(Reservation r) {

        if (r == null) {
            System.out.println("⚠️ No request to process.\n");
            return;
        }

        String type = r.getRoomType();

        // Check availability
        if (inventoryService.getAvailability(type) <= 0) {
            System.out.println("❌ No rooms available for " + type + " (Guest: " + r.getGuestName() + ")\n");
            return;
        }

        // Generate unique room ID
        String roomId;
        do {
            roomId = type.substring(0, 2).toUpperCase() + new Random().nextInt(1000);
        } while (allocatedRoomIds.contains(roomId));

        // Add to global set
        allocatedRoomIds.add(roomId);

        // Map room type -> room IDs
        roomAllocations.putIfAbsent(type, new HashSet<>());
        roomAllocations.get(type).add(roomId);

        // Update inventory immediately (atomic step)
        inventoryService.decrementRoom(type);

        // Confirm booking
        System.out.println("🎉 Booking Confirmed!");
        System.out.println("Guest: " + r.getGuestName());
        System.out.println("Room Type: " + type);
        System.out.println("Allocated Room ID: " + roomId + "\n");
    }

    public void displayAllocations() {
        System.out.println("\n🏨 Room Allocations:");
        if (roomAllocations.isEmpty()) {
            System.out.println("No rooms allocated yet.\n");
            return;
        }

        for (String type : roomAllocations.keySet()) {
            System.out.println(type + " -> " + roomAllocations.get(type));
        }
        System.out.println();
    }
}

// Main Class
public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Setup
        InventoryService inventory = new InventoryService();
        inventory.addRoomType("Single", 2);
        inventory.addRoomType("Double", 1);
        inventory.addRoomType("Suite", 1);

        BookingQueue queue = new BookingQueue();
        BookingService bookingService = new BookingService(inventory);

        int choice;

        do {
            System.out.println("====== 🏨 Book My Stay ======");
            System.out.println("1. Add Booking Request");
            System.out.println("2. View Booking Queue");
            System.out.println("3. Process Next Booking");
            System.out.println("4. View Allocations");
            System.out.println("5. View Inventory");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter Guest Name: ");
                    String name = sc.nextLine();

                    System.out.println("Select Room Type:");
                    System.out.println("1. Single  2. Double  3. Suite");
                    int opt = sc.nextInt();
                    sc.nextLine();

                    String type = "";
                    if (opt == 1) type = "Single";
                    else if (opt == 2) type = "Double";
                    else if (opt == 3) type = "Suite";
                    else {
                        System.out.println("❌ Invalid choice\n");
                        break;
                    }

                    queue.addRequest(new Reservation(name, type));
                    break;

                case 2:
                    queue.viewQueue();
                    break;

                case 3:
                    Reservation r = queue.getNextRequest();
                    bookingService.processRequest(r);
                    break;

                case 4:
                    bookingService.displayAllocations();
                    break;

                case 5:
                    inventory.displayInventory();
                    break;

                case 6:
                    System.out.println("👋 Exiting...");
                    break;

                default:
                    System.out.println("❌ Invalid option\n");
            }

        } while (choice != 6);

        sc.close();
    }
}