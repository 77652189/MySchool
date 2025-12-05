package com.nan.myschool.gui;

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
    private final PetProfileGUI petProfileGUI;  // 新增
    private final AchievementWallGUI achievementWallGUI;  // 新增

    @Autowired
    public MainMenuGUI(UserViewerGUI userViewerGUI,
                       SectionMasterDetailGUI sectionMasterDetailGUI,
                       EnrollmentManagementGUI enrollmentManagementGUI,
                       ThemeSettingsDialog themeSettingsDialog,
                       ThemeManager themeManager,
                       PetProfileGUI petProfileGUI,  // 新增
                       AchievementWallGUI achievementWallGUI) {  // 新增
        this.userViewerGUI = userViewerGUI;
        this.sectionMasterDetailGUI = sectionMasterDetailGUI;
        this.enrollmentManagementGUI = enrollmentManagementGUI;
        this.themeSettingsDialog = themeSettingsDialog;
        this.themeManager = themeManager;
        this.petProfileGUI = petProfileGUI;  // 新增
        this.achievementWallGUI = achievementWallGUI;  // 新增
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("🐾 宠物训练学校管理系统");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 620);
        setLayout(new BorderLayout(0, 0));

        JPanel headerPanel = createHeaderPanel();
        JPanel centerPanel = createCenterPanel();
        JPanel footerPanel = createFooterPanel();

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
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

        JButton themeBtn = new JButton("切换主题");
        themeBtn.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        themeBtn.setPreferredSize(new Dimension(90, 32));
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
        title1.setFont(new Font("微软雅黑", Font.BOLD, 38));
        title1.setForeground(Color.WHITE);
        title1.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        JLabel title2 = new JLabel("宠物训练学校管理系统");
        title2.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        title2.setForeground(Color.WHITE);
        title2.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        centerArea.add(Box.createVerticalStrut(25));
        centerArea.add(title1);
        centerArea.add(Box.createVerticalStrut(10));
        centerArea.add(title2);
        centerArea.add(Box.createVerticalStrut(25));

        container.add(topBar, BorderLayout.NORTH);
        container.add(centerArea, BorderLayout.CENTER);

        return container;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBorder(new EmptyBorder(30, 60, 30, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);

        // 创建菜单按钮
        JButton petProfileButton = createStyledButton("宠物档案", new Color(52, 152, 219), "PETS");  // 新增
        JButton trainingButton = createStyledButton("训练课程管理", new Color(46, 204, 113), "TRAINING");
        JButton enrollmentButton = createStyledButton("报名管理", new Color(155, 89, 182), "ENROLL");
        JButton achievementButton = createStyledButton("成就墙", new Color(241, 196, 15), "AWARDS");  // 新增
        JButton userButton = createStyledButton("用户管理", new Color(149, 165, 166), "USERS");
        JButton themeSettingsButton = createStyledButton("主题设置", new Color(243, 156, 18), "THEME");
        JButton aboutButton = createStyledButton("关于系统", new Color(52, 73, 94), "ABOUT");
        JButton exitButton = createStyledButton("退出系统", new Color(231, 76, 60), "EXIT");

        // 添加按钮事件
        petProfileButton.addActionListener(e -> petProfileGUI.setVisible(true));  // 新增
        trainingButton.addActionListener(e -> openTrainingManagement());
        enrollmentButton.addActionListener(e -> openEnrollmentManagement());
        achievementButton.addActionListener(e -> achievementWallGUI.setVisible(true));  // 新增
        userButton.addActionListener(e -> openUserManagement());
        themeSettingsButton.addActionListener(e -> openThemeSettings());
        aboutButton.addActionListener(e -> showAbout());
        exitButton.addActionListener(e -> exitApplication());

        // 添加按钮到面板
        gbc.gridy = 0; centerPanel.add(petProfileButton, gbc);  // 新增
        gbc.gridy = 1; centerPanel.add(trainingButton, gbc);
        gbc.gridy = 2; centerPanel.add(enrollmentButton, gbc);
        gbc.gridy = 3; centerPanel.add(achievementButton, gbc);  // 新增
        gbc.gridy = 4; centerPanel.add(userButton, gbc);
        gbc.gridy = 5; centerPanel.add(themeSettingsButton, gbc);
        gbc.gridy = 6; centerPanel.add(aboutButton, gbc);
        gbc.gridy = 7; gbc.insets = new Insets(15, 0, 0, 0);
        centerPanel.add(exitButton, gbc);

        return centerPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(new EmptyBorder(12, 0, 12, 0));

        JLabel footerLabel = new JLabel("© 2025 Pet Training School System | Developed by Nan");
        footerLabel.setFont(new Font("微软雅黑", Font.PLAIN, 11));
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
        textLabel.setFont(new Font("微软雅黑", Font.PLAIN, 17));
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

    private void openTrainingManagement() { sectionMasterDetailGUI.setVisible(true); }
    private void openEnrollmentManagement() { enrollmentManagementGUI.setVisible(true); }
    private void openUserManagement() { userViewerGUI.setVisible(true); }
    private void openThemeSettings() { themeSettingsDialog.showDialog(); }

    private void showAbout() {
        String message = "🐾 宠物训练学校管理系统 v1.0\n\n" +
                "专业的宠物训练与管理平台\n\n" +
                "功能模块:\n" +
                "• 🐾 宠物档案管理\n" +
                "• 📚 训练课程管理\n" +
                "• ✏️ 报名管理\n" +
                "• 🏆 成就墙展示\n" +
                "• 👥 用户管理\n" +
                "• 🎨 主题切换\n\n" +
                "我们的使命:\n" +
                "帮助每一只宠物成为最好的自己！\n\n" +
                "开发者: Nan\n" +
                "学校: Northeastern University\n" +
                "版本日期: 2025";
        JOptionPane.showMessageDialog(this, message, "关于系统", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exitApplication() {
        int option = JOptionPane.showConfirmDialog(this, "确定要退出系统吗？", "退出确认",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}