package com.nan.myschool.gui;

import com.nan.myschool.entity.Course;
import com.nan.myschool.entity.CourseSection;
import com.nan.myschool.entity.Enrollment;
import com.nan.myschool.entity.Trainer;
import com.nan.myschool.service.CourseSectionService;
import com.nan.myschool.service.CourseService;
import com.nan.myschool.service.EnrollmentsService;
import com.nan.myschool.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@Component
public class SectionMasterDetailGUI extends JFrame {
    private final CourseSectionService sectionService;
    private final CourseService courseService;
    private final TrainerService trainerService;
    private final EnrollmentsService enrollmentsService;

    private JTable sectionTable;
    private DefaultTableModel sectionTableModel;

    private JLabel sectionIdLabel = new JLabel("班级ID: ");
    private JLabel courseLabel = new JLabel("课程: ");
    private JLabel trainerLabel = new JLabel("训练师: ");
    private JLabel groundLabel = new JLabel("训练场地: ");
    private JLabel scheduleLabel = new JLabel("时间: ");
    private JLabel capacityLabel = new JLabel("容量: ");

    private JTable enrollmentTable;
    private DefaultTableModel enrollmentTableModel;

    private JButton addButton = new JButton("添加训练班");
    private JButton updateButton = new JButton("修改班级");
    private JButton deleteButton = new JButton("删除班级");
    private JButton refreshButton = new JButton("刷新");

    @Autowired
    public SectionMasterDetailGUI(CourseSectionService sectionService,
                                  CourseService courseService,
                                  TrainerService trainerService,
                                  EnrollmentsService enrollmentsService) {
        this.sectionService = sectionService;
        this.courseService = courseService;
        this.trainerService = trainerService;
        this.enrollmentsService = enrollmentsService;
        initializeGUI();
        loadSections();
    }

