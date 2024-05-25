package org.example;

public class User extends Person {
    private Bike currentBike;

    public User(int id, String name, String email, String password) {
        super(id, name, email, password, "USER");
        this.currentBike = null;
    }
    public Bike getCurrentBike() {return currentBike;}
    public void setCurrentBike(Bike currentBike) {this.currentBike = currentBike;}

}


