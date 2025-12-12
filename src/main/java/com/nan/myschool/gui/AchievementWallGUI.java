package com.nan.myschool.gui;

import com.nan.myschool.entity.Enrollment;
import com.nan.myschool.entity.Pet;
import com.nan.myschool.service.EnrollmentsService;
import com.nan.myschool.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AchievementWallGUI extends JFrame {
    private final PetService petService;
    private final EnrollmentsService enrollmentsService;

    private JTextArea excellentArea;
    private JTextArea graduatesArea;
    private JTextArea activeArea;
    private JPanel statsPanel;

    @Autowired
    public AchievementWallGUI(PetService petService, EnrollmentsService enrollmentsService) {
        this.petService = petService;
        this.enrollmentsService = enrollmentsService;
        initializeGUI();
        loadAchievements();  // Auto load
    }

    private void initializeGUI() {
        setTitle("Training Achievement Wall");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(1000, 700);
        setLayout(new BorderLayout(15, 15));

        ((JPanel)getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(241, 196, 15));
        headerPanel.setPreferredSize(new Dimension(1000, 80));

        JLabel titleLabel = new JLabel("Training Achievement Wall", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setFont(new Font("Microsoft YaHei", Font.PLAIN, 13));
        refreshBtn.setBackground(new Color(255, 255, 255, 40));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> loadAchievements());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 25));
        btnPanel.setOpaque(false);
        btnPanel.add(refreshBtn);

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(btnPanel, BorderLayout.EAST);

        // Main content area
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 15, 15));

        excellentArea = new JTextArea();
        graduatesArea = new JTextArea();
        activeArea = new JTextArea();

        JPanel excellentPanel = createAchievementPanel("[★] Excellent Pets", excellentArea, new Color(241, 196, 15));
        JPanel graduatesPanel = createAchievementPanel("[√] Graduated Pets", graduatesArea, new Color(46, 204, 113));
        JPanel activePanel = createAchievementPanel("[>] Active Pets", activeArea, new Color(52, 152, 219));
        statsPanel = createStatisticsPanel();

        contentPanel.add(excellentPanel);
        contentPanel.add(graduatesPanel);
        contentPanel.add(activePanel);
        contentPanel.add(statsPanel);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }

    private JPanel createAchievementPanel(String title, JTextArea textArea, Color themeColor) {
        JPanel panel = new JPanel(new BorderLayout(5, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeColor, 2),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 18));
        titleLabel.setForeground(themeColor);

        textArea.setEditable(false);
        textArea.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText("Loading...");

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel("[#] System Statistics");
        titleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 18));
        titleLabel.setForeground(new Color(155, 89, 182));
        titleLabel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));

        return panel;
    }

    private void loadAchievements() {
        List<Pet> allPets = petService.getAllPets();
        List<Enrollment> allEnrollments = enrollmentsService.getAllEnrollments();

        // Excellent Pets
        List<String> excellentPets = allEnrollments.stream()
                .filter(e -> "Excellent".equals(e.getRating()))
                .map(e -> "[★] " + e.getPet().getName() + " (" + e.getPet().getBreed() + ")\n    Course: " +
                        e.getCourseSection().getCourse().getTitle())
                .distinct()
                .collect(Collectors.toList());

        // Graduated Pets
        List<String> graduates = allEnrollments.stream()
                .filter(e -> "Completed".equals(e.getStatus()))
                .map(e -> "[√] " + e.getPet().getName() + " - " +
                        e.getCourseSection().getCourse().getTitle() +
                        (e.getRating() != null ? " [" + e.getRating() + "]" : ""))
                .collect(Collectors.toList());

        // Active Pets
        Map<String, Long> petActivityCount = allEnrollments.stream()
                .filter(e -> "InProgress".equals(e.getStatus()))
                .collect(Collectors.groupingBy(
                        e -> e.getPet().getName() + " (" + e.getPet().getBreed() + ")",
                        Collectors.counting()
                ));

        List<String> activePets = petActivityCount.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(e -> "[>] " + e.getKey() + " - Training in " + e.getValue() + " course(s)")
                .collect(Collectors.toList());

        // Fill data
        excellentArea.setText(excellentPets.isEmpty() ? "No excellent pets" : String.join("\n\n", excellentPets));
        graduatesArea.setText(graduates.isEmpty() ? "No graduates" : String.join("\n\n", graduates));
        activeArea.setText(activePets.isEmpty() ? "No active pets" : String.join("\n\n", activePets));

        updateStatistics(allPets, allEnrollments);
    }

    private void updateStatistics(List<Pet> pets, List<Enrollment> enrollments) {
        // Clear old stats
        java.awt.Component[] components = statsPanel.getComponents();
        for (int i = components.length - 1; i > 0; i--) {
            statsPanel.remove(components[i]);
        }

        // Statistics
        long completedCount = enrollments.stream()
                .filter(e -> "Completed".equals(e.getStatus()))
                .count();

        long inProgressCount = enrollments.stream()
                .filter(e -> "InProgress".equals(e.getStatus()))
                .count();

        long excellentCount = enrollments.stream()
                .filter(e -> "Excellent".equals(e.getRating()))
                .count();

        // Species distribution
        Map<String, Long> speciesCount = pets.stream()
                .collect(Collectors.groupingBy(Pet::getSpecies, Collectors.counting()));

        // Add stat labels
        addStatLabel(statsPanel, "Total Registered Pets", String.valueOf(pets.size()), new Color(52, 152, 219));
        addStatLabel(statsPanel, "Total Enrollments", String.valueOf(enrollments.size()), new Color(46, 204, 113));
        addStatLabel(statsPanel, "Completed Courses", String.valueOf(completedCount), new Color(26, 188, 156));
        addStatLabel(statsPanel, "In-progress Courses", String.valueOf(inProgressCount), new Color(52, 152, 219));
        addStatLabel(statsPanel, "Excellent Ratings", String.valueOf(excellentCount), new Color(241, 196, 15));

        statsPanel.add(Box.createVerticalStrut(15));
        statsPanel.add(createSeparator());
        statsPanel.add(Box.createVerticalStrut(10));

        // Species stats
        for (Map.Entry<String, Long> entry : speciesCount.entrySet()) {
            addStatLabel(statsPanel, entry.getKey() + " Count", String.valueOf(entry.getValue()), new Color(155, 89, 182));
        }

        statsPanel.revalidate();
        statsPanel.repaint();
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(400, 1));
        separator.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        return separator;
    }

    private void addStatLabel(JPanel panel, String label, String value, Color color) {
        JPanel statRow = new JPanel(new BorderLayout(10, 5));
        statRow.setMaximumSize(new Dimension(400, 40));
        statRow.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));

        JLabel valueText = new JLabel(value);
        valueText.setFont(new Font("Microsoft YaHei", Font.BOLD, 20));
        valueText.setForeground(color);

        statRow.add(labelText, BorderLayout.WEST);
        statRow.add(valueText, BorderLayout.EAST);

        panel.add(statRow);
        panel.add(Box.createVerticalStrut(8));
    }
}
