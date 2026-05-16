package project1;

// CONCEPT — Enum for car status (better than boolean)
enum CarStatus { AVAILABLE, BOOKED, SOLD }

public class Car {
    private int       id;
    private String    model;
    private String    brand;
    private double    price;
    private String    fuelType;
    private int       year;           // NEW — manufacture year
    private CarStatus status;         // NEW — enum instead of boolean
    private String    bookedBy;
    private String    review;
    private String    bookingDate;    // NEW — date & time of booking

    // Constructor
    public Car(int id, String model, String brand, double price,
               String fuelType, int year, CarStatus status,
               String bookedBy, String review, String bookingDate) {
        this.id          = id;
        this.model       = model.trim();
        this.brand       = brand.trim();
        this.price       = price;
        this.fuelType    = fuelType.trim();
        this.year        = year;
        this.status      = status;
        this.bookedBy    = bookedBy;
        this.review      = review;
        this.bookingDate = bookingDate;
    }

    // Getters
    public int       getId()          { return id; }
    public String    getModel()       { return model; }
    public String    getBrand()       { return brand; }
    public double    getPrice()       { return price; }
    public String    getFuelType()    { return fuelType; }
    public int       getYear()        { return year; }
    public CarStatus getStatus()      { return status; }
    public String    getBookedBy()    { return bookedBy; }
    public String    getReview()      { return review; }
    public String    getBookingDate() { return bookingDate; }

    // Setters for mutable fields
    public void setStatus(CarStatus status)       { this.status      = status; }
    public void setBookedBy(String bookedBy)      { this.bookedBy    = bookedBy; }
    public void setReview(String review)          { this.review      = review; }
    public void setBookingDate(String date)       { this.bookingDate = date; }

    @Override
    public String toString() {
        return String.format("[%d] %s %s (%d) | Rs.%.2f | %s | Status: %s | Review: %s",
            id, brand, model, year, price, fuelType, status,
            (review != null && !review.isEmpty() ? review : "No review yet"));
    }
}
