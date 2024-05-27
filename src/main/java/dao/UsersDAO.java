package dao;

import model.SimpleUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersDAO {

    private MyDBConnection dbConnection;

    public UsersDAO() {
       dbConnection = MyDBConnection.getInstance();
    }

    // clasa din care iei date din baza de date si le pui intr-o lista de obiecte
    public List<SimpleUser> getUsers() {
        Connection connection = dbConnection.getConnection();
        List<SimpleUser> data = new ArrayList<>(); // poti sa faci o lista de obiecte de tipul clasei tale 
        try{
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Users");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                SimpleUser clasa = new SimpleUser();
                clasa.setId(rs.getInt("id"));
                clasa.setEmail(rs.getString("email"));
                clasa.setPassword(rs.getString("password"));
                clasa.setUsername(rs.getString("username"));
                data.add(clasa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;

    }
}
