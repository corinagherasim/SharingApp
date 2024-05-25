package org.example;

public class Main {
    public static void main(String[] args) {
        Shop shop = Shop.getInstance();
        // Create an Admin
        Admin admin = new Admin(1, "Admin Name", "admin@example.com", "password123");

        // Add the Admin to the Shop
        shop.addAdmin(admin);

        // Create a Bike
        Bike bike1 = new Bike(1, "Model X", BikeCategory.MAN);

        // Add the Bike to the Shop
        shop.addBike(admin, bike1);

        // Remove the Bike from the Shop
        shop.removeBike(admin, 1);
    }
}