import java.util.*;

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

// Shared Booking Queue
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    // synchronized add
    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
        System.out.println("📥 Request added: " + r.getGuestName());
    }

    // synchronized remove
    public synchronized Reservation getNextRequest() {
        return queue.poll();
    }
}

// Inventory (Shared Resource)
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single", 2);
        inventory.put("Double", 1);
    }

    // synchronized critical section
    public synchronized boolean allocateRoom(String type) {
        int available = inventory.getOrDefault(type, 0);

        if (available > 0) {
            inventory.put(type, available - 1);
            return true;
        }
        return false;
    }

    public synchronized void displayInventory() {
        System.out.println("\n📦 Final Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " -> " + inventory.get(type));
        }
    }
}

// Booking Processor (Thread)
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private InventoryService inventory;

    public BookingProcessor(String name, BookingQueue queue, InventoryService inventory) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        while (true) {

            Reservation r;

            // Critical section: fetching request
            synchronized (queue) {
                r = queue.getNextRequest();
            }

            if (r == null) break;

            // Simulate processing delay
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Critical section: allocation
            boolean success = inventory.allocateRoom(r.getRoomType());

            if (success) {
                System.out.println("✅ " + getName() +
                        " booked " + r.getRoomType() +
                        " for " + r.getGuestName());
            } else {
                System.out.println("❌ " + getName() +
                        " failed (No availability) for " + r.getGuestName());
            }
        }
    }
}

// Main Class
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        BookingQueue queue = new BookingQueue();
        InventoryService inventory = new InventoryService();

        // Simulate multiple booking requests
        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Single"));
        queue.addRequest(new Reservation("Charlie", "Single")); // extra request
        queue.addRequest(new Reservation("David", "Double"));
        queue.addRequest(new Reservation("Eve", "Double")); // extra request

        // Create multiple threads (users)
        BookingProcessor t1 = new BookingProcessor("Thread-1", queue, inventory);
        BookingProcessor t2 = new BookingProcessor("Thread-2", queue, inventory);
        BookingProcessor t3 = new BookingProcessor("Thread-3", queue, inventory);

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final inventory state
        inventory.displayInventory();

        System.out.println("\n🎯 All bookings processed safely!");
    }
}