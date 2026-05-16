package project1;

import java.util.Scanner;

public class Carstoreapp {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    public static void main(String[] args) {
        Scanner scanner  = new Scanner(System.in);
        Carstore store   = new Carstore();

        System.out.println("╔══════════════════════════╗");
        System.out.println("║   Welcome to Car Store   ║");
        System.out.println("╚══════════════════════════╝");

        boolean running = true;
        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1.  View All Cars");
            System.out.println("2.  Book a Car");
            System.out.println("3.  Add Review");
            System.out.println("4.  Search by Brand");
            System.out.println("5.  Filter by Fuel Type");
            System.out.println("6.  Filter by Price Range");
            System.out.println("7.  Sort by Price");
            System.out.println("8.  Sort by Year");
            System.out.println("9.  Admin Login");
            System.out.println("10. Exit");
            System.out.print("Choose: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim()); // safe input

                switch (choice) {
                    case 1  -> store.displayCars();
                    case 2  -> bookCar(store, scanner);
                    case 3  -> addReview(store, scanner);
                    case 4  -> {
                        System.out.print("Enter brand keyword: ");
                        store.searchByBrand(scanner.nextLine());
                    }
                    case 5  -> {
                        System.out.print("Fuel type (Petrol/Diesel/Electric): ");
                        store.filterByFuelType(scanner.nextLine());
                    }
                    case 6  -> filterByPrice(store, scanner);
                    case 7  -> store.sortByPrice();
                    case 8  -> store.sortByYear();
                    case 9  -> adminPanel(store, scanner);
                    case 10 -> { running = false; System.out.println("Goodbye!"); }
                    default -> System.out.println("Invalid option. Choose 1-10.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (IllegalArgumentException e) {
                System.out.println("Input Error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    // ── BOOK CAR ────────────────────────────────────────────────
    private static void bookCar(Carstore store, Scanner scanner) {
        try {
            System.out.print("Model to book : "); String model = scanner.nextLine();
            System.out.print("Your name     : "); String name  = scanner.nextLine();
            System.out.print("Phone (10 dig): "); String phone = scanner.nextLine();
            System.out.print("Email         : "); String email = scanner.nextLine();
            Customer c = new Customer(name, phone, email); // validation inside constructor
            store.bookCar(model, c);
        } catch (IllegalArgumentException e) {
            System.out.println("Validation Error: " + e.getMessage());
        }
    }

    // ── ADD REVIEW ──────────────────────────────────────────────
    private static void addReview(Carstore store, Scanner scanner) {
        try {
            System.out.print("Model  : "); String model  = scanner.nextLine();
            System.out.print("Review : "); String review = scanner.nextLine();
            store.addReview(model, review);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ── FILTER BY PRICE ─────────────────────────────────────────
    private static void filterByPrice(Carstore store, Scanner scanner) {
        try {
            System.out.print("Min price: "); double min = Double.parseDouble(scanner.nextLine());
            System.out.print("Max price: "); double max = Double.parseDouble(scanner.nextLine());
            store.filterByPriceRange(min, max);
        } catch (NumberFormatException e) {
            System.out.println("Enter valid price numbers.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ── ADMIN PANEL ─────────────────────────────────────────────
    private static void adminPanel(Carstore store, Scanner scanner) {
        System.out.print("Username: "); String user = scanner.nextLine();
        System.out.print("Password: "); String pass = scanner.nextLine();

        if (!user.equals(ADMIN_USERNAME) || !pass.equals(ADMIN_PASSWORD)) {
            System.out.println("Invalid credentials."); return;
        }

        System.out.println("Admin Login Successful!");
        boolean adminRunning = true;

        while (adminRunning) {
            System.out.println("\n--- ADMIN PANEL ---");
            System.out.println("1. Add Car");
            System.out.println("2. Remove Car");
            System.out.println("3. View All Cars");
            System.out.println("4. Sales Report");
            System.out.println("5. Logout");
            System.out.print("Choose: ");

            try {
                int ch = Integer.parseInt(scanner.nextLine().trim());
                switch (ch) {
                    case 1 -> addCar(store, scanner);
                    case 2 -> {
                        System.out.print("Model to remove: ");
                        store.removeCar(scanner.nextLine());
                    }
                    case 3 -> store.displayCars();
                    case 4 -> store.generateSalesReport();
                    case 5 -> adminRunning = false;
                    default -> System.out.println("Invalid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ── ADD CAR (ADMIN) ─────────────────────────────────────────
    private static void addCar(Carstore store, Scanner scanner) {
        try {
            System.out.print("Model     : "); String model    = scanner.nextLine();
            System.out.print("Brand     : "); String brand    = scanner.nextLine();
            System.out.print("Price     : "); double price    = Double.parseDouble(scanner.nextLine());
            System.out.print("Fuel Type : "); String fuelType = scanner.nextLine();
            System.out.print("Year      : "); int year        = Integer.parseInt(scanner.nextLine());
            store.addCar(model, brand, price, fuelType, year);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format for price or year.");
        } catch (IllegalArgumentException e) {
            System.out.println("Validation Error: " + e.getMessage());
        }
    }
}
