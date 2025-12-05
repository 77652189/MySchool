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
        loadAchievements();  // 自动加载
    }

    private void initializeGUI() {
        setTitle("训练成就墙");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(1000, 700);
        setLayout(new BorderLayout(15, 15));

        ((JPanel)getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));

        // 标题
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(241, 196, 15));
        headerPanel.setPreferredSize(new Dimension(1000, 80));

        JLabel titleLabel = new JLabel("训练成就墙", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        JButton refreshBtn = new JButton("刷新数据");
        refreshBtn.setFont(new Font("微软雅黑", Font.PLAIN, 13));
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

        // 主内容区域
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 15, 15));

        excellentArea = new JTextArea();
        graduatesArea = new JTextArea();
        activeArea = new JTextArea();

        JPanel excellentPanel = createAchievementPanel("[★] 优秀学员", excellentArea, new Color(241, 196, 15));
        JPanel graduatesPanel = createAchievementPanel("[√] 毕业学员", graduatesArea, new Color(46, 204, 113));
        JPanel activePanel = createAchievementPanel("[>] 活跃学员", activeArea, new Color(52, 152, 219));
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
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        titleLabel.setForeground(themeColor);

        textArea.setEditable(false);
        textArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText("加载中...");

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

        JLabel titleLabel = new JLabel("[#] 系统统计");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        titleLabel.setForeground(new Color(155, 89, 182));
        titleLabel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));

        return panel;
    }

    private void loadAchievements() {
        List<Pet> allPets = petService.getAllPets();
        List<Enrollment> allEnrollments = enrollmentsService.getAllEnrollments();

        // 优秀学员
        List<String> excellentPets = allEnrollments.stream()
                .filter(e -> "Excellent".equals(e.getRating()))
                .map(e -> "[★] " + e.getPet().getName() + " (" + e.getPet().getBreed() + ")\n    课程: " +
                        e.getCourseSection().getCourse().getTitle())
                .distinct()
                .collect(Collectors.toList());

        // 毕业学员
        List<String> graduates = allEnrollments.stream()
                .filter(e -> "Completed".equals(e.getStatus()))
                .map(e -> "[√] " + e.getPet().getName() + " - " +
                        e.getCourseSection().getCourse().getTitle() +
                        (e.getRating() != null ? " [" + e.getRating() + "]" : ""))
                .collect(Collectors.toList());

        // 活跃学员
        Map<String, Long> petActivityCount = allEnrollments.stream()
                .filter(e -> "InProgress".equals(e.getStatus()))
                .collect(Collectors.groupingBy(
                        e -> e.getPet().getName() + " (" + e.getPet().getBreed() + ")",
                        Collectors.counting()
                ));

        List<String> activePets = petActivityCount.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(e -> "[>] " + e.getKey() + " - 正在训练 " + e.getValue() + " 门课程")
                .collect(Collectors.toList());

        // 填充数据
        excellentArea.setText(excellentPets.isEmpty() ? "暂无优秀学员" : String.join("\n\n", excellentPets));
        graduatesArea.setText(graduates.isEmpty() ? "暂无毕业学员" : String.join("\n\n", graduates));
        activeArea.setText(activePets.isEmpty() ? "暂无活跃学员" : String.join("\n\n", activePets));

        updateStatistics(allPets, allEnrollments);
    }

    private void updateStatistics(List<Pet> pets, List<Enrollment> enrollments) {
        // 清除旧统计
        java.awt.Component[] components = statsPanel.getComponents();
        for (int i = components.length - 1; i > 0; i--) {
            statsPanel.remove(components[i]);
        }

        // 计算统计数据
        long completedCount = enrollments.stream()
                .filter(e -> "Completed".equals(e.getStatus()))
                .count();

        long inProgressCount = enrollments.stream()
                .filter(e -> "InProgress".equals(e.getStatus()))
                .count();

        long excellentCount = enrollments.stream()
                .filter(e -> "Excellent".equals(e.getRating()))
                .count();

        // 物种统计
        Map<String, Long> speciesCount = pets.stream()
                .collect(Collectors.groupingBy(Pet::getSpecies, Collectors.counting()));

        // 添加统计标签
        addStatLabel(statsPanel, "注册宠物总数", String.valueOf(pets.size()), new Color(52, 152, 219));
        addStatLabel(statsPanel, "总报名数", String.valueOf(enrollments.size()), new Color(46, 204, 113));
        addStatLabel(statsPanel, "已完成课程", String.valueOf(completedCount), new Color(26, 188, 156));
        addStatLabel(statsPanel, "进行中课程", String.valueOf(inProgressCount), new Color(52, 152, 219));
        addStatLabel(statsPanel, "优秀评级数", String.valueOf(excellentCount), new Color(241, 196, 15));

        statsPanel.add(Box.createVerticalStrut(15));
        statsPanel.add(createSeparator());
        statsPanel.add(Box.createVerticalStrut(10));

        // 物种分布
        for (Map.Entry<String, Long> entry : speciesCount.entrySet()) {
            addStatLabel(statsPanel, entry.getKey() + " 数量", String.valueOf(entry.getValue()), new Color(155, 89, 182));
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
        labelText.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        JLabel valueText = new JLabel(value);
        valueText.setFont(new Font("微软雅黑", Font.BOLD, 20));
        valueText.setForeground(color);

        statRow.add(labelText, BorderLayout.WEST);
        statRow.add(valueText, BorderLayout.EAST);

        panel.add(statRow);
        panel.add(Box.createVerticalStrut(8));
    }
}