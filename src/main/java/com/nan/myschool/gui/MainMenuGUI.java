package com.nan.myschool.gui;

import com.nan.myschool.config.SessionManager;
import com.nan.myschool.config.ThemeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

@Component
public class MainMenuGUI extends JFrame {
    private final UserViewerGUI userViewerGUI;
    private final SectionMasterDetailGUI sectionMasterDetailGUI;
    private final EnrollmentManagementGUI enrollmentManagementGUI;
    private final ThemeSettingsDialog themeSettingsDialog;
    private final ThemeManager themeManager;
    private final PetProfileGUI petProfileGUI;
    private final AchievementWallGUI achievementWallGUI;
    private final SessionManager sessionManager;
    private SimpleLoginGUI loginGUI;

    private JPanel menuPanel;
    private JLabel userInfoLabel;

    @Autowired
    public MainMenuGUI(UserViewerGUI userViewerGUI,
                       SectionMasterDetailGUI sectionMasterDetailGUI,
                       EnrollmentManagementGUI enrollmentManagementGUI,
                       ThemeSettingsDialog themeSettingsDialog,
                       ThemeManager themeManager,
                       PetProfileGUI petProfileGUI,
                       AchievementWallGUI achievementWallGUI,
                       SessionManager sessionManager) {
        this.userViewerGUI = userViewerGUI;
        this.sectionMasterDetailGUI = sectionMasterDetailGUI;
        this.enrollmentManagementGUI = enrollmentManagementGUI;
        this.themeSettingsDialog = themeSettingsDialog;
        this.themeManager = themeManager;
        this.petProfileGUI = petProfileGUI;
        this.achievementWallGUI = achievementWallGUI;
        this.sessionManager = sessionManager;
        initializeGUI();
    }

    public void setLoginGUI(SimpleLoginGUI loginGUI) {
        this.loginGUI = loginGUI;
    }

