package dao;

import model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    private MyDBConnection dbConnection;

    public TransactionDAO() {
        dbConnection = MyDBConnection.getInstance();
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        Connection connection = dbConnection.getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM transaction");
            while (resultSet.next()) {
                int id = resultSet.getInt("id_transaction");
                int action = resultSet.getInt("action");
                int bikeId = resultSet.getInt("id_bike");
                int personId = resultSet.getInt("id_person");
                LocalDate date = resultSet.getDate("date").toLocalDate();

                Transaction transaction = new Transaction(id, action, bikeId, personId, date);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving transactions from the database: " + e.getMessage());
        }
        return transactions;
    }

    public void addTransaction(int action, int bikeId, int personId, LocalDate date) {
        Connection connection = dbConnection.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO transaction (action, id_bike, id_person, date) VALUES (?, ?, ?, ?)"
            );

            preparedStatement.setInt(1, action);
            preparedStatement.setInt(2, bikeId);
            preparedStatement.setInt(3, personId);
            preparedStatement.setDate(4, Date.valueOf(date));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding transaction to the database: " + e.getMessage());
        }
    }


}
