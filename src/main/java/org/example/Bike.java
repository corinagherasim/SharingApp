package org.example;

import java.time.LocalDate;

public class Bike {
    private int id;
    private String model;
    private BikeCategory category;
    private Availability availability;
    private User borrower;
    private User reserver;
    private LocalDate reservationDate;
    private LocalDate borrowDate;

    public Bike(int id, String model, BikeCategory category){
        this.id = id;
        this.model = model;
        this.category = category;
        this.availability = Availability.AVAILABLE;
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
    public LocalDate getReservationDate() {return reservationDate;}
    public void setReservationDate(LocalDate reservationDate) {this.reservationDate = reservationDate;}
    public LocalDate getBorrowDate() {return borrowDate;}
    public void setBorrowDate(LocalDate borrowDate) {this.borrowDate = borrowDate;}
}

