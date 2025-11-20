import java.util.*;
import java.nio.file.*;
import java.io.*;

public class GlobalManager {

    // Constantes para evitar números mágicos
    private static final double BASE_DOMESTIC_COST = 6.0;
    private static final double BASE_INTERNATIONAL_COST = 8.0;
    private static final double WEIGHT_FACTOR = 0.3;
    private static final double DISTANCE_DIVISOR = 250.0;
    private static final double HEAVY_WEIGHT_THRESHOLD = 15.0;
    private static final double EXTRA_DOMESTIC_COST = 2.0;
    private static final double EXTRA_INTERNATIONAL_COST = 3.0;
    private static final double SAMPLE_ORDER_TOTAL = 199.99;

    private Path dbPath = Paths.get("users.csv");

    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        if (!Files.exists(dbPath)) return users;

        try (BufferedReader br = Files.newBufferedReader(dbPath)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                String id = parts.length > 0 ? parts[0] : "";
                String name = parts.length > 1 ? parts[1] : "";
                String tier = parts.length > 2 ? parts[2] : "";
                users.add(new User(id, name, tier));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }

    public void printUser(User user) {
        System.out.printf("[%s] %s - tier=%s%n", user.getId(), user.getName(), user.getTier());
    }

    public double discountForOrder(User user, double total) {
        String tier = user.getTier();
        if ("gold".equalsIgnoreCase(tier) && total > 200) return total * 0.20;
        if ("silver".equalsIgnoreCase(tier) && total > 50) return total * 0.10;
        return 0.0;
    }

    public double calculateShipping(double weight, double distanceKm, double baseCost, double extraCost) {
        double variable = weight * WEIGHT_FACTOR + (distanceKm / DISTANCE_DIVISOR);
        if (weight > HEAVY_WEIGHT_THRESHOLD) variable += extraCost;
        return baseCost + variable;
    }

    public double shipCostDomestic(double weight, double distanceKm) {
        return calculateShipping(weight, distanceKm, BASE_DOMESTIC_COST, EXTRA_DOMESTIC_COST);
    }

    public double shipCostInternational(double weight, double distanceKm) {
        return calculateShipping(weight, distanceKm, BASE_INTERNATIONAL_COST, EXTRA_INTERNATIONAL_COST);
    }

    public void run() {
        List<User> users = loadUsers();
        for (User user : users) {
            printUser(user);
            double discount = discountForOrder(user, SAMPLE_ORDER_TOTAL);
            System.out.println("Descuento: " + discount);
            System.out.println("Envío nacional: " + shipCostDomestic(12, 900));
            System.out.println("Envío internacional: " + shipCostInternational(12, 900));
        }
    }

    public static void main(String[] args) {
        new GlobalManager().run();
    }
}
