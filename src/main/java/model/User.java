package model;

import java.util.ArrayList;
import java.util.List;

public class User extends Person {
    private List<Bike> borrowedBikes;

    public User(int id, String name, String email, String password) {
        super(id, name, email, password, "USER");
        this.borrowedBikes = new ArrayList<>();
    }

    public List<Bike> getBorrowedBikes() {
        return borrowedBikes;
    }

    public void setBorrowedBikes(List<Bike> borrowedBikes) {
        this.borrowedBikes = borrowedBikes;
    }

    public void borrowBike(Bike bike) {
        borrowedBikes.add(bike);
    }

    public void returnBike(Bike bike) {
        borrowedBikes.remove(bike);
    }

    @Override
    public boolean validateEmail(String email) {

        return email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");
    }

}


