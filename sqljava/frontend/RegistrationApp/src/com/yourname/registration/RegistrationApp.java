package com.yourname.registration;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class RegistrationApp extends JFrame {
    // Fields for the form
    private JTextField idField, nameField, emailField, dobField;
    private JButton addButton, viewButton, updateButton, deleteButton;

    public RegistrationApp() {
        // Set up the frame
        setTitle("Registration Form");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // ID field for Update/Delete operations
        JLabel idLabel = new JLabel("ID:");
        idLabel.setBounds(20, 20, 80, 25);
        add(idLabel);

        idField = new JTextField();
        idField.setBounds(100, 20, 200, 25);
        add(idField);

        // Form fields
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 60, 80, 25);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(100, 60, 200, 25);
        add(nameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 100, 80, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(100, 100, 200, 25);
        add(emailField);

        JLabel dobLabel = new JLabel("Date of Birth (YYYY-MM-DD):");
        dobLabel.setBounds(20, 140, 200, 25);
        add(dobLabel);

        dobField = new JTextField();
        dobField.setBounds(220, 140, 100, 25);
        add(dobField);

        // Buttons for CRUD operations
        addButton = new JButton("Add");
        addButton.setBounds(50, 200, 80, 25);
        add(addButton);

        viewButton = new JButton("View");
        viewButton.setBounds(150, 200, 80, 25);
        add(viewButton);

        updateButton = new JButton("Update");
        updateButton.setBounds(250, 200, 80, 25);
        add(updateButton);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(350, 200, 80, 25);
        add(deleteButton);

        // Action listeners for buttons
        addButton.addActionListener(e -> addRegistration());
        viewButton.addActionListener(e -> viewRegistration());
        updateButton.addActionListener(e -> updateRegistration());
        deleteButton.addActionListener(e -> deleteRegistration());

        setVisible(true);
    }

    // JDBC connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/registration_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Mukund@2001";

    // **1. Add (Create) Operation**
    private void addRegistration() {
        String name = nameField.getText();
        String email = emailField.getText();
        String dob = dobField.getText();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO Registration (name, email, date_of_birth) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, dob);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Registration added successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // **2. View (Read) Operation**
    private void viewRegistration() {
        String id = idField.getText();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM Registration WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                emailField.setText(rs.getString("email"));
                dobField.setText(rs.getString("date_of_birth"));
            } else {
                JOptionPane.showMessageDialog(this, "No record found with ID: " + id);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // **3. Update Operation**
    private void updateRegistration() {
        String id = idField.getText();
        String name = nameField.getText();
        String email = emailField.getText();
        String dob = dobField.getText();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE Registration SET name = ?, email = ?, date_of_birth = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, dob);
            stmt.setString(4, id);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Record updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No record found with ID: " + id);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // **4. Delete Operation**
    private void deleteRegistration() {
        String id = idField.getText();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "DELETE FROM Registration WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "Record deleted successfully!");
                // Clear form fields
                idField.setText("");
                nameField.setText("");
                emailField.setText("");
                dobField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "No record found with ID: " + id);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new RegistrationApp();
    }
}
