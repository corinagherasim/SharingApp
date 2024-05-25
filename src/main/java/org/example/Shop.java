package org.example;
import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Shop {
    private List<Bike> bikes; // List of all bikes in the shop
    private Map<Bike, LocalDate> borrowedBikes;
    private List<User> users;
    private List<Admin> admins;

    //    Singleton
    private static final Shop shop = new Shop();

    public static Shop getInstance() {
        return shop;
    }

    private Shop() {
        this.bikes = new ArrayList<>();
        this.users = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.borrowedBikes = new HashMap<>();
    }

    // Method to add a bike (only accessible by admin)
    public void addBike(Admin admin, Bike bike) {
        if (isAdmin(admin)) {
            bikes.add(bike);
            System.out.println("Bike added by admin: " + bike.getModel());
        } else {
            System.out.println("Only an admin can add bikes.");
        }
    }

    public void removeBike(Admin admin, int bikeId) {
        if (isAdmin(admin)) {
            Iterator<Bike> iterator = bikes.iterator();
            boolean removed = false;
            while (iterator.hasNext()) {
                Bike bike = iterator.next();
                if (bike.getId() == bikeId) {
                    iterator.remove();
                    removed = true;
                }
            }
            if (removed) {
                System.out.println("Bike removed by admin with ID: " + bikeId);
            } else {
                System.out.println("No bike found with ID: " + bikeId);
            }
        } else {
            System.out.println("Only an admin can remove bikes.");
        }
    }

    private boolean isAdmin(Admin admin) {
        return admins.contains(admin);
    }

    public void addAdmin(Admin admin) {
        admins.add(admin);
    }

}

