package model;

import dao.BikeDAO;
import dao.UsersDAO;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Database connection test");
        
        UsersDAO usersDAO = new UsersDAO();

        List<SimpleUser> users = usersDAO.getUsers();

        for ( SimpleUser user : users){
            System.out.println(user);
        }

        BikeDAO bikeDAO = new BikeDAO();
        bikeDAO.getBikeById(1);

        List<Bike> bikes = bikeDAO.getBikesFromDB();

        for ( Bike bike : bikes){
            System.out.println(bike);
        }

        Shop shop = Shop.getInstance();

        shop.addBikesFromDatabase();
        shop.displayAllBikes();

//        Admin admin = new Admin(1, "Admin Name", "admin@shop.com", "adminpass");
//        Admin admin2 = new Admin(1, "Admin Name", "admin@dbth.com", "adminpass");
//        User user = new User(2, "User Name", "user@example.com", "userpass");
//        User user2 = new User(2, "User Name", "user", "userpass");
//
//        shop.addPerson(admin);
//        shop.addPerson(user);
//        shop.addPerson(admin2);
//        shop.addPerson(user2);

//        shop.displayAllPeople(admin);

        Bike bike = new Bike(2, "fsbd", BikeCategory.KID, Availability.AVAILABLE);
//        shop.addBike(admin,bike);
        shop.addPeopleFromDatabase();
        shop.processTransactionsFromDatabase();

    }
}
