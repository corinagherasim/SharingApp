package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportReservation extends Report {

    public ReportReservation(Map<BikeCategory, Section> sections) {
        super(sections);
    }

    @Override
    public List<String> generateReport(boolean confirm) {
        List<String> reportList = new ArrayList<>();
        int reserved = 0;

        for (BikeCategory bikeCategory : sections.keySet()) {
            Section section = sections.get(bikeCategory);

            if (!section.getBikes().isEmpty()) {
                for (Bike bike : section.getBikes()) {
                    if (bike.getAvailability() == Availability.RESERVED && LocalDate.now().isAfter(bike.getReservationDate().plusDays(7))) {
                        reserved = 1;
                        LocalDate reservationDate = bike.getReservationDate();
                        long daysDifference = Math.abs(LocalDate.now().until(reservationDate, ChronoUnit.DAYS)) - 7;
                        String bikeInfo = "ID: " + bike.getId() + "\n" +
                                ", Model: " + bike.getModel() + "\n" +
                                ", Category: " + bike.getCategory() + "\n" +
                                ", Reserved by: " + bike.getReserver().getName() + "\n" +
                                ", Reservation Date: " + bike.getReservationDate() + "\n" +
                                ", Days Overdue: " + daysDifference + "\n";

                        reportList.add(bikeInfo);

                        if (confirm){
                            bike.setAvailability(Availability.AVAILABLE);
                        }

                    }
                }
            }
        }
        if (reserved == 0) {
            reportList.add("No expired reservations found.");
        }

        return reportList;
    }
}