    private void initializeGUI() {
        setTitle("Pet Training School Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 720);
        setLayout(new BorderLayout(0, 0));

        JPanel headerPanel = createHeaderPanel();
        menuPanel = createCenterPanel();
        JPanel footerPanel = createFooterPanel();

        add(headerPanel, BorderLayout.NORTH);
        add(menuPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setResizable(false);
    }

    private JPanel createHeaderPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setPreferredSize(new Dimension(650, 160));
        container.setBackground(themeManager.isDarkMode() ? new Color(52, 73, 94) : new Color(52, 152, 219));

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        topBar.setOpaque(false);

        JButton themeBtn = new JButton("Toggle Theme");
        themeBtn.setFont(new Font("Dialog", Font.PLAIN, 12));
        themeBtn.setPreferredSize(new Dimension(110, 32));
        themeBtn.setBackground(new Color(255, 255, 255, 40));
        themeBtn.setForeground(Color.WHITE);
        themeBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        themeBtn.setFocusPainted(false);
        themeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        themeBtn.addActionListener(e -> openThemeSettings());

        themeBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                themeBtn.setBackground(new Color(255, 255, 255, 60));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                themeBtn.setBackground(new Color(255, 255, 255, 40));
            }
        });

        topBar.add(themeBtn);

        JPanel centerArea = new JPanel();
        centerArea.setOpaque(false);
        centerArea.setLayout(new BoxLayout(centerArea, BoxLayout.Y_AXIS));

        JLabel title1 = new JLabel("Pet Training School");
        title1.setFont(new Font("Dialog", Font.BOLD, 38));
        title1.setForeground(Color.WHITE);
        title1.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        JLabel title2 = new JLabel("Management System");
        title2.setFont(new Font("Dialog", Font.PLAIN, 18));
        title2.setForeground(Color.WHITE);
        title2.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        userInfoLabel = new JLabel(getUserInfoText());
        userInfoLabel.setFont(new Font("Dialog", Font.PLAIN, 13));
        userInfoLabel.setForeground(new Color(236, 240, 241));
        userInfoLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        centerArea.add(Box.createVerticalStrut(20));
        centerArea.add(title1);
        centerArea.add(Box.createVerticalStrut(10));
        centerArea.add(title2);
        centerArea.add(Box.createVerticalStrut(8));
        centerArea.add(userInfoLabel);
        centerArea.add(Box.createVerticalStrut(20));

        container.add(topBar, BorderLayout.NORTH);
        container.add(centerArea, BorderLayout.CENTER);

        return container;
    }

    private String getUserInfoText() {
        if (!sessionManager.isLoggedIn()) {
            return "Not logged in - Please login to access full features";
        }

        String username = sessionManager.getCurrentUser().getUsername();
        String role = getRoleDisplayName(sessionManager.getCurrentRole());
        return "Current User: " + username + " (" + role + ")";
    }

    private String getRoleDisplayName(String role) {
        return switch (role) {
            case "Admin" -> "Administrator";
            case "Trainer" -> "Trainer";
            case "PetOwner" -> "Pet Owner";
            default -> "Unknown";
        };
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBorder(new EmptyBorder(30, 60, 30, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);

        int rowIndex = 0;
        String role = sessionManager.getCurrentRole();

        // If not logged in, only show login button
        if (!sessionManager.isLoggedIn()) {
            JButton loginButton = createStyledButton("Click to Login", new Color(46, 204, 113), "LOGIN");
            loginButton.addActionListener(e -> returnToLogin());
            gbc.gridy = rowIndex++;
            centerPanel.add(loginButton, gbc);

            JButton exitButton = createStyledButton("Exit System", new Color(231, 76, 60), "EXIT");
            exitButton.addActionListener(e -> exitApplication());
            gbc.gridy = rowIndex++;
            gbc.insets = new Insets(15, 0, 0, 0);
            centerPanel.add(exitButton, gbc);

            return centerPanel;
        }

        // Logged in: Show features based on role

        // Pet Profile - PetOwner, Admin
        if ("PetOwner".equals(role) || "Admin".equals(role)) {
            JButton petProfileButton = createStyledButton("Pet Profiles", new Color(52, 152, 219), "PETS");
            petProfileButton.addActionListener(e -> petProfileGUI.setVisible(true));
            gbc.gridy = rowIndex++;
            centerPanel.add(petProfileButton, gbc);
        }

        // Training Course Management - Trainer, Admin
        if ("Trainer".equals(role) || "Admin".equals(role)) {
            JButton trainingButton = createStyledButton("Training Course Management", new Color(46, 204, 113), "TRAINING");
            trainingButton.addActionListener(e -> openTrainingManagement());
            gbc.gridy = rowIndex++;
            centerPanel.add(trainingButton, gbc);
        }

        // Enrollment Management - PetOwner, Admin
        if ("PetOwner".equals(role) || "Admin".equals(role)) {
            JButton enrollmentButton = createStyledButton("Enrollment Management", new Color(155, 89, 182), "ENROLL");
            enrollmentButton.addActionListener(e -> openEnrollmentManagement());
            gbc.gridy = rowIndex++;
            centerPanel.add(enrollmentButton, gbc);
        }

        // Achievement Wall - Trainer, Admin
        if ("Trainer".equals(role) || "Admin".equals(role)) {
            JButton achievementButton = createStyledButton("Achievement Wall", new Color(241, 196, 15), "AWARDS");
            achievementButton.addActionListener(e -> achievementWallGUI.setVisible(true));
            gbc.gridy = rowIndex++;
            centerPanel.add(achievementButton, gbc);
        }

        // User Management - Admin only
        if ("Admin".equals(role)) {
            JButton userButton = createStyledButton("User Management", new Color(149, 165, 166), "USERS");
            userButton.addActionListener(e -> openUserManagement());
            gbc.gridy = rowIndex++;
            centerPanel.add(userButton, gbc);
        }

        // Theme Settings - Everyone
        JButton themeSettingsButton = createStyledButton("Theme Settings", new Color(243, 156, 18), "THEME");
        themeSettingsButton.addActionListener(e -> openThemeSettings());
        gbc.gridy = rowIndex++;
        centerPanel.add(themeSettingsButton, gbc);

        // About System - Everyone
        JButton aboutButton = createStyledButton("About System", new Color(52, 73, 94), "ABOUT");
        aboutButton.addActionListener(e -> showAbout());
        gbc.gridy = rowIndex++;
        centerPanel.add(aboutButton, gbc);

        // Logout button
        JButton logoutButton = createStyledButton("Logout", new Color(230, 126, 34), "LOGOUT");
        logoutButton.addActionListener(e -> logout());
        gbc.gridy = rowIndex++;
        gbc.insets = new Insets(15, 0, 0, 0);
        centerPanel.add(logoutButton, gbc);

        return centerPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(new EmptyBorder(12, 0, 12, 0));

        JLabel footerLabel = new JLabel("© 2025 Pet Training School System | Developed by Nan");
        footerLabel.setFont(new Font("Dialog", Font.PLAIN, 11));
        footerLabel.setForeground(Color.GRAY);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerPanel.add(footerLabel, BorderLayout.CENTER);

        return footerPanel;
    }

    private JButton createStyledButton(String text, Color backgroundColor, String tag) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout(15, 0));
        button.setPreferredSize(new Dimension(450, 55));
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel tagLabel = new JLabel(tag);
        tagLabel.setFont(new Font("Arial", Font.BOLD, 11));
        tagLabel.setForeground(new Color(255, 255, 255, 180));
        tagLabel.setBorder(new EmptyBorder(0, 20, 0, 0));

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Dialog", Font.PLAIN, 17));
        textLabel.setForeground(Color.WHITE);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);

        button.add(tagLabel, BorderLayout.WEST);
        button.add(textLabel, BorderLayout.CENTER);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = backgroundColor;
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });

        return button;
    }

    public void refreshMenu() {
        if (userInfoLabel != null) {
            userInfoLabel.setText(getUserInfoText());
        }

        remove(menuPanel);
        menuPanel = createCenterPanel();
        add(menuPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private void logout() {
        int option = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            sessionManager.logout();

            // Close main menu and return to login screen
            this.setVisible(false);

            if (loginGUI != null) {
                loginGUI.clearForm();
                loginGUI.setVisible(true);
            }
        }
    }

    private void returnToLogin() {
        this.setVisible(false);
        if (loginGUI != null) {
            loginGUI.setVisible(true);
        }
    }

    private void openTrainingManagement() {
        sectionMasterDetailGUI.setVisible(true);
    }

    private void openEnrollmentManagement() {
        enrollmentManagementGUI.setVisible(true);
    }

    private void openUserManagement() {
        userViewerGUI.setVisible(true);
    }

    private void openThemeSettings() {
        themeSettingsDialog.showDialog();
    }

    private void showAbout() {
        String message = "Pet Training School Management System v1.0\n\n" +
                "Professional Pet Training & Management Platform\n\n" +
                "Features:\n" +
                "• Pet Profile Management\n" +
                "• Training Course Management\n" +
                "• Enrollment Management\n" +
                "• Achievement Wall Display\n" +
                "• User Management\n" +
                "• Theme Switching\n" +
                "• Role-Based Access Control\n\n" +
                "Our Mission:\n" +
                "Help every pet become the best version of themselves!\n\n" +
                "Developer: Nan\n" +
                "School: Northeastern University\n" +
                "Version Date: 2025";
        JOptionPane.showMessageDialog(this, message, "About System", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exitApplication() {
        int option = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit the system?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}