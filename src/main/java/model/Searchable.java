package model;

import exceptions.BikeNotFoundException;
import exceptions.UserNotFoundException;

import java.util.List;

public interface Searchable {
    Bike searchBikeById(int id);

    Bike searchBikeByModel(String model);
    User searchUserById(int id);
}
