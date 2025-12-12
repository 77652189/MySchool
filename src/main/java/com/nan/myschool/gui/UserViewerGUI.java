package com.nan.myschool.gui;

import com.nan.myschool.entity.User;
import com.nan.myschool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@Component
public class UserViewerGUI extends JFrame {
    private final UserService userService;
    private List<User> users;
    private int currentIndex = 0;

    private JLabel idLabel = new JLabel("User ID: ");
    private JLabel usernameLabel = new JLabel("Username: ");
    private JLabel roleLabel = new JLabel("Role: ");
    private JButton prevButton = new JButton("Previous");
    private JButton nextButton = new JButton("Next");
    private JButton addButton = new JButton("Add");
    private JButton deleteButton = new JButton("Delete");
    private JButton updateButton = new JButton("Update");

    @Autowired
    public UserViewerGUI(UserService userService) {
        this.userService = userService;
        initializeGUI();
        loadUsers();
    }

    private void initializeGUI() {
        setTitle("User Management System");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout(10, 10));

        // Information panel
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoPanel.add(idLabel);
        infoPanel.add(usernameLabel);
        infoPanel.add(roleLabel);

        // Navigation button panel
        JPanel navPanel = new JPanel(new FlowLayout());
        navPanel.add(prevButton);
        navPanel.add(nextButton);

        // Add refresh button
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> {
            loadUsers();
            JOptionPane.showMessageDialog(this, "Data refreshed!", "Notice", JOptionPane.INFORMATION_MESSAGE);
        });
        navPanel.add(refreshBtn);

        // Action button panel
        JPanel actionPanel = new JPanel(new FlowLayout());
        actionPanel.add(addButton);
        actionPanel.add(deleteButton);
        actionPanel.add(updateButton);

        // Add all panels to main window
        add(infoPanel, BorderLayout.CENTER);
        add(navPanel, BorderLayout.NORTH);
        add(actionPanel, BorderLayout.SOUTH);

        // Add button events
        prevButton.addActionListener(e -> showPreviousUser());
        nextButton.addActionListener(e -> showNextUser());
        addButton.addActionListener(e -> addNewUser());
        deleteButton.addActionListener(e -> deleteCurrentUser());
        updateButton.addActionListener(e -> updateCurrentUser());

        // Center window on screen
        setLocationRelativeTo(null);
    }

    private void loadUsers() {
        users = userService.getAllUsers();
        currentIndex = 0;
        if (!users.isEmpty()) {
            displayCurrentUser();
        } else {
            idLabel.setText("User ID: No data");
            usernameLabel.setText("Username: Please add users first");
            roleLabel.setText("Role: -");
        }
    }

    private void displayCurrentUser() {
        if (users != null && !users.isEmpty()) {
            User currentUser = users.get(currentIndex);
            idLabel.setText("User ID: " + currentUser.getId());
            usernameLabel.setText("Username: " + currentUser.getUsername());
            roleLabel.setText("Role: " + currentUser.getRole());
        }
    }

    private void showPreviousUser() {
        if (currentIndex > 0) {
            currentIndex--;
            displayCurrentUser();
        }
    }

    private void showNextUser() {
        if (currentIndex < users.size() - 1) {
            currentIndex++;
            displayCurrentUser();
        }
    }

    private void addNewUser() {
        JDialog dialog = new JDialog(this, "Add New User", true);
        dialog.setLayout(new GridLayout(4, 2, 5, 5));

        ((JPanel)dialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField usernameField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField roleField = new JTextField();

        dialog.add(new JLabel("Username:"));
        dialog.add(usernameField);
        dialog.add(new JLabel("Password:"));
        dialog.add(passwordField);
        dialog.add(new JLabel("Role:"));
        dialog.add(roleField);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            User newUser = new User();
            newUser.setUsername(usernameField.getText());
            newUser.setPassword(passwordField.getText());
            newUser.setRole(roleField.getText());
            userService.saveUser(newUser);
            loadUsers();
            dialog.dispose();
        });

        dialog.add(submitButton);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void deleteCurrentUser() {
        if (users != null && !users.isEmpty()) {
            User currentUser = users.get(currentIndex);
            int option = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete user " + currentUser.getUsername() + "?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                userService.deleteUser(currentUser.getId());
                loadUsers();
                if (currentIndex >= users.size() && currentIndex > 0) {
                    currentIndex--;
                }
                if (!users.isEmpty()) {
                    displayCurrentUser();
                }
            }
        }
    }

    private void updateCurrentUser() {
        if (users == null || users.isEmpty()) return;

        User currentUser = users.get(currentIndex);
        JDialog dialog = new JDialog(this, "Update User Information", true);
        dialog.setLayout(new GridLayout(4, 2, 5, 5));

        ((JPanel)dialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField usernameField = new JTextField(currentUser.getUsername());
        JTextField passwordField = new JTextField(currentUser.getPassword());
        JTextField roleField = new JTextField(currentUser.getRole());

        dialog.add(new JLabel("Username:"));
        dialog.add(usernameField);
        dialog.add(new JLabel("Password:"));
        dialog.add(passwordField);
        dialog.add(new JLabel("Role:"));
        dialog.add(roleField);

        JButton submitButton = new JButton("Save");
        submitButton.addActionListener(e -> {
            currentUser.setUsername(usernameField.getText());
            currentUser.setPassword(passwordField.getText());
            currentUser.setRole(roleField.getText());
            userService.saveUser(currentUser);
            loadUsers();
            dialog.dispose();
        });

        dialog.add(submitButton);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}