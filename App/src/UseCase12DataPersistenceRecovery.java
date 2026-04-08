import java.io.*;
import java.util.*;

// Reservation (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public void display() {
        System.out.println("ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType);
    }
}

// System State Wrapper (Serializable)
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<Reservation> bookings;

    public SystemState(Map<String, Integer> inventory, List<Reservation> bookings) {
        this.inventory = inventory;
        this.bookings = bookings;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "hotel_data.ser";

    // Save state
    public static void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("💾 Data saved successfully.\n");

        } catch (IOException e) {
            System.out.println("❌ Error saving data: " + e.getMessage());
        }
    }

    // Load state
    public static SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("📂 Data loaded successfully.\n");
            return (SystemState) ois.readObject();

        } catch (FileNotFoundException e) {
            System.out.println("⚠️ No previous data found. Starting fresh.\n");
        } catch (Exception e) {
            System.out.println("❌ Error loading data. Starting fresh.\n");
        }
        return null;
    }
}

// Main Class
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Try loading existing data
        SystemState loadedState = PersistenceService.load();

        Map<String, Integer> inventory;
        List<Reservation> bookings;

        if (loadedState != null) {
            inventory = loadedState.inventory;
            bookings = loadedState.bookings;
        } else {
            // Initialize fresh state
            inventory = new HashMap<>();
            inventory.put("Single", 2);
            inventory.put("Double", 1);

            bookings = new ArrayList<>();
        }

        int choice;

        do {
            System.out.println("====== 🏨 Book My Stay (Persistence) ======");
            System.out.println("1. Add Booking");
            System.out.println("2. View Bookings");
            System.out.println("3. View Inventory");
            System.out.println("4. Save & Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter Reservation ID: ");
                    String id = sc.nextLine();

                    System.out.print("Enter Guest Name: ");
                    String name = sc.nextLine();

                    System.out.println("Select Room Type:");
                    System.out.println("1. Single  2. Double");
                    int opt = sc.nextInt();
                    sc.nextLine();

                    String type = (opt == 1) ? "Single" : "Double";

                    int available = inventory.getOrDefault(type, 0);

                    if (available <= 0) {
                        System.out.println("❌ No rooms available.\n");
                        break;
                    }

                    // Update state
                    inventory.put(type, available - 1);
                    bookings.add(new Reservation(id, name, type));

                    System.out.println("🎉 Booking added successfully!\n");
                    break;

                case 2:
                    System.out.println("\n📚 Bookings:");
                    if (bookings.isEmpty()) {
                        System.out.println("No bookings.\n");
                    } else {
                        for (Reservation r : bookings) {
                            r.display();
                        }
                        System.out.println();
                    }
                    break;

                case 3:
                    System.out.println("\n📦 Inventory:");
                    for (String t : inventory.keySet()) {
                        System.out.println(t + " -> " + inventory.get(t));
                    }
                    System.out.println();
                    break;

                case 4:
                    // Save before exit
                    SystemState state = new SystemState(inventory, bookings);
                    PersistenceService.save(state);

                    System.out.println("👋 Exiting...");
                    break;

                default:
                    System.out.println("❌ Invalid choice\n");
            }

        } while (choice != 4);

        sc.close();
    }
}