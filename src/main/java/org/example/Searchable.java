package org.example;

import java.util.List;

public interface Searchable {
    List<Bike> searchById(int id) throws BikeNotFoundException;

    List<Bike> searchByModel(String model) throws BikeNotFoundException;
}
