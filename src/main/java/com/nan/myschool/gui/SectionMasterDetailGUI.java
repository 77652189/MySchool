package com.nan.myschool.gui;

import com.nan.myschool.entity.Course;
import com.nan.myschool.entity.CourseSection;
import com.nan.myschool.entity.Enrollment;
import com.nan.myschool.entity.Instructor;
import com.nan.myschool.service.CourseSectionService;
import com.nan.myschool.service.CourseService;
import com.nan.myschool.service.EnrollmentsService;
import com.nan.myschool.service.InstructorService;
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
    private final InstructorService instructorService;
    private final EnrollmentsService enrollmentsService;

    // Master 部分 - 显示所有课程章节
    private JTable sectionTable;
    private DefaultTableModel sectionTableModel;

    // Detail 部分 - 显示选中章节的详细信息
    private JLabel sectionIdLabel = new JLabel("章节ID: ");
    private JLabel courseLabel = new JLabel("课程: ");
    private JLabel instructorLabel = new JLabel("教师: ");
    private JLabel roomLabel = new JLabel("教室: ");
    private JLabel scheduleLabel = new JLabel("时间: ");
    private JLabel capacityLabel = new JLabel("容量: ");

    // 显示该章节的选课学生列表
    private JTable enrollmentTable;
    private DefaultTableModel enrollmentTableModel;

    // 操作按钮
    private JButton addButton = new JButton("添加章节");
    private JButton updateButton = new JButton("修改章节");
    private JButton deleteButton = new JButton("删除章节");
    private JButton refreshButton = new JButton("刷新");

    @Autowired
    public SectionMasterDetailGUI(CourseSectionService sectionService,
                                  CourseService courseService,
                                  InstructorService instructorService,
                                  EnrollmentsService enrollmentsService) {
        this.sectionService = sectionService;
        this.courseService = courseService;
        this.instructorService = instructorService;
        this.enrollmentsService = enrollmentsService;
        initializeGUI();
        loadSections();
    }

    private void initializeGUI() {
        setTitle("课程章节管理系统 - Master-Detail");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLayout(new BorderLayout(10, 10));

        // Master 面板 - 章节列表
        JPanel masterPanel = new JPanel(new BorderLayout());
        masterPanel.setBorder(BorderFactory.createTitledBorder("所有课程章节"));

        String[] columnNames = {"章节ID", "课程名称", "教师", "教室", "时间", "容量"};
        sectionTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        sectionTable = new JTable(sectionTableModel);
        sectionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sectionTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                displaySectionDetails();
            }
        });

        JScrollPane masterScrollPane = new JScrollPane(sectionTable);
        masterPanel.add(masterScrollPane, BorderLayout.CENTER);

        // Detail 面板 - 章节详细信息
        JPanel detailPanel = new JPanel(new BorderLayout(10, 10));
        detailPanel.setBorder(BorderFactory.createTitledBorder("章节详细信息"));

        JPanel infoPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoPanel.add(sectionIdLabel);
        infoPanel.add(courseLabel);
        infoPanel.add(instructorLabel);
        infoPanel.add(roomLabel);
        infoPanel.add(scheduleLabel);
        infoPanel.add(capacityLabel);

        detailPanel.add(infoPanel, BorderLayout.NORTH);

        // 选课学生列表
        JPanel enrollmentPanel = new JPanel(new BorderLayout());
        enrollmentPanel.setBorder(BorderFactory.createTitledBorder("选课学生列表"));

        String[] enrollmentColumns = {"选课ID", "学生ID", "学生姓名", "成绩"};
        enrollmentTableModel = new DefaultTableModel(enrollmentColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        enrollmentTable = new JTable(enrollmentTableModel);
        JScrollPane enrollmentScrollPane = new JScrollPane(enrollmentTable);
        enrollmentPanel.add(enrollmentScrollPane, BorderLayout.CENTER);

        detailPanel.add(enrollmentPanel, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // 添加按钮事件
        addButton.addActionListener(e -> addNewSection());
        updateButton.addActionListener(e -> updateSelectedSection());
        deleteButton.addActionListener(e -> deleteSelectedSection());
        refreshButton.addActionListener(e -> loadSections());

        // 使用 JSplitPane 分割 Master 和 Detail 面板
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, masterPanel, detailPanel);
        splitPane.setDividerLocation(500);

        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private void loadSections() {
        sectionTableModel.setRowCount(0);
        List<CourseSection> sections = sectionService.getAllSections();
        for (CourseSection section : sections) {
            Object[] row = {
                    section.getSectionId(),
                    section.getCourse().getTitle(),
                    section.getInstructor().getName(),
                    section.getRoom(),
                    section.getSchedule(),
                    section.getCapacity()
            };
            sectionTableModel.addRow(row);
        }
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
            sectionIdLabel.setText("章节ID: " + section.getSectionId());
            courseLabel.setText("课程: " + section.getCourse().getTitle());
            instructorLabel.setText("教师: " + section.getInstructor().getName());
            roomLabel.setText("教室: " + section.getRoom());
            scheduleLabel.setText("时间: " + section.getSchedule());
            capacityLabel.setText("容量: " + section.getCapacity());

            // 加载选课学生
            loadEnrollments(section);
        }
    }

    private void loadEnrollments(CourseSection section) {
        enrollmentTableModel.setRowCount(0);
        List<Enrollment> enrollments = enrollmentsService.getEnrollmentsBySection(section);
        for (Enrollment enrollment : enrollments) {
            Object[] row = {
                    enrollment.getEnrollmentId(),
                    enrollment.getStudent().getStudentId(),
                    enrollment.getStudent().getName(),
                    enrollment.getGrade() != null ? enrollment.getGrade() : "未评分"
            };
            enrollmentTableModel.addRow(row);
        }
    }

    private void clearDetails() {
        sectionIdLabel.setText("章节ID: ");
        courseLabel.setText("课程: ");
        instructorLabel.setText("教师: ");
        roomLabel.setText("教室: ");
        scheduleLabel.setText("时间: ");
        capacityLabel.setText("容量: ");
        enrollmentTableModel.setRowCount(0);
    }

    private void addNewSection() {
        JDialog dialog = new JDialog(this, "添加新章节", true);
        dialog.setLayout(new GridLayout(6, 2, 5, 5));
        ((JPanel)dialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        List<Course> courses = courseService.getAllCourses();
        List<Instructor> instructors = instructorService.getAllInstructors();

        if (courses.isEmpty() || instructors.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "请先添加课程和教师数据！",
                    "数据不足",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JComboBox<Course> courseCombo = new JComboBox<>(courses.toArray(new Course[0]));
        JComboBox<Instructor> instructorCombo = new JComboBox<>(instructors.toArray(new Instructor[0]));
        JTextField roomField = new JTextField();
        JTextField scheduleField = new JTextField();
        JTextField capacityField = new JTextField();

        dialog.add(new JLabel("课程:"));
        dialog.add(courseCombo);
        dialog.add(new JLabel("教师:"));
        dialog.add(instructorCombo);
        dialog.add(new JLabel("教室:"));
        dialog.add(roomField);
        dialog.add(new JLabel("时间:"));
        dialog.add(scheduleField);
        dialog.add(new JLabel("容量:"));
        dialog.add(capacityField);

        JButton submitButton = new JButton("确定");
        submitButton.addActionListener(e -> {
            try {
                CourseSection newSection = new CourseSection();
                newSection.setCourse((Course) courseCombo.getSelectedItem());
                newSection.setInstructor((Instructor) instructorCombo.getSelectedItem());
                newSection.setRoom(roomField.getText());
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
            JOptionPane.showMessageDialog(this, "请先选择一个章节！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer sectionId = (Integer) sectionTableModel.getValueAt(selectedRow, 0);
        CourseSection section = sectionService.getSectionById(sectionId);

        JDialog dialog = new JDialog(this, "修改章节信息", true);
        dialog.setLayout(new GridLayout(6, 2, 5, 5));
        ((JPanel)dialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        List<Course> courses = courseService.getAllCourses();
        List<Instructor> instructors = instructorService.getAllInstructors();

        JComboBox<Course> courseCombo = new JComboBox<>(courses.toArray(new Course[0]));
        courseCombo.setSelectedItem(section.getCourse());

        JComboBox<Instructor> instructorCombo = new JComboBox<>(instructors.toArray(new Instructor[0]));
        instructorCombo.setSelectedItem(section.getInstructor());

        JTextField roomField = new JTextField(section.getRoom());
        JTextField scheduleField = new JTextField(section.getSchedule());
        JTextField capacityField = new JTextField(String.valueOf(section.getCapacity()));

        dialog.add(new JLabel("课程:"));
        dialog.add(courseCombo);
        dialog.add(new JLabel("教师:"));
        dialog.add(instructorCombo);
        dialog.add(new JLabel("教室:"));
        dialog.add(roomField);
        dialog.add(new JLabel("时间:"));
        dialog.add(scheduleField);
        dialog.add(new JLabel("容量:"));
        dialog.add(capacityField);

        JButton submitButton = new JButton("保存");
        submitButton.addActionListener(e -> {
            try {
                section.setCourse((Course) courseCombo.getSelectedItem());
                section.setInstructor((Instructor) instructorCombo.getSelectedItem());
                section.setRoom(roomField.getText());
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
            JOptionPane.showMessageDialog(this, "请先选择一个章节！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer sectionId = (Integer) sectionTableModel.getValueAt(selectedRow, 0);
        String courseName = (String) sectionTableModel.getValueAt(selectedRow, 1);

        int option = JOptionPane.showConfirmDialog(this,
                "确定要删除章节 " + sectionId + " (" + courseName + ") 吗？",
                "确认删除",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            sectionService.deleteSection(sectionId);
            loadSections();
            clearDetails();
        }
    }
}