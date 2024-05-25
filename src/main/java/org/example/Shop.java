package org.example;
import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Shop {
    private List<Bike> bikes; // List of all bikes in the shop
    private Map<Bike, LocalDate> borrowedBikes;
    private List<User> users;

    //    Singleton
    private static final Shop shop = new Shop();

    public static Shop getInstance() {
        return shop;
    }

    private Shop() {
        this.bikes = new ArrayList<>();
        this.users = new ArrayList<>();
        this.borrowedBikes = new HashMap<>();
    }
}

