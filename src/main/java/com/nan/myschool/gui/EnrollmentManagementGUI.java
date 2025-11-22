package com.nan.myschool.gui;

import com.nan.myschool.entity.CourseSection;
import com.nan.myschool.entity.Enrollment;
import com.nan.myschool.entity.Student;
import com.nan.myschool.service.CourseSectionService;
import com.nan.myschool.service.EnrollmentsService;
import com.nan.myschool.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@Component
public class EnrollmentManagementGUI extends JFrame {
    private final StudentService studentService;
    private final CourseSectionService sectionService;
    private final EnrollmentsService enrollmentsService;

    // 学生选择
    private JComboBox<Student> studentComboBox;

    // 可选课程表格（左侧）
    private JTable availableSectionsTable;
    private DefaultTableModel availableSectionsModel;

    // 已选课程表格（右侧）
    private JTable enrolledSectionsTable;
    private DefaultTableModel enrolledSectionsModel;

    // 按钮
    private JButton enrollButton = new JButton("选课 →");
    private JButton dropButton = new JButton("← 退课");
    private JButton refreshButton = new JButton("刷新");

    @Autowired
    public EnrollmentManagementGUI(StudentService studentService,
                                   CourseSectionService sectionService,
                                   EnrollmentsService enrollmentsService) {
        this.studentService = studentService;
        this.sectionService = sectionService;
        this.enrollmentsService = enrollmentsService;
        initializeGUI();
        loadData();
    }

    private void initializeGUI() {
        setTitle("选课管理系统");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(1200, 600);
        setLayout(new BorderLayout(10, 10));

        // 顶部学生选择面板
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        topPanel.add(new JLabel("选择学生："));
        studentComboBox = new JComboBox<>();
        studentComboBox.setPreferredSize(new Dimension(200, 25));
        studentComboBox.addActionListener(e -> loadStudentEnrollments());
        topPanel.add(studentComboBox);

        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(refreshButton);
        refreshButton.addActionListener(e -> loadData());

        // 左侧：可选课程
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("可选课程"));

        String[] availableColumns = {"章节ID", "课程名称", "教师", "教室", "时间", "容量", "已选/总数"};
        availableSectionsModel = new DefaultTableModel(availableColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        availableSectionsTable = new JTable(availableSectionsModel);
        availableSectionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane leftScrollPane = new JScrollPane(availableSectionsTable);
        leftPanel.add(leftScrollPane, BorderLayout.CENTER);

        // 右侧：已选课程
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("已选课程"));

        String[] enrolledColumns = {"选课ID", "章节ID", "课程名称", "教师", "教室", "时间", "成绩"};
        enrolledSectionsModel = new DefaultTableModel(enrolledColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        enrolledSectionsTable = new JTable(enrolledSectionsModel);
        enrolledSectionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane rightScrollPane = new JScrollPane(enrolledSectionsTable);
        rightPanel.add(rightScrollPane, BorderLayout.CENTER);

        // 中间按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(200, 10, 10, 10));
        enrollButton.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        dropButton.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        enrollButton.setPreferredSize(new Dimension(120, 40));
        dropButton.setPreferredSize(new Dimension(120, 40));

        buttonPanel.add(enrollButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(dropButton);

        // 按钮事件
        enrollButton.addActionListener(e -> enrollInSection());
        dropButton.addActionListener(e -> dropSection());

        // 主面板使用水平分割
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
        // 加载所有学生到下拉框
        studentComboBox.removeAllItems();
        List<Student> students = studentService.getAllStudents();
        for (Student student : students) {
            studentComboBox.addItem(student);
        }

        if (!students.isEmpty()) {
            loadAvailableSections();
            loadStudentEnrollments();
        }
    }

    private void loadAvailableSections() {
        availableSectionsModel.setRowCount(0);
        List<CourseSection> sections = sectionService.getAllSections();

        for (CourseSection section : sections) {
            long enrolledCount = enrollmentsService.getEnrollmentCount(section);
            Object[] row = {
                    section.getSectionId(),
                    section.getCourse().getTitle(),
                    section.getInstructor().getName(),
                    section.getRoom(),
                    section.getSchedule(),
                    section.getCapacity(),
                    enrolledCount + " / " + section.getCapacity()
            };
            availableSectionsModel.addRow(row);
        }
    }

    private void loadStudentEnrollments() {
        enrolledSectionsModel.setRowCount(0);

        Student selectedStudent = (Student) studentComboBox.getSelectedItem();
        if (selectedStudent == null) return;

        List<Enrollment> enrollments = enrollmentsService.getEnrollmentsByStudent(selectedStudent);
        for (Enrollment enrollment : enrollments) {
            CourseSection section = enrollment.getCourseSection();
            Object[] row = {
                    enrollment.getEnrollmentId(),
                    section.getSectionId(),
                    section.getCourse().getTitle(),
                    section.getInstructor().getName(),
                    section.getRoom(),
                    section.getSchedule(),
                    enrollment.getGrade() != null ? enrollment.getGrade() : "未评分"
            };
            enrolledSectionsModel.addRow(row);
        }
    }

    private void enrollInSection() {
        Student selectedStudent = (Student) studentComboBox.getSelectedItem();
        if (selectedStudent == null) {
            JOptionPane.showMessageDialog(this, "请先选择学生！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int selectedRow = availableSectionsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要选的课程！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer sectionId = (Integer) availableSectionsModel.getValueAt(selectedRow, 0);
        CourseSection section = sectionService.getSectionById(sectionId);

        // 检查是否已选
        if (enrollmentsService.isStudentEnrolled(selectedStudent, section)) {
            JOptionPane.showMessageDialog(this, "该学生已选过这门课程！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 检查容量
        long enrolledCount = enrollmentsService.getEnrollmentCount(section);
        if (enrolledCount >= section.getCapacity()) {
            JOptionPane.showMessageDialog(this, "该课程已满，无法选课！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 创建选课记录
        Enrollment newEnrollment = new Enrollment();
        newEnrollment.setStudent(selectedStudent);
        newEnrollment.setCourseSection(section);
        newEnrollment.setGrade(null); // 初始成绩为空

        enrollmentsService.saveEnrollment(newEnrollment);

        JOptionPane.showMessageDialog(this, "选课成功！", "成功", JOptionPane.INFORMATION_MESSAGE);

        // 刷新数据
        loadAvailableSections();
        loadStudentEnrollments();
    }

    private void dropSection() {
        int selectedRow = enrolledSectionsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要退的课程！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer enrollmentId = (Integer) enrolledSectionsModel.getValueAt(selectedRow, 0);
        String courseName = (String) enrolledSectionsModel.getValueAt(selectedRow, 2);

        int option = JOptionPane.showConfirmDialog(this,
                "确定要退选 " + courseName + " 吗？",
                "确认退课",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            enrollmentsService.deleteEnrollment(enrollmentId);
            JOptionPane.showMessageDialog(this, "退课成功！", "成功", JOptionPane.INFORMATION_MESSAGE);

            // 刷新数据
            loadAvailableSections();
            loadStudentEnrollments();
        }
    }
}