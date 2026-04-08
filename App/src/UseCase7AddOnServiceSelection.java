import java.util.*;

// Add-On Service (Independent Entity)
class AddOnService {
    private String name;
    private double price;

    public AddOnService(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void display() {
        System.out.println(name + " - ₹" + price);
    }
}

// Add-On Service Manager (Core Logic)
class AddOnServiceManager {

    // Map: Reservation ID -> List of Services
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    // Add service to reservation
    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);

        System.out.println("✅ Service added to Reservation ID: " + reservationId);
    }

    // View services
    public void viewServices(String reservationId) {
        System.out.println("\n🧾 Services for Reservation: " + reservationId);

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services added.\n");
            return;
        }

        for (AddOnService s : services) {
            s.display();
        }
        System.out.println();
    }

    // Calculate total cost
    public void calculateTotalCost(String reservationId) {
        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No additional cost.\n");
            return;
        }

        double total = 0;
        for (AddOnService s : services) {
            total += s.getPrice();
        }

        System.out.println("💰 Total Add-On Cost: ₹" + total + "\n");
    }
}

// Main Class
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        AddOnServiceManager manager = new AddOnServiceManager();

        // Predefined services (Extensible)
        Map<Integer, AddOnService> availableServices = new HashMap<>();
        availableServices.put(1, new AddOnService("Breakfast", 500));
        availableServices.put(2, new AddOnService("Airport Pickup", 1200));
        availableServices.put(3, new AddOnService("Extra Bed", 800));
        availableServices.put(4, new AddOnService("Spa Access", 1500));

        int choice;

        do {
            System.out.println("====== 🏨 Book My Stay (Add-On Services) ======");
            System.out.println("1. Add Service to Reservation");
            System.out.println("2. View Services");
            System.out.println("3. Calculate Total Cost");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter Reservation ID: ");
                    String resId = sc.nextLine();

                    System.out.println("\nAvailable Services:");
                    for (Map.Entry<Integer, AddOnService> entry : availableServices.entrySet()) {
                        System.out.print(entry.getKey() + ". ");
                        entry.getValue().display();
                    }

                    System.out.print("Select service option: ");
                    int opt = sc.nextInt();
                    sc.nextLine();

                    AddOnService selected = availableServices.get(opt);

                    if (selected == null) {
                        System.out.println("❌ Invalid option\n");
                        break;
                    }

                    manager.addService(resId, selected);
                    break;

                case 2:
                    System.out.print("Enter Reservation ID: ");
                    manager.viewServices(sc.nextLine());
                    break;

                case 3:
                    System.out.print("Enter Reservation ID: ");
                    manager.calculateTotalCost(sc.nextLine());
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