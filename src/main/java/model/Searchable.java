package model;

import exceptions.BikeNotFoundException;
import exceptions.UserNotFoundException;

import java.util.List;

public interface Searchable {
    Bike searchBikeById(int id);
    Bike searchBikeByModel(String model) throws BikeNotFoundException;
    User searchUserById(int id);
    Admin searchAdminByEmailPassword (String email, String password);
    User searchUserByEmailPassword (String email, String password);
    boolean searchAdminByEmail (String email);
    boolean searchUserByEmail (String email);

}
