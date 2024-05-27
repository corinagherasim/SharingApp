package model;

import java.util.ArrayList;
import java.util.List;

class Section {
    private List<Bike> bikes;

    public Section() {
        this.bikes = new ArrayList<>();
    }

    // Add a bike to this section
    public void addBike(Bike book) {
        bikes.add(book);
    }

    // Get all bikes in this section
    public List<Bike> getBikes() {
        return bikes;
    }
}