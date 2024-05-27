package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MyDBConnection {

    private static MyDBConnection instance;
    private Connection connection;

    private MyDBConnection() {
        Properties props = new Properties();
        try {
            props.load(MyDBConnection.class.getClassLoader().getResourceAsStream("dbproperties"));
            String url = props.getProperty("url");
            String username = props.getProperty("username");
            String password = props.getProperty("password");
            connection = DriverManager.getConnection(url, username, password);
        } catch ( IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized MyDBConnection getInstance() {
        if (instance == null) {
            instance = new MyDBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
