package org.example;
import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Shop implements Rentable{
    private Map<BikeCategory, Section> sections; // Map of section name to Section object
    private List<Bike> bikes; // List of all bikes in the shop
    private Map<Bike, LocalDate> borrowedBikes;
    private List<User> users;
    private List<Admin> admins;
    private List<Person> people;

    //    Singleton
    private static final Shop shop = new Shop();

    public static Shop getInstance() {
        return shop;
    }

    private Shop() {
        this.sections = new HashMap<>();
        this.bikes = new ArrayList<>();
        this.users = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.people = new ArrayList<>();
        this.borrowedBikes = new HashMap<>();
    }

    public Map<BikeCategory, Section> getSections() {return sections;}

    // Method to add a bike (only accessible by admin)
    public void addBike(Admin admin, Bike bike) {
        if (isAdmin(admin)) {
            BikeCategory category = bike.getCategory();
            bikes.add(bike);
            Section section = sections.getOrDefault(category, new Section());
            section.addBike(bike);
            sections.put(category, section);
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
                    for (Section section : sections.values()) {
                        if (section.getBikes().contains(bike)) {
                            section.getBikes().remove(bike);
                            break;
                        }
                    }
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

    public void addUser(User user) {
        users.add(user);
    }

    public void displayAllUsers(Admin admin) {
        if (isAdmin(admin)) {
            System.out.println("All Users:");
            for (User user : users) {
                System.out.println(user.getName());
            }

            System.out.println("All Admins:");
            for (User user : users) {
                System.out.println(user.getName());
            }
        } else {
            System.out.println("Only an admin can view users and admins.");
        }
    }

    public void displayAllBikes() {
        System.out.println("All Bikes in the Shop:");

        // Iterate through each bike category (section)
        for (BikeCategory category : sections.keySet()) {
            Section section = sections.get(category);

            // Check if the section is empty
            if (!section.getBikes().isEmpty()) {
                // Print section name (category)
                System.out.println("Category: " + category);

                // Iterate through each bike in the section
                for (Bike bike : section.getBikes()) {
                    System.out.print("ID: " + bike.getId() + ", Model: " + bike.getModel() + ", Availability: " + bike.getAvailability());

                    // Check if the bike is borrowed
                    if (bike.getAvailability() == Availability.BORROWED) {
                        System.out.println(", Borrowed by: " + bike.getBorrower().getName() + ", Borrowed Date: " + bike.getBorrowDate());
                    } else if (bike.getAvailability() == Availability.RESERVED) {
                        System.out.println(", Reserved by: " + bike.getReserver().getName() + ", Reservation Date: " + bike.getReservationDate());
                    } else {
                        System.out.println();
                    }
                }
            }
        }
    }

    @Override
    // Method to borrow a bike
    public boolean borrowBike(Bike bike, User borrower, LocalDate borrowDate) {
        boolean borrowedSuccessfully = false;
        if (bike.getAvailability() == Availability.AVAILABLE) {
            bike.setAvailability(Availability.BORROWED);
            bike.setBorrower(borrower);
            bike.setBorrowDate(borrowDate);
            borrower.borrowBike(bike);
            borrowedBikes.put(bike, borrowDate);
            borrowedSuccessfully = true;
            System.out.println("Bike '" + bike.getModel() + "' borrowed by " + borrower.getName() + " on " + borrowDate);
        } else if (bike.getAvailability() == Availability.RESERVED) {
            if (bike.getReserver() != null && bike.getReserver().equals(borrower)) {
                bike.setAvailability(Availability.BORROWED);
                bike.setBorrower(borrower);
                bike.setBorrowDate(borrowDate);
                borrower.borrowBike(bike);
                borrowedBikes.put(bike, borrowDate);
                borrowedSuccessfully = true;
                System.out.println("Bike '" + bike.getModel() + "' borrowed by " + borrower.getName() + " on " + borrowDate);
            } else {
                LocalDate reservationDate = bike.getReservationDate();
                if (ChronoUnit.DAYS.between(reservationDate, LocalDate.now()) > 30) {
                    bike.setAvailability(Availability.BORROWED);
                    bike.setBorrower(borrower);
                    bike.setBorrowDate(borrowDate);
                    borrower.borrowBike(bike);
                    borrowedBikes.put(bike, borrowDate);
                    borrowedSuccessfully = true;
                    System.out.println("Bike '" + bike.getModel() + "' borrowed by " + borrower.getName() + " on " + borrowDate);
                } else {
                    System.out.println("Bike '" + bike.getModel() + "' is reserved by someone else and cannot be borrowed by " + borrower.getName() + ".");
                }
            }
        } else {
            System.out.println("Bike '" + bike.getModel() + "' is not available for borrowing by " + borrower.getName() + ".");
        }
        return borrowedSuccessfully;
    }

    // Check bike availability
    public void checkAvailability(Bike bike) {
        boolean found = false;
        for (Bike b : bikes) {
            if (b.equals(bike)) {
                found = true;
                if (b.getAvailability() == Availability.AVAILABLE) {
                    System.out.println("Bike '" + bike.getModel() + "' is available.");
                } else if (b.getAvailability() == Availability.RESERVED) {
                    LocalDate reservationDate = b.getReservationDate();
                    if (ChronoUnit.DAYS.between(reservationDate, LocalDate.now()) > 30) {
                        b.setAvailability(Availability.AVAILABLE);
                        System.out.println("Bike '" + bike.getModel() + "' is available now.");
                    } else {
                        System.out.println("Bike '" + bike.getModel() + "' is not available. Status: " + b.getAvailability());
                    }
                } else {
                    System.out.println("Bike '" + bike.getModel() + "' is not available. Status: " + b.getAvailability());
                }
                break;
            }
        }
        if (!found) {
            System.out.println("Bike not found in the shop.");
        }
    }

    @Override
    // Return a bike
    public void returnBike(Bike bike) {
        if (bike.getAvailability() == Availability.BORROWED) {
            bike.setAvailability(Availability.AVAILABLE);
            User borrower = bike.getBorrower();
            if (borrower != null) {
                borrower.returnBike(bike);
            } else {
                System.out.println("Error: The bike has no associated borrower.");
            }
            borrowedBikes.remove(bike);
            System.out.println("Bike '" + bike.getModel() + "' has been returned.");
        } else {
            System.out.println("This bike is not borrowed and cannot be returned.");
        }
    }

    @Override
    // Reserve a bike
    public boolean reserveBike(Bike bike, User user, LocalDate reserveDate) {
        boolean reservationSuccessful = false;
        if (bike.getAvailability() == Availability.AVAILABLE) {
            bike.setAvailability(Availability.RESERVED);
            bike.setReservationDate(reserveDate);
            bike.setReserver(user);
            reservationSuccessful = true;
            System.out.println("Bike '" + bike.getModel() + "' has been reserved by " + user.getName() + " on " + reserveDate);
        } else if (bike.getAvailability() == Availability.RESERVED) {
            LocalDate reservationDate = bike.getReservationDate();
            if (ChronoUnit.DAYS.between(reservationDate, LocalDate.now()) > 30) {
                bike.setAvailability(Availability.RESERVED);
                bike.setReservationDate(reserveDate);
                bike.setReserver(user);
                reservationSuccessful = true;
                System.out.println("Bike '" + bike.getModel() + "' has been reserved by " + user.getName() + " on " + reserveDate);
            } else {
                System.out.println("Sorry, the bike '" + bike.getModel() + "' is already reserved by someone else and cannot be reserved by " + user.getName() + ".");
            }
        } else {
            System.out.println("Sorry, the bike '" + bike.getModel() + "' is not available for reservation.");
        }
        return reservationSuccessful;
    }

}


