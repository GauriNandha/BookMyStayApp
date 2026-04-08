import java.util.*;

// Reservation Model
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean isCancelled;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isCancelled = false;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        this.isCancelled = true;
    }

    public void display() {
        System.out.println("ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType +
                " | RoomID: " + roomId +
                " | Status: " + (isCancelled ? "Cancelled" : "Active"));
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public void incrementRoom(String type) {
        inventory.put(type, inventory.getOrDefault(type, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("\n📦 Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " -> " + inventory.get(type));
        }
        System.out.println();
    }
}

// Booking History
class BookingHistory {
    private Map<String, Reservation> history = new HashMap<>();

    public void addReservation(Reservation r) {
        history.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String id) {
        return history.get(id);
    }

    public void displayAll() {
        System.out.println("\n📚 Booking History:");
        if (history.isEmpty()) {
            System.out.println("No bookings.\n");
            return;
        }
        for (Reservation r : history.values()) {
            r.display();
        }
        System.out.println();
    }
}

// Cancellation Service
class CancellationService {

    private InventoryService inventory;
    private BookingHistory history;

    // Stack for rollback tracking (LIFO)
    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(InventoryService inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancelBooking(String reservationId) {

        Reservation r = history.getReservation(reservationId);

        // Validation
        if (r == null) {
            System.out.println("❌ Reservation not found.\n");
            return;
        }

        if (r.isCancelled()) {
            System.out.println("❌ Booking already cancelled.\n");
            return;
        }

        // Step 1: Push room ID to rollback stack
        rollbackStack.push(r.getRoomId());

        // Step 2: Restore inventory
        inventory.incrementRoom(r.getRoomType());

        // Step 3: Mark reservation cancelled
        r.cancel();

        System.out.println("🔄 Booking Cancelled Successfully!");
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Room Released: " + r.getRoomId() + "\n");
    }

    public void viewRollbackStack() {
        System.out.println("\n🧱 Rollback Stack (Recent Releases): " + rollbackStack + "\n");
    }
}

// Main Class
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Setup
        InventoryService inventory = new InventoryService();
        inventory.addRoomType("Single", 1);
        inventory.addRoomType("Double", 1);

        BookingHistory history = new BookingHistory();

        // Preload some confirmed bookings
        history.addReservation(new Reservation("R101", "Rahul", "Single", "S101"));
        history.addReservation(new Reservation("R102", "Anita", "Double", "D201"));

        CancellationService cancelService = new CancellationService(inventory, history);

        int choice;

        do {
            System.out.println("====== 🏨 Book My Stay (Cancellation) ======");
            System.out.println("1. View Bookings");
            System.out.println("2. Cancel Booking");
            System.out.println("3. View Inventory");
            System.out.println("4. View Rollback Stack");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    history.displayAll();
                    break;

                case 2:
                    System.out.print("Enter Reservation ID to cancel: ");
                    String id = sc.nextLine();
                    cancelService.cancelBooking(id);
                    break;

                case 3:
                    inventory.displayInventory();
                    break;

                case 4:
                    cancelService.viewRollbackStack();
                    break;

                case 5:
                    System.out.println("👋 Exiting...");
                    break;

                default:
                    System.out.println("❌ Invalid choice\n");
            }

        } while (choice != 5);

        sc.close();
    }
}