    private void initializeGUI() {
        setTitle("训练课程管理 - Master-Detail");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(1200, 700);
        setLayout(new BorderLayout(10, 10));

        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Master 面板
        JPanel masterPanel = new JPanel(new BorderLayout(5, 5));
        masterPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                "所有训练班级",
                0, 0,
                new Font("微软雅黑", Font.BOLD, 14),
                new Color(52, 152, 219)
        ));

        String[] columnNames = {"班级ID", "课程名称", "训练师", "训练场地", "时间", "容量"};
        sectionTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        sectionTable = new JTable(sectionTableModel);

        sectionTable.setRowHeight(30);
        sectionTable.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        sectionTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 13));
        sectionTable.getTableHeader().setBackground(new Color(52, 152, 219));
        sectionTable.getTableHeader().setForeground(Color.WHITE);
        sectionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sectionTable.setSelectionBackground(new Color(52, 152, 219, 50));
        sectionTable.setSelectionForeground(Color.BLACK);
        sectionTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                displaySectionDetails();
            }
        });

        JScrollPane masterScrollPane = new JScrollPane(sectionTable);
        masterPanel.add(masterScrollPane, BorderLayout.CENTER);

        // Detail 面板
        JPanel detailPanel = new JPanel(new BorderLayout(10, 10));
        detailPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
                "班级详细信息",
                0, 0,
                new Font("微软雅黑", Font.BOLD, 14),
                new Color(46, 204, 113)
        ));

        JPanel infoPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        Font labelFont = new Font("微软雅黑", Font.PLAIN, 14);
        sectionIdLabel.setFont(labelFont);
        courseLabel.setFont(labelFont);
        trainerLabel.setFont(labelFont);
        groundLabel.setFont(labelFont);
        scheduleLabel.setFont(labelFont);
        capacityLabel.setFont(labelFont);

        infoPanel.add(sectionIdLabel);
        infoPanel.add(courseLabel);
        infoPanel.add(trainerLabel);
        infoPanel.add(groundLabel);
        infoPanel.add(scheduleLabel);
        infoPanel.add(capacityLabel);

        detailPanel.add(infoPanel, BorderLayout.NORTH);

        // 报名宠物列表
        JPanel enrollmentPanel = new JPanel(new BorderLayout(5, 5));
        enrollmentPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
                "报名宠物列表",
                0, 0,
                new Font("微软雅黑", Font.BOLD, 13),
                new Color(155, 89, 182)
        ));

        String[] enrollmentColumns = {"报名ID", "宠物ID", "宠物名字", "评级"};
        enrollmentTableModel = new DefaultTableModel(enrollmentColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        enrollmentTable = new JTable(enrollmentTableModel);

        enrollmentTable.setRowHeight(28);
        enrollmentTable.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        enrollmentTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 12));
        enrollmentTable.getTableHeader().setBackground(new Color(155, 89, 182));
        enrollmentTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane enrollmentScrollPane = new JScrollPane(enrollmentTable);
        enrollmentPanel.add(enrollmentScrollPane, BorderLayout.CENTER);

        detailPanel.add(enrollmentPanel, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        styleButton(addButton, new Color(46, 204, 113));
        styleButton(updateButton, new Color(52, 152, 219));
        styleButton(deleteButton, new Color(231, 76, 60));
        styleButton(refreshButton, new Color(52, 73, 94));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        addButton.addActionListener(e -> addNewSection());
        updateButton.addActionListener(e -> updateSelectedSection());
        deleteButton.addActionListener(e -> deleteSelectedSection());
        refreshButton.addActionListener(e -> loadSections());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, masterPanel, detailPanel);
        splitPane.setDividerLocation(600);
        splitPane.setDividerSize(8);

        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(120, 40));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = backgroundColor;

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });
    }

    private void loadSections() {
        sectionTableModel.setRowCount(0);
        List<CourseSection> sections = sectionService.getAllSections();

        for (CourseSection section : sections) {
            String typeTag = getTrainingTypeTag(section.getCourse().getTrainingType());

            Object[] row = {
                    section.getSectionId(),
                    "[" + typeTag + "] " + section.getCourse().getTitle(),
                    section.getTrainer().getName(),
                    section.getTrainingGround(),
                    section.getSchedule(),
                    section.getCapacity()
            };
            sectionTableModel.addRow(row);
        }
    }

    // 添加辅助方法
    private String getTrainingTypeTag(String trainingType) {
        return switch (trainingType) {
            case "基础服从" -> "基础";
            case "敏捷训练" -> "敏捷";
            case "社交训练" -> "社交";
            case "行为矫正" -> "矫正";
            default -> "其他";
        };
    }

    private void displaySectionDetails() {
        int selectedRow = sectionTable.getSelectedRow();
        if (selectedRow == -1) {
            clearDetails();
            return;
        }

        Integer sectionId = (Integer) sectionTableModel.getValueAt(selectedRow, 0);
        CourseSection section = sectionService.getSectionById(sectionId);

        if (section != null) {
            sectionIdLabel.setText("班级ID: " + section.getSectionId());
            courseLabel.setText("课程: " + section.getCourse().getTitle());
            trainerLabel.setText("训练师: " + section.getTrainer().getName());
            groundLabel.setText("训练场地: " + section.getTrainingGround());
            scheduleLabel.setText("时间: " + section.getSchedule());
            capacityLabel.setText("容量: " + section.getCapacity());

            loadEnrollments(section);
        }
    }

    private void loadEnrollments(CourseSection section) {
        enrollmentTableModel.setRowCount(0);
        List<Enrollment> enrollments = enrollmentsService.getEnrollmentsBySection(section);
        for (Enrollment enrollment : enrollments) {
            Object[] row = {
                    enrollment.getEnrollmentId(),
                    enrollment.getPet().getPetId(),
                    enrollment.getPet().getName(),
                    enrollment.getRating() != null ? enrollment.getRating() : "未评级"
            };
            enrollmentTableModel.addRow(row);
        }
    }

    private void clearDetails() {
        sectionIdLabel.setText("班级ID: ");
        courseLabel.setText("课程: ");
        trainerLabel.setText("训练师: ");
        groundLabel.setText("训练场地: ");
        scheduleLabel.setText("时间: ");
        capacityLabel.setText("容量: ");
        enrollmentTableModel.setRowCount(0);
    }

    private void addNewSection() {
        JDialog dialog = new JDialog(this, "添加新训练班", true);
        dialog.setLayout(new GridLayout(6, 2, 5, 5));

        ((JPanel)dialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        List<Course> courses = courseService.getAllCourses();
        List<Trainer> trainers = trainerService.getAllTrainers();

        if (courses.isEmpty() || trainers.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "请先添加课程和训练师数据！",
                    "数据不足",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JComboBox<Course> courseCombo = new JComboBox<>(courses.toArray(new Course[0]));
        JComboBox<Trainer> trainerCombo = new JComboBox<>(trainers.toArray(new Trainer[0]));
        JTextField groundField = new JTextField();
        JTextField scheduleField = new JTextField();
        JTextField capacityField = new JTextField();

        dialog.add(new JLabel("课程:"));
        dialog.add(courseCombo);
        dialog.add(new JLabel("训练师:"));
        dialog.add(trainerCombo);
        dialog.add(new JLabel("训练场地:"));
        dialog.add(groundField);
        dialog.add(new JLabel("时间:"));
        dialog.add(scheduleField);
        dialog.add(new JLabel("容量:"));
        dialog.add(capacityField);

        JButton submitButton = new JButton("确定");
        submitButton.addActionListener(e -> {
            try {
                CourseSection newSection = new CourseSection();
                newSection.setCourse((Course) courseCombo.getSelectedItem());
                newSection.setTrainer((Trainer) trainerCombo.getSelectedItem());
                newSection.setTrainingGround(groundField.getText());
                newSection.setSchedule(scheduleField.getText());
                newSection.setCapacity(Integer.parseInt(capacityField.getText()));

                sectionService.saveSection(newSection);
                loadSections();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "容量必须是数字！", "输入错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(submitButton);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void updateSelectedSection() {
        int selectedRow = sectionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择一个班级！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer sectionId = (Integer) sectionTableModel.getValueAt(selectedRow, 0);
        CourseSection section = sectionService.getSectionById(sectionId);

        JDialog dialog = new JDialog(this, "修改班级信息", true);
        dialog.setLayout(new GridLayout(6, 2, 5, 5));

        ((JPanel)dialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        List<Course> courses = courseService.getAllCourses();
        List<Trainer> trainers = trainerService.getAllTrainers();

        JComboBox<Course> courseCombo = new JComboBox<>(courses.toArray(new Course[0]));
        courseCombo.setSelectedItem(section.getCourse());

        JComboBox<Trainer> trainerCombo = new JComboBox<>(trainers.toArray(new Trainer[0]));
        trainerCombo.setSelectedItem(section.getTrainer());

        JTextField groundField = new JTextField(section.getTrainingGround());
        JTextField scheduleField = new JTextField(section.getSchedule());
        JTextField capacityField = new JTextField(String.valueOf(section.getCapacity()));

        dialog.add(new JLabel("课程:"));
        dialog.add(courseCombo);
        dialog.add(new JLabel("训练师:"));
        dialog.add(trainerCombo);
        dialog.add(new JLabel("训练场地:"));
        dialog.add(groundField);
        dialog.add(new JLabel("时间:"));
        dialog.add(scheduleField);
        dialog.add(new JLabel("容量:"));
        dialog.add(capacityField);

        JButton submitButton = new JButton("保存");
        submitButton.addActionListener(e -> {
            try {
                section.setCourse((Course) courseCombo.getSelectedItem());
                section.setTrainer((Trainer) trainerCombo.getSelectedItem());
                section.setTrainingGround(groundField.getText());
                section.setSchedule(scheduleField.getText());
                section.setCapacity(Integer.parseInt(capacityField.getText()));

                sectionService.saveSection(section);
                loadSections();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "容量必须是数字！", "输入错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(submitButton);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void deleteSelectedSection() {
        int selectedRow = sectionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择一个班级！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer sectionId = (Integer) sectionTableModel.getValueAt(selectedRow, 0);
        String courseName = (String) sectionTableModel.getValueAt(selectedRow, 1);

        int option = JOptionPane.showConfirmDialog(this,
                "确定要删除班级 " + sectionId + " (" + courseName + ") 吗？",
                "确认删除",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            sectionService.deleteSection(sectionId);
            loadSections();
            clearDetails();
        }
    }
}