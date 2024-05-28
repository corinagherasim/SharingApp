package dao;

import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonDAO {
    private MyDBConnection dbConnection;

    public PersonDAO() {
        dbConnection = MyDBConnection.getInstance();
    }

    public List<Person> getPeopleFromDB() {
        Connection connection = dbConnection.getConnection();
        List<Person> people = new ArrayList<>(); // poti sa faci o lista de obiecte de tipul clasei tale
        try{
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM person");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id_person");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String role = rs.getString("role");

                Person person;
                if ("ADMIN".equalsIgnoreCase(role)) {
                    person = new Admin(id, name, email, password);  // Create an Admin object
                } else if ("USER".equalsIgnoreCase(role)) {
                    person = new User(id, name, email, password);  // Create a User object
                } else {
                    continue;  // Skip any unknown roles
                }

                people.add(person);
                people.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return people;

    }

    public void addUserToDB(Person person) {
        Connection connection = dbConnection.getConnection();
        try {
            // Prepare the INSERT statement
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO person (name, email, password, role) VALUES (?, ?, ?, ?)"
            );

            // Set the values for the placeholders
            preparedStatement.setString(1, person.getName());
            preparedStatement.setString(2, person.getEmail());
            preparedStatement.setString(3, person.getPassword());
            preparedStatement.setString(4, person.getRole());

            // Execute the INSERT statement
            preparedStatement.executeUpdate();

            System.out.println("User added to the database.");
        } catch (SQLException e) {
            System.err.println("Error adding user to the database: " + e.getMessage());
        }
    }

    public void removePersonFromDB(int personId) {
        Connection connection = dbConnection.getConnection();
        try {
            // Prepare the DELETE statement
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM person WHERE id_person = ?"
            );

            // Set the value for the placeholder
            preparedStatement.setInt(1, personId);

            // Execute the DELETE statement
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Person removed from the database with ID: " + personId);
            } else {
                System.out.println("No person found in the database with ID: " + personId);
            }
        } catch (SQLException e) {
            System.err.println("Error removing person from the database: " + e.getMessage());
        }
    }

    public Person getPersonById(int personId) {
        Connection connection = dbConnection.getConnection();
        Person foundperson = null;
        try {
            // Prepare the SELECT statement
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM bike WHERE id_person = ?");

            // Set the value for the placeholder
            ps.setInt(1, personId);

            // Execute the SELECT statement
            ResultSet rs = ps.executeQuery();

            // Check if a bike with the given ID exists
            if (rs.next()) {
                // Create a new Person object and populate it with data from the ResultSet
                int id = rs.getInt("id_person");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String role = rs.getString("role");

                foundperson = new Person(id, name, email, password, role);
                // Set other properties as needed
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving person from the database: " + e.getMessage());
        }
        return foundperson;
    }



}
