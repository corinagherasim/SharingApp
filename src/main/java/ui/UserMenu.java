package ui;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

public class UserMenu extends JFrame{
    Shop shop = Shop.getInstance();
    private User user;

    public UserMenu(Shop shop, User user) {
        this.shop = shop;
        this.user = user;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Welcome, " + user.getName() + "!");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout setup
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Add buttons
        JButton displayAllBikesButton = new JButton("Display All Bikes");
        JButton searchBikeByModelButton = new JButton("Search Bike By Model");
        JButton borrowButton = new JButton("Borrow Bike");
        JButton reserveButton = new JButton("Reserve Bike");
        JButton returnButton = new JButton("Return Bike");
        JButton changePassword = new JButton("Change password");

        // Set button size
        Dimension buttonSize = new Dimension(300, 50);
        displayAllBikesButton.setPreferredSize(buttonSize);
        searchBikeByModelButton.setPreferredSize(buttonSize);
        borrowButton.setPreferredSize(buttonSize);
        reserveButton.setPreferredSize(buttonSize);
        returnButton.setPreferredSize(buttonSize);
        changePassword.setPreferredSize(buttonSize);


        // Add buttons to layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(displayAllBikesButton, gbc);

        gbc.gridy = 1;
        add(searchBikeByModelButton, gbc);

        gbc.gridy = 2;
        add(borrowButton, gbc);

        gbc.gridy = 3;
        add(reserveButton, gbc);

        gbc.gridy = 4;
        add(returnButton, gbc);

        gbc.gridy = 5;
        add(changePassword, gbc);

        displayAllBikesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayAllBikes();
            }
        });

        searchBikeByModelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchBikeByModel();
            }
        });

        borrowButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                borrowBike();
            }
        });

        reserveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reserveBike();
            }
        });

        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnBike();
            }
        });

        changePassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private void displayAllBikes() {

        List<String> bikesList = new ArrayList<>();

        for (Section section : shop.getSections().values()) {
            for (Bike bike : section.getBikes()) {
                bikesList.add("ID: " + bike.getId() + ", Model: " + bike.getModel() + ", Category: " + bike.getCategory() + ", Availability: " + bike.getAvailability());
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

    private void searchBikeByModel(){
        String model = JOptionPane.showInputDialog(this, "Search Bike Model:");
        Bike bike = shop.searchBikeByModel(model);
        if (bike != null) {
            JOptionPane.showMessageDialog(this, "Bike found: ID: " + bike.getId() + ", Model: " + bike.getModel() + ", Category: " + bike.getCategory() + ", Availability: " + bike.getAvailability());
        } else {
            JOptionPane.showMessageDialog(this, "No bike found with model: " + model);
        }

    }

    private void borrowBike(){
        List<String> bikesList = new ArrayList<>();

        for (Section section : shop.getSections().values()) {
            for (Bike bike : section.getBikes()) {
                if (bike.getAvailability() == Availability.AVAILABLE) {
                    bikesList.add("ID: " + bike.getId() + ", Model: " + bike.getModel() + ", Category: " + bike.getCategory());
                } else if (bike.getAvailability() == Availability.RESERVED) {
                    if (bike.getReserver() != null && bike.getReserver().equals(user)) {
                        bikesList.add("ID: " + bike.getId() + ", Model: " + bike.getModel() + ", Category: " + bike.getCategory());
                    } else {
                        LocalDate reservationDate = bike.getReservationDate();
                        if (ChronoUnit.DAYS.between(reservationDate, LocalDate.now()) > 7) {
                            bikesList.add("ID: " + bike.getId() + ", Model: " + bike.getModel() + ", Category: " + bike.getCategory());
                        }
                    }
                }
            }
        }


        String[] bikesArray = bikesList.toArray(new String[0]);

        String bikeIdStr = (String) JOptionPane.showInputDialog(
                this,
                "Select the bike you want to borrow:",
                "Borrow Bike",
                JOptionPane.PLAIN_MESSAGE,
                null,
                bikesArray,
                bikesArray[0]);

        if (bikeIdStr != null) {
            try {
                int bikeId = Integer.parseInt(bikeIdStr.substring(bikeIdStr.indexOf("ID:") + 4, bikeIdStr.indexOf(",")));
                shop.borrowBike(shop.searchBikeById(bikeId),user, LocalDate.now(), false);
                JOptionPane.showMessageDialog(this, "Bike borrowed successfully by " + user.getName() + "!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input");
            }
        }

    }

    private void reserveBike(){
        List<String> bikesList = new ArrayList<>();

        for (Section section : shop.getSections().values()) {
            for (Bike bike : section.getBikes()) {
                if (bike.getAvailability() == Availability.AVAILABLE) {
                    bikesList.add("ID: " + bike.getId() + ", Model: " + bike.getModel() + ", Category: " + bike.getCategory());
                } else if (bike.getAvailability() == Availability.RESERVED) {
                        LocalDate reservationDate = bike.getReservationDate();
                        if (ChronoUnit.DAYS.between(reservationDate, LocalDate.now()) > 7) {
                            bikesList.add("ID: " + bike.getId() + ", Model: " + bike.getModel() + ", Category: " + bike.getCategory());
                        }
                }
            }
        }


        String[] bikesArray = bikesList.toArray(new String[0]);

        String bikeIdStr = (String) JOptionPane.showInputDialog(
                this,
                "Select the bike you want to reserve:",
                "Reserve Bike",
                JOptionPane.PLAIN_MESSAGE,
                null,
                bikesArray,
                bikesArray[0]);

        if (bikeIdStr != null) {
            try {
                int bikeId = Integer.parseInt(bikeIdStr.substring(bikeIdStr.indexOf("ID:") + 4, bikeIdStr.indexOf(",")));
                shop.reserveBike(shop.searchBikeById(bikeId),user, LocalDate.now(), false);
                JOptionPane.showMessageDialog(this, "Bike reserved successfully by " + user.getName() + "!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input");
            }
        }
    }

    private void returnBike(){
        List<String> bikesList = new ArrayList<>();

        for (Section section : shop.getSections().values()) {
            for (Bike bike : section.getBikes()) {
                if (bike.getAvailability() == Availability.BORROWED && user == shop.searchUserByEmailPassword(bike.getBorrower().getEmail(), bike.getBorrower().getPassword())) {
                    bikesList.add("ID: " + bike.getId() + ", Model: " + bike.getModel() + ", Category: " + bike.getCategory());
                }
            }
        }

        String[] bikesArray = bikesList.toArray(new String[0]);

        String bikeIdStr = (String) JOptionPane.showInputDialog(
                this,
                "Select the bike you want to return:",
                "Return Bike",
                JOptionPane.PLAIN_MESSAGE,
                null,
                bikesArray,
                bikesArray[0]);

        if (bikeIdStr != null) {
            try {
                int bikeId = Integer.parseInt(bikeIdStr.substring(bikeIdStr.indexOf("ID:") + 4, bikeIdStr.indexOf(",")));
                shop.returnBike(shop.searchBikeById(bikeId), false);
                JOptionPane.showMessageDialog(this, "Bike returned successfully by " + user.getName() + "!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input");
            }
        }
    }
}
