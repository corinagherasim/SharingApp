package model;

import dao.BikeDAO;
import dao.PersonDAO;
import ui.AdminMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Main extends JFrame {

    private Shop shop;

    public Main() {
        shop = new Shop();
        initializeUI();
        shop.addPeopleFromDatabase();
        shop.addBikesFromDatabase();
        shop.processTransactionsFromDatabase();
    }

    private void initializeUI() {
        setTitle("Bike Shop Management");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        Dimension buttonSize = new Dimension(300, 70);
        loginButton.setPreferredSize(buttonSize);
        registerButton.setPreferredSize(buttonSize);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // Add some spacing
        add(loginButton, gbc);

        gbc.gridy = 1;
        add(registerButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoginDialog();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showRegisterDialog();
            }
        });
    }

    private void showLoginDialog() {
        JTextField emailField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            Admin admin = shop.searchAdminByEmailPassword(email, password);
            if (admin != null) {
                new AdminMenu(shop, admin).setVisible(true);
            } else {
                User user = shop.searchUserByEmailPassword(email, password);
                if (user != null) {
                    showUserMenu(user);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid email or password");
                }
            }
        }
    }

    private void showRegisterDialog() {
        JTextField nameField = new JTextField(10);
        JTextField emailField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Register", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()){
                if (email.endsWith("@shop.com")) {
                    Admin admin = new Admin(shop.getNextPersonId(), name, email, password);
                    if(admin.validateEmail(email) && !shop.searchAdminByEmail(email)){
                        shop.addPerson(admin);
                        JOptionPane.showMessageDialog(this, "Admin registered successfully!");
                    } else{
                        JOptionPane.showMessageDialog(this, "Invalid or already taken email");
                    }

                } else {
                    User user = new User(shop.getNextPersonId(), name, email, password);
                    if(user.validateEmail(email) && !shop.searchUserByEmail(email)){
                        shop.addPerson(user);
                        JOptionPane.showMessageDialog(this, "User registered successfully!");
                    } else{
                        JOptionPane.showMessageDialog(this, "Invalid or already taken email");
                    }
                }
            } else{
                JOptionPane.showMessageDialog(this, "Incomplete information");
            }

        }
    }

    private void showAdminMenu(Admin admin) {
        JOptionPane.showMessageDialog(this, "Welcome Admin: " + admin.getName());
    }

    private void showUserMenu(User user) {
        JOptionPane.showMessageDialog(this, "Welcome User: " + user.getName());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}
