package com.nan.myschool.gui;

import com.nan.myschool.config.ThemeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class ThemeSettingsDialog extends JDialog {

    private final ThemeManager themeManager;
    private JToggleButton themeToggle;
    private JLabel themeStatusLabel;

    @Autowired
    public ThemeSettingsDialog(ThemeManager themeManager) {
        this.themeManager = themeManager;
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Theme Settings");
        setModal(true);
        setSize(450, 300);
        setLayout(new BorderLayout(10, 10));

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        // Title
        JLabel titleLabel = new JLabel("Appearance Settings");
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 22));
        titleLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        // Current theme status
        themeStatusLabel = new JLabel("Current Theme: " + themeManager.getCurrentThemeName());
        themeStatusLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
        themeStatusLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        // Theme toggle panel
        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 25));

        // Toggle button
        themeToggle = new JToggleButton();
        themeToggle.setPreferredSize(new Dimension(70, 35));
        themeToggle.setSelected(themeManager.isDarkMode());
        themeToggle.setFocusPainted(false);
        themeToggle.setText(themeManager.isDarkMode() ? "Dark" : "Light");
        themeToggle.setFont(new Font("Dialog", Font.PLAIN, 12));
        themeToggle.addActionListener(e -> switchTheme());

        togglePanel.add(themeToggle);

        // Description text
        JLabel descLabel = new JLabel("Click the toggle button to change theme");
        descLabel.setFont(new Font("Dialog", Font.PLAIN, 13));
        descLabel.setForeground(Color.GRAY);
        descLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        // Preview panel
        JPanel previewPanel = createPreviewPanel();

        // Add components
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(themeStatusLabel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(togglePanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(descLabel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(previewPanel);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));

        JButton applyButton = new JButton("Apply");
        applyButton.setPreferredSize(new Dimension(100, 38));
        applyButton.setFont(new Font("Dialog", Font.PLAIN, 14));
        applyButton.setBackground(new Color(52, 152, 219));
        applyButton.setForeground(Color.WHITE);
        applyButton.setFocusPainted(false);
        applyButton.setBorderPainted(false);
        applyButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Theme has been applied to all windows",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        JButton closeButton = new JButton("Close");
        closeButton.setPreferredSize(new Dimension(100, 38));
        closeButton.setFont(new Font("Dialog", Font.PLAIN, 14));
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(applyButton);
        buttonPanel.add(closeButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setResizable(false);
    }

    private JPanel createPreviewPanel() {
        JPanel previewPanel = new JPanel();
        previewPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        previewPanel.setMaximumSize(new Dimension(400, 50));

        // Preview color blocks
        JPanel lightPreview = new JPanel();
        lightPreview.setPreferredSize(new Dimension(60, 40));
        lightPreview.setBackground(Color.WHITE);
        lightPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JPanel darkPreview = new JPanel();
        darkPreview.setPreferredSize(new Dimension(60, 40));
        darkPreview.setBackground(new Color(43, 43, 43));
        darkPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JLabel arrowLabel = new JLabel("→");
        arrowLabel.setFont(new Font("Arial", Font.BOLD, 20));

        if (themeManager.isDarkMode()) {
            previewPanel.add(darkPreview);
            previewPanel.add(new JLabel("Currently Active"));
        } else {
            previewPanel.add(lightPreview);
            previewPanel.add(new JLabel("Currently Active"));
        }

        return previewPanel;
    }

    private void switchTheme() {
        boolean newDarkMode = themeToggle.isSelected();
        themeManager.setDarkMode(newDarkMode);
        themeStatusLabel.setText("Current Theme: " + themeManager.getCurrentThemeName());
        themeToggle.setText(newDarkMode ? "Dark" : "Light");

        // Update current dialog
        SwingUtilities.updateComponentTreeUI(this);

        // Recreate preview panel
        java.awt.Component[] components = ((JPanel)getContentPane().getComponent(0)).getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof JPanel && i == 8) {  // Preview panel position
                ((JPanel)getContentPane().getComponent(0)).remove(i);
                ((JPanel)getContentPane().getComponent(0)).add(createPreviewPanel(), i);
                break;
            }
        }
        revalidate();
        repaint();
    }

    /**
     * Update status before showing dialog
     */
    public void showDialog() {
        themeToggle.setSelected(themeManager.isDarkMode());
        themeToggle.setText(themeManager.isDarkMode() ? "Dark" : "Light");
        themeStatusLabel.setText("Current Theme: " + themeManager.getCurrentThemeName());
        setVisible(true);
    }
}