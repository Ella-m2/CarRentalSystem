package carrental.gui;

import java.awt.GridLayout;

import javax.swing.*;

import carrental.exceptions.AccountCreationException;
import carrental.models.Administrator;
import carrental.models.CarInventory;
import carrental.models.RentalHistory;
import carrental.util.AdminAuthentication;

public class AdminLoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JButton loginButton;
    private JButton createAccountButton;
    private CarInventory carInventory;

    public AdminLoginPanel(CarInventory inventory, RentalHistory rentalHistory) {
        initComponents();
        setLayout(new GridLayout(5, 2));
        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(new JLabel("Full Name:"));
        add(fullNameField);
        add(new JLabel("Email:"));
        add(emailField);
        add(loginButton);
        add(createAccountButton);
        carInventory = inventory;

        loginButton.addActionListener(e -> adminLogin(rentalHistory));
        createAccountButton.addActionListener(e -> adminCreateAccount());
    }

    private void initComponents() {
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        fullNameField = new JTextField(); // Initialize the new field
        emailField = new JTextField();    // Initialize the new field
        loginButton = new JButton("Login");
        createAccountButton = new JButton("Create Account");
    }

    private void adminLogin(RentalHistory rentalHistory) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText();
        Administrator authenticatedUser = AdminAuthentication.authenticateUser(username, password, email);
        if (authenticatedUser != null) {
            JOptionPane.showMessageDialog(this, "Administrator Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            // Open a new window upon successful login
            openAdminMainWindow(authenticatedUser, rentalHistory);
        } else {
            JOptionPane.showMessageDialog(this, "Administrator Login failed. Please check your credentials.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAdminMainWindow(Administrator authenticatedUser, RentalHistory rentalHistory) {
        // You can create and display a new window for the administrator here
        AdminMainWindow adminMainWindow = new AdminMainWindow(authenticatedUser, carInventory,  rentalHistory);
        adminMainWindow.setVisible(true);

        // Close the current login window
        SwingUtilities.getWindowAncestor(this).dispose();
    }

    private void adminCreateAccount() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String fullName = fullNameField.getText();
        String email = emailField.getText();

        Administrator newUser = new Administrator(username, password, fullName, email);
        try {
            AdminAuthentication.createUser(newUser);
            JOptionPane.showMessageDialog(this, "Administrator Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (AccountCreationException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
