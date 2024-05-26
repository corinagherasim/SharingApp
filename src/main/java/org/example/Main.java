package org.example;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Shop shop = Shop.getInstance();

        // Create Admin and User
        Admin admin = new Admin(1, "Admin Name", "admin@example.com", "adminpass");
        User user = new User(2, "User Name", "user@example.com", "userpass");

        shop.addAdmin(admin);
        shop.addUser(user);
        shop.displayAllUsers(admin);

        // Create a Bike
        Bike bike1 = new Bike(1, "Model X", BikeCategory.MAN);
        Bike bike2 = new Bike(2, "Model y", BikeCategory.WOMAN);
        Bike bike3 = new Bike(3, "Model z", BikeCategory.KIDS);

        // Add the bike to the shop by admin
        shop.addBike(admin, bike1);
        shop.addBike(admin, bike2);
        shop.addBike(admin, bike3);
        // Check bike availability
        shop.checkAvailability(bike1);

        // Borrow the bike by user
        boolean borrowed = shop.borrowBike(bike1, user, LocalDate.now());
        if (borrowed) {
            System.out.println("Bike borrowed successfully.");
        } else {
            System.out.println("Bike could not be borrowed.");
        }

        // Check bike availability after borrowing
        shop.checkAvailability(bike1);

        // Return the bike
        shop.returnBike(bike1);

        // Check bike availability after returning
        shop.checkAvailability(bike1);

        // Reserve the bike
        boolean reserved = shop.reserveBike(bike1, user, LocalDate.now());
        if (reserved) {
            System.out.println("Bike reserved successfully.");
        } else {
            System.out.println("Bike could not be reserved.");
        }

        // Check bike availability after reserving
        shop.checkAvailability(bike1);

        shop.displayAllBikes();
    }
}
