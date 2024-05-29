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

    public void deleteTransaction(int action, int bikeId, int personId, LocalDate date) {
        Connection connection = dbConnection.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM transaction WHERE action = ? AND id_person = ?  AND id_bike = ? AND date = ?"
            );

            preparedStatement.setInt(1, action);
            preparedStatement.setInt(2, personId);
            preparedStatement.setInt(3, bikeId);
            preparedStatement.setDate(4, Date.valueOf(date));

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Transaction deleted successfully.");
            } else {
                System.out.println("No transaction found.");
            }
        } catch (SQLException e) {
            System.err.println("Error deleting transaction from the database: " + e.getMessage());
        }
    }

    public List<Transaction> getInfoHistory() {
        List<Transaction> transactions = new ArrayList<>();
        Connection connection = dbConnection.getConnection();
        try {
            String query = "SELECT t.id_transaction, t.action, t.id_bike, t.id_person, t.date, p.name " +
                    "FROM transaction t " +
                    "JOIN person p ON t.id_person = p.id_person";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id_transaction");
                int action = resultSet.getInt("action");
                int bikeId = resultSet.getInt("id_bike");
                int personId = resultSet.getInt("id_person");
                LocalDate date = resultSet.getDate("date").toLocalDate();
                String personName = resultSet.getString("name");

                Transaction transaction = new Transaction(id, action, bikeId, personId, date);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving transactions from the database: " + e.getMessage());
        }
        return transactions;
    }

    public List<String> generateHistory() {
        List<Transaction> transactions = getInfoHistory();
        List<String> reportList = new ArrayList<>();

        Shop shop = Shop.getInstance();
        shop.addPeopleFromDatabase();

        for (Transaction transaction : transactions) {
            String actionName;
            switch (transaction.getAction()) {
                case 0: actionName = "Return"; break;
                case 1: actionName = "Borrow"; break;
                case 2: actionName = "Reserve"; break;
                default: actionName = "Unknown"; break;
            }

            String personName = shop.searchUserById(transaction.getPersonId()).getName();

            String transactionInfo = "Action: " + actionName + "\n" +
                    ", Person: " + personName + "\n" +
                    ", Bike ID: " + transaction.getBikeId() + "\n" +
                    ", Date: " + transaction.getDate() + "\n";
            reportList.add(transactionInfo);
        }

        if (reportList.isEmpty()) {
            reportList.add("No transactions found.");
        }

        return reportList;
    }


}
