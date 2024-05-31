package ui;

import exceptions.BikeNotFoundException;
import model.*;
import dao.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class AdminMenu extends JFrame{
    Shop shop = Shop.getInstance();
    private Admin admin;

    public AdminMenu(Shop shop, Admin admin) {
        this.shop = shop;
        this.admin = admin;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Welcome, " + admin.getName() + "!");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout setup
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Add buttons
        JButton addBikeButton = new JButton("Add Bike");
        JButton displayAllBikesButton = new JButton("Display All Bikes");
        JButton removeBikeButton = new JButton("Remove Bike");
        JButton displayAllPeopleButton = new JButton("Display All Users and Admins");
        JButton searchBikeByModelButton = new JButton("Search Bike By Model");
        JButton overdueBorrowReportButton = new JButton("Generate Overdue Borrow Report");
        JButton resetReservationsButton = new JButton("Reset Overdue Reservations");
        JButton historyButton = new JButton("History");

        // Set button size
        Dimension buttonSize = new Dimension(300, 50);
        addBikeButton.setPreferredSize(buttonSize);
        displayAllBikesButton.setPreferredSize(buttonSize);
        removeBikeButton.setPreferredSize(buttonSize);
        displayAllPeopleButton.setPreferredSize(buttonSize);
        searchBikeByModelButton.setPreferredSize(buttonSize);
        overdueBorrowReportButton.setPreferredSize(buttonSize);
        resetReservationsButton.setPreferredSize(buttonSize);
        historyButton.setPreferredSize(buttonSize);

        // Add buttons to layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(displayAllPeopleButton, gbc);

        gbc.gridy = 1;
        add(displayAllBikesButton, gbc);

        gbc.gridy = 2;
        add(addBikeButton, gbc);

        gbc.gridy = 3;
        add(removeBikeButton, gbc);

        gbc.gridy = 4;
        add(searchBikeByModelButton, gbc);

        gbc.gridy = 5;
        add(overdueBorrowReportButton, gbc);

        gbc.gridy = 6;
        add(resetReservationsButton, gbc);

        gbc.gridy = 7;
        add(historyButton, gbc);

        // Action listeners for buttons
        displayAllPeopleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                displayAllPeople();
            }
        });

        displayAllBikesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayAllBikes();
            }
        });

        addBikeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addBike();
            }
        });

        removeBikeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeBike();
            }
        });


        searchBikeByModelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                    searchBikeByModel();
                } catch (BikeNotFoundException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }

            }
        });

        overdueBorrowReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateOverdueBorrowReport();
            }
        });

        resetReservationsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetReservationsReport();
            }
        });

        historyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                history();
            }
        });
    }

    private void displayAllPeople() {
        List<String> peopleList = new ArrayList<>();
        for (Admin admin : shop.getAdmins()) {
            peopleList.add("Admin: ID: " + admin.getId() + ", name: " + admin.getName() + ", email:" + admin.getEmail());
        }

        for (User user : shop.getUsers()) {
            peopleList.add("User: ID: " + user.getId() + ", name: " + user.getName() + ", email: " + user.getEmail());
        }

        JFrame peopleFrame = new JFrame("People List");
        peopleFrame.setSize(500, 500);
        peopleFrame.setLocationRelativeTo(null);
        peopleFrame.setLayout(new BorderLayout());

        String[] peopleArray = new String[peopleList.size()];
        peopleArray = peopleList.toArray(peopleArray);

        JList<String> peopleJList = new JList<>(peopleArray);
        JScrollPane scrollPane = new JScrollPane(peopleJList);
        peopleFrame.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                peopleFrame.dispose(); // Close the people list frame
            }
        });
        peopleFrame.add(backButton, BorderLayout.SOUTH);

        peopleFrame.setVisible(true);

        shop.displayAllPeople(admin);
    }

    private void displayAllBikes() {

        List<String> bikesList = new ArrayList<>();

        for (Section section : shop.getSections().values()) {
            for (Bike bike : section.getBikes()) {
                bikesList.add("ID: " + bike.getId() + ", Model: " + bike.getModel() + ", Category: " + bike.getCategory() + ", Availability: " + bike.getAvailability());
                if (bike.getAvailability() == Availability.BORROWED) {
                    bikesList.add("Borrowed by: " + bike.getBorrower().getName() + ", Borrowed Date: " + bike.getBorrowDate());
                } else if (bike.getAvailability() == Availability.RESERVED) {
                    bikesList.add("Reserved by: " + bike.getReserver().getName() + ", Reservation Date: " + bike.getReservationDate());
                }
            }
        }

        JFrame bikeFrame = new JFrame("Bike List");
        bikeFrame.setSize(500, 500);
        bikeFrame.setLocationRelativeTo(null);
        bikeFrame.setLayout(new BorderLayout());

        String[] bikeArray = new String[bikesList.size()];
        bikeArray = bikesList.toArray(bikeArray);

        JList<String> bikeJList = new JList<>(bikeArray);
        JScrollPane scrollPane = new JScrollPane(bikeJList);
        bikeFrame.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bikeFrame.dispose(); // Close the people list frame
            }
        });
        bikeFrame.add(backButton, BorderLayout.SOUTH);

        bikeFrame.setVisible(true);

        shop.displayAllBikes();
    }

    private void addBike() {

        String[] options = {"MAN", "WOMAN", "KID"};
        int choice = JOptionPane.showOptionDialog(this,
                "Select Bike Category",
                "Bike Category",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        // Check if a category was selected
        if (choice != JOptionPane.CLOSED_OPTION) {
            // Get the selected category
            BikeCategory selectedCategory = null;
            switch (choice) {
                case 0:
                    selectedCategory = BikeCategory.MAN;
                    break;
                case 1:
                    selectedCategory = BikeCategory.WOMAN;
                    break;
                case 2:
                    selectedCategory = BikeCategory.KID;
                    break;
            }

            String model = JOptionPane.showInputDialog(this, "Enter Bike Model:");

            Bike bike = new Bike(shop.getNextBikeId(), model, selectedCategory, Availability.AVAILABLE);
            shop.addBike(admin, bike);
            JOptionPane.showMessageDialog(this, "Bike added successfully!");

        }

    }

    private void removeBike() {
        List<String> bikesList = new ArrayList<>();

        for (Section section : shop.getSections().values()) {
            for (Bike bike : section.getBikes()) {
                if (bike.getAvailability() == Availability.AVAILABLE) {
                    bikesList.add("ID: " + bike.getId() + ", Model: " + bike.getModel() + ", Category: " + bike.getCategory());
                }
            }
        }

        String[] bikesArray = bikesList.toArray(new String[0]);

        String bikeIdStr = (String) JOptionPane.showInputDialog(
                this,
                "Select the bike you want to remove:",
                "Remove Bike",
                JOptionPane.PLAIN_MESSAGE,
                null,
                bikesArray,
                bikesArray[0]);

        if (bikeIdStr != null) {
            try {
                int bikeId = Integer.parseInt(bikeIdStr.substring(bikeIdStr.indexOf("ID:") + 4, bikeIdStr.indexOf(",")));
                shop.removeBike(admin, bikeId);
                JOptionPane.showMessageDialog(this, "Bike removed successfully!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input");
            }
        }
    }

    private void searchBikeByModel() throws BikeNotFoundException {
        String model = JOptionPane.showInputDialog(this, "Search Bike Model:");
        Bike bike = shop.searchBikeByModel(model);
        if (bike != null) {
            if(bike.getAvailability() == Availability.AVAILABLE) {
                JOptionPane.showMessageDialog(this, "Bike found: ID: " + bike.getId() + ", Model: " + bike.getModel() + ", Category: " + bike.getCategory() + ", Availability: " + bike.getAvailability());
            } else if (bike.getAvailability() == Availability.BORROWED) {
                JOptionPane.showMessageDialog(this, "Bike found: ID: " + bike.getId() + ", Model: " + bike.getModel() + ", Category: " + bike.getCategory() + ", Availability: " + bike.getAvailability() + ", Borrowed by: " + bike.getBorrower().getName() + ", Borrowed Date: " + bike.getBorrowDate());
            } else if (bike.getAvailability() == Availability.RESERVED) {
                JOptionPane.showMessageDialog(this, "Bike found: ID: " + bike.getId() + ", Model: " + bike.getModel() + ", Category: " + bike.getCategory() + ", Availability: " + bike.getAvailability() + ", Reserved by: " + bike.getReserver().getName() + ", Reservation Date: " + bike.getReservationDate());
            };
        } else {
            JOptionPane.showMessageDialog(this, "No bike found with model: " + model);
        }

    }

    private void generateOverdueBorrowReport(){
        ReportBorrow overdueReport = new ReportBorrow(shop.getSections());
        List<String> reportsList = overdueReport.generateReport(true);

        JFrame reportFrame = new JFrame("Overdue report for borrowed bikes");
        reportFrame.setSize(550, 300);
        reportFrame.setLocationRelativeTo(null);
        reportFrame.setLayout(new BorderLayout());

        String[] reportArray = new String[reportsList.size()];
        reportArray = reportsList.toArray(reportArray);

        JList<String> reportJList = new JList<>(reportArray);
        JScrollPane scrollPane = new JScrollPane(reportJList);
        reportFrame.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reportFrame.dispose(); // Close the bike list frame
            }
        });
        reportFrame.add(backButton, BorderLayout.SOUTH);

        reportFrame.setVisible(true);
    }

    private void resetReservationsReport(){
        ReportReservation overdueReport = new ReportReservation(shop.getSections());
        List<String> reportsList = overdueReport.generateReport(false);

        JFrame reportFrame = new JFrame("Overdue report for reservations:");
        reportFrame.setSize(550, 300);
        reportFrame.setLocationRelativeTo(null);
        reportFrame.setLayout(new BorderLayout());

        String[] reportArray = new String[reportsList.size()];
        reportArray = reportsList.toArray(reportArray);

        JList<String> reportJList = new JList<>(reportArray);
        JScrollPane scrollPane = new JScrollPane(reportJList);
        reportFrame.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(reportFrame, "Are you sure you want to reset the availability for these overdue reservations?", "Confirm Reset", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    // Generate the report again with confirmation to reset the availability
                    overdueReport.generateReport(true);
                    JOptionPane.showMessageDialog(reportFrame, "Overdue reservations have been reset.");
                }
                reportFrame.dispose(); // Close the report frame
            }
        });
        reportFrame.add(backButton, BorderLayout.SOUTH);

        reportFrame.setVisible(true);
    }

    private void history(){
        TransactionDAO transactionDAO = new TransactionDAO();
        List<String> transactions = transactionDAO.generateHistory();

        JFrame reportFrame = new JFrame("History");
        reportFrame.setSize(550, 300);
        reportFrame.setLocationRelativeTo(null);
        reportFrame.setLayout(new BorderLayout());

        String[] reportArray = new String[transactions.size()];
        reportArray = transactions.toArray(reportArray);

        JList<String> reportJList = new JList<>(reportArray);
        JScrollPane scrollPane = new JScrollPane(reportJList);
        reportFrame.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reportFrame.dispose(); // Close the bike list frame
            }
        });
        reportFrame.add(backButton, BorderLayout.SOUTH);

        reportFrame.setVisible(true);

    }

}
