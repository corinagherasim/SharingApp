package dao;

import model.Availability;
import model.Bike;
import model.BikeCategory;
import model.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BikeDAO {
    private MyDBConnection dbConnection;

    public BikeDAO() {
        dbConnection = MyDBConnection.getInstance();
    }

    public List<Bike> getBikesFromDB() {
        Connection connection = dbConnection.getConnection();
        List<Bike> bikes = new ArrayList<>(); // poti sa faci o lista de obiecte de tipul clasei tale
        try{
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM bike");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id_bike");
                String model = rs.getString("model");
                BikeCategory category = BikeCategory.valueOf(rs.getString("category"));
                Availability availability = Availability.valueOf(rs.getString("availability"));

                Bike bike = new Bike(id, model, category, availability);
                bikes.add(bike);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bikes;

    }

    public void addBikeToDB(Bike bike) {
        Connection connection = dbConnection.getConnection();
        try {
            // Prepare the INSERT statement
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO bike (model, category) VALUES (?, ?)"
            );

            // Set the values for the placeholders
            preparedStatement.setString(1, bike.getModel());
            preparedStatement.setString(2, bike.getCategory().name());

            // Execute the INSERT statement
            preparedStatement.executeUpdate();

            System.out.println("User added to the database.");
        } catch (SQLException e) {
            System.err.println("Error adding user to the database: " + e.getMessage());
        }
    }

    public void removeBikeFromDB(int bikeId) {
        Connection connection = dbConnection.getConnection();
        try {
            // Prepare the DELETE statement
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM bike WHERE id_bike = ?"
            );

            // Set the value for the placeholder
            preparedStatement.setInt(1, bikeId);

            // Execute the DELETE statement
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Bike removed from the database with ID: " + bikeId);
            } else {
                System.out.println("No bike found in the database with ID: " + bikeId);
            }
        } catch (SQLException e) {
            System.err.println("Error removing bike from the database: " + e.getMessage());
        }
    }

    public Bike getBikeById(int bikeId) {
        Connection connection = dbConnection.getConnection();
        Bike foundBike = null;
        try {
            // Prepare the SELECT statement
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM bike WHERE id_bike = ?");

            // Set the value for the placeholder
            ps.setInt(1, bikeId);

            // Execute the SELECT statement
            ResultSet rs = ps.executeQuery();

            // Check if a bike with the given ID exists
            if (rs.next()) {
                // Create a new Bike object and populate it with data from the ResultSet
                int id = rs.getInt("id_bike");
                String model = rs.getString("model");
                BikeCategory category = BikeCategory.valueOf(rs.getString("category"));
                Availability availability = Availability.valueOf(rs.getString("availability"));

                foundBike = new Bike(id, model, category, availability);
                // Set other properties as needed
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving bike from the database: " + e.getMessage());
        }
        return foundBike;
    }





}
