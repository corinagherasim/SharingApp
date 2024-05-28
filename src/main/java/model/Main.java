package model;

import dao.BikeDAO;
import dao.PersonDAO;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {

    private Shop shop;

    public Main() {
        shop = new Shop();
        initializeUI();
        shop.addPeopleFromDatabase();
    }


    private void initializeUI() {
        setTitle("Bike Shop Management");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout setup
        setLayout(new BorderLayout());

        // Add buttons
        JPanel buttonPanel = new JPanel();
        JButton addBikeButton = new JButton("Add Bike");

        buttonPanel.add(addBikeButton);

        add(buttonPanel, BorderLayout.NORTH);

        // Action listeners for buttons
        addBikeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addBike();
            }
        });
    }

    private void addBike() {
        String name = JOptionPane.showInputDialog(this, "Enter Admin name:");
        String password = JOptionPane.showInputDialog(this, "Enter Admin Password:");

        // Check if the entered credentials are valid
        Admin admin = shop.searchAdminByNamePassword(name,password);

        if(shop.isAdminFound(admin)){
            // Implementation of adding a bike
            String id = JOptionPane.showInputDialog(this, "Enter Bike id:");
            String model = JOptionPane.showInputDialog(this, "Enter Bike Model:");
            String categoryStr = JOptionPane.showInputDialog(this, "Enter Bike Category:");
            BikeCategory category = BikeCategory.valueOf(categoryStr.toUpperCase());

            Bike bike = new Bike(Integer.parseInt(id),model, category, Availability.AVAILABLE);
            shop.addBike(admin,bike);
            JOptionPane.showMessageDialog(this, "Bike added successfully!");
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}
