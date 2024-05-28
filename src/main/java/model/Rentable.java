package model;

import java.time.LocalDate;

public interface Rentable {
    boolean borrowBike(Bike bike, User borrower, LocalDate borrowDate, boolean firstTime);
    boolean reserveBike(Bike bike, User user, LocalDate reserveDate, boolean firstTime);
    void returnBike(Bike bike, boolean firstTime);
}
