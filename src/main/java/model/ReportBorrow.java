package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.time.temporal.ChronoUnit;

public class ReportBorrow extends Report {
    public ReportBorrow(Map<BikeCategory, Section> sections) {
        super(sections);
    }

    @Override
    public List<String> generateReport(boolean confirm) {
        List<String> reportList = new ArrayList<>();
        int borrowed = 0;

        for (BikeCategory bikeCategory : sections.keySet()) {
            Section section = sections.get(bikeCategory);

            if (!section.getBikes().isEmpty()) {
                for (Bike bike : section.getBikes()) {
                    if (bike.getAvailability() == Availability.BORROWED && LocalDate.now().isAfter(bike.getBorrowDate().plusDays(14))) {
                        borrowed = 1;
                        LocalDate borrowDate = bike.getBorrowDate();
                        long daysDifference = Math.abs(LocalDate.now().until(borrowDate, ChronoUnit.DAYS)) - 14;
                        String bikeInfo = "ID: " + bike.getId() + "\n" +
                                ", Model: " + bike.getModel() + "\n" +
                                ", Category: " + bike.getCategory() + "\n" +
                                ", Borrowed by: " + bike.getBorrower().getName() + "\n" +
                                ", Borrowed Date: " + bike.getBorrowDate() + "\n" +
                                ", Days Overdue: " + daysDifference + "\n";
                        reportList.add(bikeInfo);
                    }
                }
            }
        }
        if(borrowed == 0) {
            reportList.add("No overdue bikes found.");
        }

        return reportList;
    }
}

