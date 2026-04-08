import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation
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

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void decrementRoom(String type) throws InvalidBookingException {
        int current = inventory.getOrDefault(type, 0);

        if (current <= 0) {
            throw new InvalidBookingException("❌ Cannot allocate room. No availability for " + type);
        }

        inventory.put(type, current - 1);
    }

    public boolean isValidRoomType(String type) {
        return inventory.containsKey(type);
    }

    public void displayInventory() {
        System.out.println("\n📦 Inventory Status:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " -> " + inventory.get(type));
        }
        System.out.println();
    }
}

// Validator (Fail-Fast)
class InvalidBookingValidator {

    public static void validate(Reservation r, InventoryService inventory)
            throws InvalidBookingException {

        if (r.getGuestName() == null || r.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("❌ Guest name cannot be empty.");
        }

        if (!inventory.isValidRoomType(r.getRoomType())) {
            throw new InvalidBookingException("❌ Invalid room type selected.");
        }

        if (inventory.getAvailability(r.getRoomType()) <= 0) {
            throw new InvalidBookingException("❌ Room not available.");
        }
    }
}

// Booking Service
class BookingService {

    private InventoryService inventory;

    public BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    public void bookRoom(Reservation r) {
        try {
            // Step 1: Validate (Fail-Fast)
            InvalidBookingValidator.validate(r, inventory);

            // Step 2: Allocate room
            inventory.decrementRoom(r.getRoomType());

            // Step 3: Confirm booking
            System.out.println("🎉 Booking Successful!");
            System.out.println("Guest: " + r.getGuestName());
            System.out.println("Room Type: " + r.getRoomType() + "\n");

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println(e.getMessage() + "\n");
        }
    }
}

// Main Class
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        InventoryService inventory = new InventoryService();
        inventory.addRoomType("Single", 1);
        inventory.addRoomType("Double", 1);
        inventory.addRoomType("Suite", 0); // intentionally unavailable

        BookingService bookingService = new BookingService(inventory);

        int choice;

        do {
            System.out.println("====== 🏨 Book My Stay (Validation System) ======");
            System.out.println("1. Book Room");
            System.out.println("2. View Inventory");
            System.out.println("3. Exit");
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
                        System.out.println("❌ Invalid option\n");
                        break;
                    }

                    Reservation r = new Reservation(name, type);
                    bookingService.bookRoom(r);
                    break;

                case 2:
                    inventory.displayInventory();
                    break;

                case 3:
                    System.out.println("👋 Exiting...");
                    break;

                default:
                    System.out.println("❌ Invalid choice\n");
            }

        } while (choice != 3);

        sc.close();
    }
}