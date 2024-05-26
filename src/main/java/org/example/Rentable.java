package org.example;

import java.time.LocalDate;

public interface Rentable {
    boolean borrowBike(Bike bike, User borrower, LocalDate borrowDate);
    boolean reserveBike(Bike bike, User user, LocalDate reserveDate);
    void returnBike(Bike bike);
}
