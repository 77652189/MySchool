package com.nan.myschool.gui;

import com.nan.myschool.entity.CourseSection;
import com.nan.myschool.entity.Enrollment;
import com.nan.myschool.entity.Pet;
import com.nan.myschool.service.CourseSectionService;
import com.nan.myschool.service.EnrollmentsService;
import com.nan.myschool.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@Component
public class EnrollmentManagementGUI extends JFrame {
    private final PetService petService;
    private final CourseSectionService sectionService;
    private final EnrollmentsService enrollmentsService;

    private JComboBox<Pet> petComboBox;

    private JTable availableSectionsTable;
    private DefaultTableModel availableSectionsModel;

    private JTable enrolledSectionsTable;
    private DefaultTableModel enrolledSectionsModel;

    private JButton enrollButton = new JButton("Enroll →");
    private JButton dropButton = new JButton("← Drop");
    private JButton refreshButton = new JButton("Refresh");

    @Autowired
    public EnrollmentManagementGUI(PetService petService,
                                   CourseSectionService sectionService,
                                   EnrollmentsService enrollmentsService) {
        this.petService = petService;
        this.sectionService = sectionService;
        this.enrollmentsService = enrollmentsService;
        initializeGUI();
        loadData();
    }

    private void initializeGUI() {
        setTitle("Enrollment Management System");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(1200, 600);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        topPanel.add(new JLabel("Select Pet:"));
        petComboBox = new JComboBox<>();
        petComboBox.setPreferredSize(new Dimension(250, 25));
        petComboBox.addActionListener(e -> loadPetEnrollments());
        topPanel.add(petComboBox);

        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(refreshButton);
        refreshButton.addActionListener(e -> loadData());

        // Left panel: Available courses
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Available Training Courses"));

        String[] availableColumns = {"Section ID", "Course Name", "Trainer", "Venue", "Schedule", "Capacity", "Enrolled/Total"};
        availableSectionsModel = new DefaultTableModel(availableColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        availableSectionsTable = new JTable(availableSectionsModel);
        availableSectionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        availableSectionsTable.setRowHeight(28);
        availableSectionsTable.setFont(new Font("Dialog", Font.PLAIN, 13));

        JScrollPane leftScrollPane = new JScrollPane(availableSectionsTable);
        leftPanel.add(leftScrollPane, BorderLayout.CENTER);

        // Right panel: Enrolled courses
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Enrolled Courses"));

        String[] enrolledColumns = {"Enrollment ID", "Section ID", "Course Name", "Trainer", "Venue", "Schedule", "Rating"};
        enrolledSectionsModel = new DefaultTableModel(enrolledColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        enrolledSectionsTable = new JTable(enrolledSectionsModel);
        enrolledSectionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        enrolledSectionsTable.setRowHeight(28);
        enrolledSectionsTable.setFont(new Font("Dialog", Font.PLAIN, 13));

        JScrollPane rightScrollPane = new JScrollPane(enrolledSectionsTable);
        rightPanel.add(rightScrollPane, BorderLayout.CENTER);

        // Middle buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(200, 10, 10, 10));

        enrollButton.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        dropButton.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        enrollButton.setPreferredSize(new Dimension(120, 40));
        dropButton.setPreferredSize(new Dimension(120, 40));

        enrollButton.setMaximumSize(new Dimension(120, 40));
        dropButton.setMaximumSize(new Dimension(120, 40));

        buttonPanel.add(enrollButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(dropButton);

        enrollButton.addActionListener(e -> enrollInSection());
        dropButton.addActionListener(e -> dropSection());

        JPanel centerPanel = new JPanel(new BorderLayout());
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(700);

        centerPanel.add(splitPane, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }

    private void loadData() {
        petComboBox.removeAllItems();
        List<Pet> pets = petService.getAllPets();
        for (Pet pet : pets) {
            petComboBox.addItem(pet);
        }

        if (!pets.isEmpty()) {
            loadAvailableSections();
            loadPetEnrollments();
        }
    }

    private void loadAvailableSections() {
        availableSectionsModel.setRowCount(0);
        List<CourseSection> sections = sectionService.getAllSections();

        for (CourseSection section : sections) {
            long enrolledCount = enrollmentsService.getEnrollmentCount(section);

            String typeTag = getTrainingTypeTag(section.getCourse().getTrainingType());

            Object[] row = {
                    section.getSectionId(),
                    "[" + typeTag + "] " + section.getCourse().getTitle(),
                    section.getTrainer().getName(),
                    section.getTrainingGround(),
                    section.getSchedule(),
                    section.getCapacity(),
                    enrolledCount + " / " + section.getCapacity()
            };
            availableSectionsModel.addRow(row);
        }
    }

    private void loadPetEnrollments() {
        enrolledSectionsModel.setRowCount(0);

        Pet selectedPet = (Pet) petComboBox.getSelectedItem();
        if (selectedPet == null) return;

        List<Enrollment> enrollments = enrollmentsService.getEnrollmentsByPet(selectedPet);
        for (Enrollment enrollment : enrollments) {
            CourseSection section = enrollment.getCourseSection();

            String ratingText = getRatingText(enrollment.getRating());

            Object[] row = {
                    enrollment.getEnrollmentId(),
                    section.getSectionId(),
                    section.getCourse().getTitle(),
                    section.getTrainer().getName(),
                    section.getTrainingGround(),
                    section.getSchedule(),
                    ratingText
            };
            enrolledSectionsModel.addRow(row);
        }
    }

    private void enrollInSection() {
        Pet selectedPet = (Pet) petComboBox.getSelectedItem();
        if (selectedPet == null) {
            JOptionPane.showMessageDialog(this, "Please select a pet first!", "Notice", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int selectedRow = availableSectionsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to enroll!", "Notice", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer sectionId = (Integer) availableSectionsModel.getValueAt(selectedRow, 0);
        CourseSection section = sectionService.getSectionById(sectionId);

        if (enrollmentsService.isPetEnrolled(selectedPet, section)) {
            JOptionPane.showMessageDialog(this, "This pet is already enrolled in this course!", "Notice", JOptionPane.WARNING_MESSAGE);
            return;
        }

        long enrolledCount = enrollmentsService.getEnrollmentCount(section);
        if (enrolledCount >= section.getCapacity()) {
            JOptionPane.showMessageDialog(this, "This course is full and cannot accept more enrollments!", "Notice", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Enrollment newEnrollment = new Enrollment();
        newEnrollment.setPet(selectedPet);
        newEnrollment.setCourseSection(section);
        newEnrollment.setRating(null);
        newEnrollment.setStatus("InProgress");
        newEnrollment.setProgressNotes("Just started training");

        enrollmentsService.saveEnrollment(newEnrollment);

        JOptionPane.showMessageDialog(this, "Enrollment successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

        loadAvailableSections();
        loadPetEnrollments();
    }

    private void dropSection() {
        int selectedRow = enrolledSectionsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to drop!", "Notice", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer enrollmentId = (Integer) enrolledSectionsModel.getValueAt(selectedRow, 0);
        String courseName = (String) enrolledSectionsModel.getValueAt(selectedRow, 2);

        int option = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to drop " + courseName + "?",
                "Confirm Drop",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            enrollmentsService.deleteEnrollment(enrollmentId);
            JOptionPane.showMessageDialog(this, "Course dropped successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            loadAvailableSections();
            loadPetEnrollments();
        }
    }

    // Helper methods
    private String getTrainingTypeTag(String trainingType) {
        // Support both Chinese and English training types for backward compatibility
        return switch (trainingType) {
            case "Basic Obedience", "基础服从" -> "Basic";
            case "Agility Training", "敏捷训练" -> "Agility";
            case "Social Training", "社交训练" -> "Social";
            case "Behavior Correction", "行为矫正" -> "Correction";
            default -> "Other";
        };
    }

    private String getRatingText(String rating) {
        if (rating == null) return "[ ] Not Rated";
        return switch (rating) {
            case "Excellent" -> "[★★★] " + rating;
            case "Good" -> "[★★] " + rating;
            case "Fair" -> "[★] " + rating;
            case "NeedsWork" -> "[!] " + rating;
            default -> rating;
        };
    }
}