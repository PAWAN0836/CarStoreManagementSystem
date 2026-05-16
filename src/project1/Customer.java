package project1;

public class Customer {
    private String name;
    private String phone;
    private String email;

    public Customer(String name, String phone, String email) {
        // INPUT VALIDATION — exception handling
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Customer name cannot be empty.");
        if (phone == null || !phone.matches("\\d{10}"))
            throw new IllegalArgumentException("Phone must be exactly 10 digits.");
        if (email == null || !email.contains("@") || !email.contains("."))
            throw new IllegalArgumentException("Invalid email address.");

        this.name  = name.trim();
        this.phone = phone.trim();
        this.email = email.trim().toLowerCase();  // String handling
    }

    public String getName()  { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return "Customer: " + name + " | Phone: " + phone + " | Email: " + email;
    }
}
