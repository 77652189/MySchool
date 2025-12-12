package com.nan.myschool.gui;

import com.nan.myschool.config.SessionManager;
import com.nan.myschool.entity.User;
import com.nan.myschool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@Component
public class SimpleLoginGUI extends JFrame {

    private final UserService userService;
    private final SessionManager sessionManager;
    private final MainMenuGUI mainMenuGUI;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton exitButton;
    private JLabel statusLabel;

    @Autowired
    public SimpleLoginGUI(UserService userService,
                          SessionManager sessionManager,
                          MainMenuGUI mainMenuGUI) {
        this.userService = userService;
        this.sessionManager = sessionManager;
        this.mainMenuGUI = mainMenuGUI;

        // Set MainMenuGUI's loginGUI reference
        this.mainMenuGUI.setLoginGUI(this);

        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("System Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 400);
        setLayout(new BorderLayout());

        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(52, 152, 219));
        logoPanel.setPreferredSize(new Dimension(450, 100));

        JLabel logoLabel = new JLabel("Pet Training School");
        logoLabel.setFont(new Font("Dialog", Font.BOLD, 26));
        logoLabel.setForeground(Color.WHITE);
        logoPanel.add(logoLabel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        usernameField = new JTextField();
        usernameField.setFont(new Font("Dialog", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(200, 32));
        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Dialog", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(200, 32));
        formPanel.add(passwordField, gbc);

        // Status label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        statusLabel.setForeground(Color.RED);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(statusLabel, gbc);

        // Hint information
        gbc.gridy = 3;
        JLabel hintLabel = new JLabel("<html><center>Test Accounts:<br>admin/admin123 (Administrator)<br>trainer_john/pass123 (Trainer)<br>owner_alice/pass123 (Pet Owner)</center></html>");
        hintLabel.setFont(new Font("Dialog", Font.PLAIN, 11));
        hintLabel.setForeground(Color.GRAY);
        hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(hintLabel, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Dialog", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(120, 40));
        loginButton.setBackground(new Color(52, 152, 219));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Dialog", Font.PLAIN, 14));
        exitButton.setPreferredSize(new Dimension(120, 40));
        exitButton.setFocusPainted(false);

        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);

        loginButton.addActionListener(e -> performLogin());
        exitButton.addActionListener(e -> System.exit(0));
        passwordField.addActionListener(e -> performLogin());

        add(logoPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Username and password cannot be empty!");
            statusLabel.setForeground(Color.RED);
            return;
        }

        List<User> allUsers = userService.getAllUsers();
        User matchedUser = null;

        for (User user : allUsers) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                matchedUser = user;
                break;
            }
        }

        if (matchedUser != null) {
            sessionManager.setCurrentUser(matchedUser);
            statusLabel.setText("Login successful!");
            statusLabel.setForeground(new Color(46, 204, 113));

            Timer timer = new Timer(500, e -> {
                mainMenuGUI.refreshMenu();
                mainMenuGUI.setVisible(true);
                this.setVisible(false);
            });
            timer.setRepeats(false);
            timer.start();

        } else {
            statusLabel.setText("Incorrect username or password!");
            statusLabel.setForeground(Color.RED);
            passwordField.setText("");
        }
    }

    /**
     * Clear login form
     */
    public void clearForm() {
        usernameField.setText("");
        passwordField.setText("");
        statusLabel.setText(" ");
    }
}