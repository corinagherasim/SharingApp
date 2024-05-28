package model;

import dao.BikeDAO;
import dao.PersonDAO;
import dao.TransactionDAO;
import exceptions.BikeNotFoundException;

import java.util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Shop implements Rentable, Searchable{
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

    Shop() {
        this.sections = new HashMap<>();
        this.bikes = new ArrayList<>();
        this.users = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.people = new ArrayList<>();
        this.borrowedBikes = new HashMap<>();
    }

    public Map<BikeCategory, Section> getSections() {return sections;}

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Admin> admins) {
        this.admins = admins;
    }

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    public List<Bike> getBikes() {
        return bikes;
    }

    public void addBikesFromDatabase() {
        // Fetch bikes from the database using BikeDAO
        BikeDAO bikeDAO = new BikeDAO();
        List<Bike> bikes = bikeDAO.getBikesFromDB();

        // Add fetched bikes to the shop
        for (Bike bike : bikes) {
            addBikeFromDatabase(bike);
        }
    }

    private void addBikeFromDatabase(Bike bike) {
        // Get the category of the bike
        BikeCategory category = bike.getCategory();
        // Get or create the section corresponding to the bike's category
        Section section = sections.getOrDefault(category, new Section());
        section.addBike(bike);
        sections.put(category, section);
        bikes.add(bike);
    }

    public void addPeopleFromDatabase() {
        // Fetch people from the database using PersonDAO
        PersonDAO personDAO = new PersonDAO();
        List<Person> people = personDAO.getPeopleFromDB();

        // Add fetched bikes to the shop
        for (Person person : people) {
            addPersonFromDB(person);
        }
    }

    public void addPersonFromDB(Person person) {
        if (person instanceof Admin) {
            if (!admins.contains(person)) {
                admins.add((Admin) person);
            }
        } else if (person instanceof User) {
            if (!users.contains(person)) {
                users.add((User) person);
            }
        }
        if (!people.contains(person)) {
            people.add(person);
        }
    }

    public void addPerson(Person person) {
        PersonDAO personDAO = new PersonDAO();
        if (person instanceof Admin) {
            if(person.validateEmail(person.getEmail())){
                admins.add((Admin) person);
                people.add(person);
                personDAO.addUserToDB(person);
            } else{
                System.out.println("Invalid email");
            }

        } else if (person instanceof User) {
            if(person.validateEmail(person.getEmail())){
                users.add((User) person);
                people.add(person);
                personDAO.addUserToDB(person);
            } else{
                System.out.println("Invalid email");
            }
        }


    }


    // Method to add a bike (only accessible by admin)
    public void addBike(Admin admin, Bike bike) {
        if (isAdmin(admin)) {
            BikeCategory category = bike.getCategory();
            bikes.add(bike);
            Section section = sections.getOrDefault(category, new Section());
            section.addBike(bike);
            sections.put(category, section);
            BikeDAO bikeDAO = new BikeDAO();
            bikeDAO.addBikeToDB(bike);
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
                if (bike.getId() == bikeId && bike.getAvailability() == Availability.AVAILABLE) {
                    iterator.remove();
                    removed = true;
                    for (Section section : sections.values()) {
                        if (section.getBikes().contains(bike)) {
                            section.getBikes().remove(bike);
                            BikeDAO bikedao = new BikeDAO();
                            bikedao.removeBikeFromDB(bikeId);
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

    public void displayAllPeople(Admin adminControl) {
        if (isAdmin(adminControl)) {
            System.out.println("All Users:");
            for (User user : users) {
                System.out.println(user.getName());
            }

            System.out.println("All Admins:");
            for (Admin admin : admins) {
                System.out.println(admin.getName());
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
    public boolean borrowBike(Bike bike, User borrower, LocalDate borrowDate, boolean firstTime) {
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
                if (ChronoUnit.DAYS.between(reservationDate, LocalDate.now()) > 7) {
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
        if(borrowedSuccessfully){
            if (!firstTime){
                TransactionDAO transactionDAO = new TransactionDAO();
                transactionDAO.addTransaction(1, bike.getId(), borrower.getId(), LocalDate.now());
            }
        }
        return borrowedSuccessfully;
    }

    @Override
    // Return a bike
    public void returnBike(Bike bike, boolean firstTime) {
        if (bike.getAvailability() == Availability.BORROWED) {
            bike.setAvailability(Availability.AVAILABLE);
            User borrower = bike.getBorrower();
            if (borrower != null) {
                borrower.returnBike(bike);
            } else {
                System.out.println("Error: The bike has no associated borrower.");
            }
            borrowedBikes.remove(bike);
            if (!firstTime){
                TransactionDAO transactionDAO = new TransactionDAO();
                transactionDAO.addTransaction(0, bike.getId(), borrower.getId(), LocalDate.now());
            }
            System.out.println("Bike '" + bike.getModel() + "' has been returned.");
        } else {
            System.out.println("This bike is not borrowed and cannot be returned.");
        }
    }

    @Override
    // Reserve a bike
    public boolean reserveBike(Bike bike, User user, LocalDate reserveDate, boolean firstTime) {
        boolean reservationSuccessful = false;
        if (bike.getAvailability() == Availability.AVAILABLE) {
            bike.setAvailability(Availability.RESERVED);
            bike.setReservationDate(reserveDate);
            bike.setReserver(user);
            reservationSuccessful = true;
            System.out.println("Bike '" + bike.getModel() + "' has been reserved by " + user.getName() + " on " + reserveDate);
        } else if (bike.getAvailability() == Availability.RESERVED) {
            LocalDate reservationDate = bike.getReservationDate();
            if (ChronoUnit.DAYS.between(reservationDate, LocalDate.now()) > 7) {
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
        if(reservationSuccessful){
            if (!firstTime){
                TransactionDAO transactionDAO = new TransactionDAO();
                transactionDAO.addTransaction(2, bike.getId(), user.getId(), LocalDate.now());
            }
        }
        return reservationSuccessful;
    }

    public void processTransactionsFromDatabase() {
        TransactionDAO transactionDAO = new TransactionDAO();
        List<Transaction> transactions = transactionDAO.getAllTransactions();

        for (Transaction transaction : transactions) {
            switch (transaction.getAction()) {
                case 0:
                    Bike returnedBike = searchBikeById(transaction.getBikeId());
                    if (returnedBike != null) {
                        returnBike(returnedBike, true);
                    } else {
                        System.out.println("Bike not found in the shop.");
                    }
                    break;
                case 1:
                    Bike borrowedBike = searchBikeById(transaction.getBikeId());
                    User borrower = searchUserById(transaction.getPersonId());
                    if (borrowedBike != null && borrower != null) {
                        borrowBike(borrowedBike, borrower, transaction.getDate(), true);
                    } else {
                        System.out.println("Bike or user not found in the shop.");
                    }
                    break;
                case 2:
                    Bike reservedBike = searchBikeById(transaction.getBikeId());
                    User reserver = searchUserById(transaction.getPersonId());
                    if (reservedBike != null && reserver != null) {
                        reserveBike(reservedBike, reserver, transaction.getDate(), true);
                    } else {
                        System.out.println("Bike or user not found in the shop.");
                    }
                    break;
                default:
                    System.out.println("Invalid action in transaction.");
                    break;
            }
        }
    }

    @Override
    public Bike searchBikeById(int id){
        Bike foundBike = null;
        for (Bike bike : bikes) {
            if (bike.getId() == id) {
                foundBike = bike;
            }
        }
        return foundBike;
    }

    @Override
    public Bike searchBikeByModel(String model){
        Bike foundBike = null;
        for (Bike bike : bikes) {
            if (bike.getModel().equalsIgnoreCase(model)) {
                foundBike = bike;
            }
        }
        return foundBike;
    }

    @Override
    public User searchUserById(int id){
        User foundUser = null;
        for (User user : users) {
            if (user.getId() == id) {
                foundUser = user;
            }
        }
        return foundUser;
    }

    public Admin searchAdminByEmailPassword (String email, String password){
        Admin foundAdmin = null;
        for (Admin admin : admins) {
            if (admin.getEmail().equals(email) && admin.getPassword().equals(password)) {
                foundAdmin = admin;
            }
        }
        return foundAdmin;
    }

    public boolean isAdminFound(Admin foundAdmin){
        if (foundAdmin == null){
            return false;
        } else{
            return true;
        }
    }

    public User searchUserByEmailPassword (String email, String password){
        User foundUser = null;
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                foundUser = user;
            }
        }
        return foundUser;
    }

    public boolean isUserFound(User foundUser){
        if (foundUser == null){
            return false;
        } else{
            return true;
        }
    }

    public int getNextPersonId() {
        int maxId = 0;
        for (Person person : people) {
            if (person.getId() > maxId) {
                maxId = person.getId();
            }
        }
        return maxId + 1;
    }

    public int getNextBikeId() {
        int maxId = 0;
        for (Bike bike : bikes) {
            if (bike.getId() > maxId) {
                maxId = bike.getId();
            }
        }
        return maxId + 1;
    }

    public boolean searchAdminByEmail (String email){
        boolean found = false;
        for (Admin admin : admins) {
            if (admin.getEmail().equals(email)) {
                found = true;
            }
        }
        return found;
    }

    public boolean searchUserByEmail (String email){
        boolean found = false;
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                found = true;
            }
        }
        return found;
    }
}


