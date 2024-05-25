package org.example;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private Bike currentBike;

    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.currentBike = null;
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public Bike getCurrentBike() {return currentBike;}

    public void setCurrentBike(Bike currentBike) {this.currentBike = currentBike;}

}


