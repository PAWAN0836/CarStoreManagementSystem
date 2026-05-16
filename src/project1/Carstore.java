package project1;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Carstore {

    // ── helper to build car object from ResultSet ──────────────
    private Car buildCar(ResultSet rs) throws SQLException {
        return new Car(
            rs.getInt("id"),
            rs.getString("model"),
            rs.getString("brand"),
            rs.getDouble("price"),
            rs.getString("fuel_type"),
            rs.getInt("year"),
            CarStatus.valueOf(rs.getString("status")),
            rs.getString("booked_by"),
            rs.getString("review"),
            rs.getString("booking_date")
        );
    }

    // ── GET ALL cars from DB ────────────────────────────────────
    private List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM cars";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            while (rs.next()) cars.add(buildCar(rs));
        } catch (SQLException e) {
            System.out.println("DB Error: " + e.getMessage());
        }
        return cars;
    }

    // ── DISPLAY ALL ─────────────────────────────────────────────
    public void displayCars() {
        List<Car> cars = getAllCars();
        if (cars.isEmpty()) { System.out.println("No cars found."); return; }
        System.out.println("\n--- All Cars ---");
        cars.forEach(System.out::println);
    }

    // ── ADD CAR ─────────────────────────────────────────────────
    public void addCar(String model, String brand, double price,
                       String fuelType, int year) {
        // Validation
        if (model.trim().isEmpty() || brand.trim().isEmpty())
            throw new IllegalArgumentException("Model and Brand cannot be empty.");
        if (price <= 0)
            throw new IllegalArgumentException("Price must be greater than 0.");
        if (year < 2000 || year > LocalDateTime.now().getYear())
            throw new IllegalArgumentException("Enter a valid year (2000 - present).");

        String sql = "INSERT INTO cars (model, brand, price, fuel_type, year, status) " +
                     "VALUES (?, ?, ?, ?, ?, 'AVAILABLE')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, model.trim());
            ps.setString(2, brand.trim());
            ps.setDouble(3, price);
            ps.setString(4, fuelType.trim());
            ps.setInt(5, year);
            ps.executeUpdate();
            System.out.println("Car added successfully.");
        } catch (SQLException e) {
            System.out.println("DB Error: " + e.getMessage());
        }
    }

    // ── REMOVE CAR ──────────────────────────────────────────────
    public void removeCar(String model) {
        if (model.trim().isEmpty())
            throw new IllegalArgumentException("Model name cannot be empty.");
        String sql = "DELETE FROM cars WHERE model = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, model.trim());
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Car removed." : "Car not found.");
        } catch (SQLException e) {
            System.out.println("DB Error: " + e.getMessage());
        }
    }

    // ── BOOK CAR ────────────────────────────────────────────────
    public void bookCar(String model, Customer c) {
        // Date & Time stamp on booking
        String bookingDate = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        String sql = "UPDATE cars SET status='BOOKED', booked_by=?, booking_date=? " +
                     "WHERE model=? AND status='AVAILABLE'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, bookingDate);
            ps.setString(3, model.trim());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Car booked successfully!");
                System.out.println(c);
                System.out.println("Booking Date: " + bookingDate);
                generateBill(model, c, bookingDate); // auto-generate bill
            } else {
                System.out.println("Car not available or model not found.");
            }
        } catch (SQLException e) {
            System.out.println("DB Error: " + e.getMessage());
        }
    }

    // ── GENERATE BILL ───────────────────────────────────────────
    private void generateBill(String model, Customer c, String date) {
        String sql = "SELECT price FROM cars WHERE model = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, model);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double base  = rs.getDouble("price");
                double gst   = base * 0.18;           // 18% GST
                double total = base + gst;
                System.out.println("\n====== BILL RECEIPT ======");
                System.out.println("Customer : " + c.getName());
                System.out.println("Phone    : " + c.getPhone());
                System.out.println("Car      : " + model);
                System.out.printf( "Base     : Rs.%.2f%n", base);
                System.out.printf( "GST 18%%  : Rs.%.2f%n", gst);
                System.out.printf( "Total    : Rs.%.2f%n", total);
                System.out.println("Date     : " + date);
                System.out.println("==========================");
            }
        } catch (SQLException e) {
            System.out.println("Bill generation error: " + e.getMessage());
        }
    }

    // ── ADD REVIEW ──────────────────────────────────────────────
    public void addReview(String model, String review) {
        if (review.trim().isEmpty())
            throw new IllegalArgumentException("Review cannot be empty.");
        String sql = "UPDATE cars SET review=? WHERE model=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, review.trim());
            ps.setString(2, model.trim());
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Review added." : "Car not found.");
        } catch (SQLException e) {
            System.out.println("DB Error: " + e.getMessage());
        }
    }

    // ── SEARCH BY BRAND ─────────────────────────────────────────
    public void searchByBrand(String keyword) {
        List<Car> results = getAllCars().stream()
            .filter(c -> c.getBrand().toLowerCase()
                          .contains(keyword.trim().toLowerCase()))
            .collect(Collectors.toList());
        if (results.isEmpty()) { System.out.println("No cars found for: " + keyword); return; }
        System.out.println("\n--- Search Results ---");
        results.forEach(System.out::println);
    }

    // ── FILTER BY FUEL TYPE ─────────────────────────────────────
    public void filterByFuelType(String fuelType) {
        List<Car> results = getAllCars().stream()
            .filter(c -> c.getFuelType().equalsIgnoreCase(fuelType.trim()))
            .collect(Collectors.toList());
        if (results.isEmpty()) { System.out.println("No cars found."); return; }
        System.out.println("\n--- " + fuelType + " Cars ---");
        results.forEach(System.out::println);
    }

    // ── FILTER BY PRICE RANGE ───────────────────────────────────
    public void filterByPriceRange(double min, double max) {
        if (min < 0 || max < min)
            throw new IllegalArgumentException("Invalid price range.");
        List<Car> results = getAllCars().stream()
            .filter(c -> c.getPrice() >= min && c.getPrice() <= max)
            .collect(Collectors.toList());
        if (results.isEmpty()) { System.out.println("No cars in this price range."); return; }
        System.out.printf("%n--- Cars between Rs.%.0f and Rs.%.0f ---%n", min, max);
        results.forEach(System.out::println);
    }

    // ── SORT BY PRICE ───────────────────────────────────────────
    public void sortByPrice() {
        List<Car> sorted = getAllCars().stream()
            .sorted(Comparator.comparingDouble(Car::getPrice))
            .collect(Collectors.toList());
        System.out.println("\n--- Cars Sorted by Price (Low to High) ---");
        sorted.forEach(System.out::println);
    }

    // ── SORT BY YEAR ────────────────────────────────────────────
    public void sortByYear() {
        List<Car> sorted = getAllCars().stream()
            .sorted(Comparator.comparingInt(Car::getYear).reversed())
            .collect(Collectors.toList());
        System.out.println("\n--- Cars Sorted by Year (Newest First) ---");
        sorted.forEach(System.out::println);
    }

    // ── SALES REPORT ────────────────────────────────────────────
    public void generateSalesReport() {
        List<Car> all    = getAllCars();
        long booked      = all.stream().filter(c -> c.getStatus() == CarStatus.BOOKED).count();
        long available   = all.stream().filter(c -> c.getStatus() == CarStatus.AVAILABLE).count();
        double revenue   = all.stream()
                              .filter(c -> c.getStatus() == CarStatus.BOOKED)
                              .mapToDouble(Car::getPrice).sum();
        System.out.println("\n====== SALES REPORT ======");
        System.out.println("Total Cars    : " + all.size());
        System.out.println("Available     : " + available);
        System.out.println("Booked        : " + booked);
        System.out.printf( "Total Revenue : Rs.%.2f%n", revenue);
        System.out.println("Report Date   : " +
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
        System.out.println("==========================");
    }
}
