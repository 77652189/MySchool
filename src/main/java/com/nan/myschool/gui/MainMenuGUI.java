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
    private SimpleLoginGUI loginGUI;  // 新增：保存登录界面引用

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

    // 新增：设置登录界面引用
    public void setLoginGUI(SimpleLoginGUI loginGUI) {
        this.loginGUI = loginGUI;
    }

    private void initializeGUI() {
        setTitle("宠物训练学校管理系统");
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

        userInfoLabel = new JLabel(getUserInfoText());
        userInfoLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
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
            return "未登录 - 请登录以访问完整功能";
        }

        String username = sessionManager.getCurrentUser().getUsername();
        String role = getRoleDisplayName(sessionManager.getCurrentRole());
        return "当前用户: " + username + " (" + role + ")";
    }

    private String getRoleDisplayName(String role) {
        return switch (role) {
            case "Admin" -> "管理员";
            case "Trainer" -> "训练师";
            case "PetOwner" -> "宠物主人";
            default -> "未知";
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

        // 如果未登录，只显示登录按钮
        if (!sessionManager.isLoggedIn()) {
            JButton loginButton = createStyledButton("点击登录", new Color(46, 204, 113), "LOGIN");
            loginButton.addActionListener(e -> returnToLogin());
            gbc.gridy = rowIndex++;
            centerPanel.add(loginButton, gbc);

            JButton exitButton = createStyledButton("退出系统", new Color(231, 76, 60), "EXIT");
            exitButton.addActionListener(e -> exitApplication());
            gbc.gridy = rowIndex++;
            gbc.insets = new Insets(15, 0, 0, 0);
            centerPanel.add(exitButton, gbc);

            return centerPanel;
        }

        // 已登录：根据角色显示功能

        // 宠物档案 - PetOwner, Admin
        if ("PetOwner".equals(role) || "Admin".equals(role)) {
            JButton petProfileButton = createStyledButton("宠物档案", new Color(52, 152, 219), "PETS");
            petProfileButton.addActionListener(e -> petProfileGUI.setVisible(true));
            gbc.gridy = rowIndex++;
            centerPanel.add(petProfileButton, gbc);
        }

        // 训练课程管理 - Trainer, Admin
        if ("Trainer".equals(role) || "Admin".equals(role)) {
            JButton trainingButton = createStyledButton("训练课程管理", new Color(46, 204, 113), "TRAINING");
            trainingButton.addActionListener(e -> openTrainingManagement());
            gbc.gridy = rowIndex++;
            centerPanel.add(trainingButton, gbc);
        }

        // 报名管理 - PetOwner, Admin
        if ("PetOwner".equals(role) || "Admin".equals(role)) {
            JButton enrollmentButton = createStyledButton("报名管理", new Color(155, 89, 182), "ENROLL");
            enrollmentButton.addActionListener(e -> openEnrollmentManagement());
            gbc.gridy = rowIndex++;
            centerPanel.add(enrollmentButton, gbc);
        }

        // 成就墙 - Trainer, Admin
        if ("Trainer".equals(role) || "Admin".equals(role)) {
            JButton achievementButton = createStyledButton("成就墙", new Color(241, 196, 15), "AWARDS");
            achievementButton.addActionListener(e -> achievementWallGUI.setVisible(true));
            gbc.gridy = rowIndex++;
            centerPanel.add(achievementButton, gbc);
        }

        // 用户管理 - 仅 Admin
        if ("Admin".equals(role)) {
            JButton userButton = createStyledButton("用户管理", new Color(149, 165, 166), "USERS");
            userButton.addActionListener(e -> openUserManagement());
            gbc.gridy = rowIndex++;
            centerPanel.add(userButton, gbc);
        }

        // 主题设置 - 所有人
        JButton themeSettingsButton = createStyledButton("主题设置", new Color(243, 156, 18), "THEME");
        themeSettingsButton.addActionListener(e -> openThemeSettings());
        gbc.gridy = rowIndex++;
        centerPanel.add(themeSettingsButton, gbc);

        // 关于系统 - 所有人
        JButton aboutButton = createStyledButton("关于系统", new Color(52, 73, 94), "ABOUT");
        aboutButton.addActionListener(e -> showAbout());
        gbc.gridy = rowIndex++;
        centerPanel.add(aboutButton, gbc);

        // 登出按钮
        JButton logoutButton = createStyledButton("登出", new Color(230, 126, 34), "LOGOUT");
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
                "确定要登出吗？",
                "登出确认",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            sessionManager.logout();

            // 关闭主菜单，返回登录界面
            this.setVisible(false);

            if (loginGUI != null) {
                loginGUI.clearForm();  // 清空登录表单
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
        String message = "宠物训练学校管理系统 v1.0\n\n" +
                "专业的宠物训练与管理平台\n\n" +
                "功能模块:\n" +
                "• 宠物档案管理\n" +
                "• 训练课程管理\n" +
                "• 报名管理\n" +
                "• 成就墙展示\n" +
                "• 用户管理\n" +
                "• 主题切换\n" +
                "• 角色权限控制\n\n" +
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