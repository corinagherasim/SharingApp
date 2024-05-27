package model;

import java.time.LocalDate;

public class Transaction {
    private int id;
    private int action;
    private int bikeId;
    private int personId;
    private LocalDate date;

    public Transaction(int id, int action, int bikeId, int personId, LocalDate date) {
        this.id = id;
        this.action = action;
        this.bikeId = bikeId;
        this.personId = personId;
        this.date = date;
    }

    // Getters and setters for the properties
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getBikeId() {
        return bikeId;
    }

    public void setBikeId(int bikeId) {
        this.bikeId = bikeId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    // You can also override toString() method for easy debugging
    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", action=" + action +
                ", bikeId=" + bikeId +
                ", personId=" + personId +
                ", date=" + date +
                '}';
    }
}

