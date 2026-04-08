import java.util.*;

// Reservation (Confirmed Booking)
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType);
    }
}

// Booking History (Storage)
class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    // Add confirmed booking
    public void addReservation(Reservation r) {
        history.add(r);
        System.out.println("✅ Booking stored in history.\n");
    }

    // Retrieve all bookings
    public List<Reservation> getAllReservations() {
        return history;
    }

    // Display all bookings
    public void displayAll() {
        System.out.println("\n📚 Booking History:");

        if (history.isEmpty()) {
            System.out.println("No bookings found.\n");
            return;
        }

        for (Reservation r : history) {
            r.display();
        }
        System.out.println();
    }
}

// Booking Report Service (Read-only reporting)
class BookingReportService {

    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    // Generate summary report
    public void generateSummary() {
        List<Reservation> bookings = history.getAllReservations();

        System.out.println("\n📊 Booking Summary Report:");

        if (bookings.isEmpty()) {
            System.out.println("No data available.\n");
            return;
        }

        Map<String, Integer> roomCount = new HashMap<>();

        for (Reservation r : bookings) {
            roomCount.put(r.getRoomType(),
                    roomCount.getOrDefault(r.getRoomType(), 0) + 1);
        }

        for (String type : roomCount.keySet()) {
            System.out.println(type + " Rooms Booked: " + roomCount.get(type));
        }

        System.out.println("Total Bookings: " + bookings.size() + "\n");
    }
}

// Main Class
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService(history);

        int choice;

        do {
            System.out.println("====== 🏨 Book My Stay (Admin Panel) ======");
            System.out.println("1. Add Confirmed Booking");
            System.out.println("2. View Booking History");
            System.out.println("3. Generate Report");
            System.out.println("4. Exit");
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

                    Reservation r = new Reservation(id, name, type);
                    history.addReservation(r);
                    break;

                case 2:
                    history.displayAll();
                    break;

                case 3:
                    reportService.generateSummary();
                    break;

                case 4:
                    System.out.println("👋 Exiting...");
                    break;

                default:
                    System.out.println("❌ Invalid choice\n");
            }

        } while (choice != 4);

        sc.close();
    }
}