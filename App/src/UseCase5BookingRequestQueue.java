import java.util.*;

// Reservation (Represents booking intent)
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

    public void display() {
        System.out.println("Guest: " + guestName + " | Room Type: " + roomType);
    }
}

// Booking Request Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    // Add request
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("✅ Booking request added successfully!\n");
    }

    // View queue
    public void viewRequests() {
        System.out.println("\n📋 Current Booking Queue (FIFO):");

        if (queue.isEmpty()) {
            System.out.println("⚠️ No booking requests available.\n");
            return;
        }

        for (Reservation r : queue) {
            r.display();
        }
        System.out.println();
    }

    // Peek next request
    public void peekNext() {
        System.out.println("\n🔍 Next Request to Process:");

        Reservation r = queue.peek();
        if (r == null) {
            System.out.println("⚠️ Queue is empty.\n");
        } else {
            r.display();
            System.out.println();
        }
    }
}

// Main Class
public class UseCase5BookingRequestQueue {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        int choice;

        do {
            System.out.println("====== 🏨 Book My Stay ======");
            System.out.println("1. Add Booking Request");
            System.out.println("2. View All Requests");
            System.out.println("3. Peek Next Request");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {

                case 1:
                    System.out.print("Enter Guest Name: ");
                    String name = sc.nextLine();

                    System.out.println("Select Room Type:");
                    System.out.println("1. Single");
                    System.out.println("2. Double");
                    System.out.println("3. Suite");
                    System.out.print("Enter option: ");

                    int roomChoice = sc.nextInt();
                    sc.nextLine();

                    String roomType = "";

                    switch (roomChoice) {
                        case 1: roomType = "Single"; break;
                        case 2: roomType = "Double"; break;
                        case 3: roomType = "Suite"; break;
                        default:
                            System.out.println("❌ Invalid room type!\n");
                            continue;
                    }

                    Reservation reservation = new Reservation(name, roomType);
                    bookingQueue.addRequest(reservation);
                    break;

                case 2:
                    bookingQueue.viewRequests();
                    break;

                case 3:
                    bookingQueue.peekNext();
                    break;

                case 4:
                    System.out.println("👋 Exiting... Thank you!");
                    break;

                default:
                    System.out.println("❌ Invalid choice! Try again.\n");
            }

        } while (choice != 4);

        sc.close();
    }
}