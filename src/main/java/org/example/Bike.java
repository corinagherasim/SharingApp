package org.example;

public class Bike {
    private int id;
    private String model;
    private BikeCategory category;
    private Availability availability;
    private User borrower;
    private User reserver;

    public Bike(int id, String model, Availability availability, BikeCategory category){
        this.id = id;
        this.model = model;
        this.availability = Availability.AVAILABLE;
        this.category = BikeCategory.MAN;
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getModel() {return model;}

    public void setModel(String model) {this.model = model;}

    public Availability getAvailability() {return availability;}

    public void setAvailability(Availability availability) {this.availability = availability;}

    public User getBorrower() {return borrower;}

    public void setBorrower(User borrower) {this.borrower = borrower;}

    public User getReserver() {return reserver;}

    public void setReserver(User reserver) {this.reserver = reserver;}

    public BikeCategory getCategory() {return category;}

    public void setCategory(BikeCategory category) {this.category = category;}
}

