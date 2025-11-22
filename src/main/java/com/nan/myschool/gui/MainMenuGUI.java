package com.nan.myschool.gui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class MainMenuGUI extends JFrame {
    private final UserViewerGUI userViewerGUI;
    private final SectionMasterDetailGUI sectionMasterDetailGUI;
    private final EnrollmentManagementGUI enrollmentManagementGUI; // 新增

    @Autowired
    public MainMenuGUI(UserViewerGUI userViewerGUI,
                       SectionMasterDetailGUI sectionMasterDetailGUI,
                       EnrollmentManagementGUI enrollmentManagementGUI) { // 新增参数
        this.userViewerGUI = userViewerGUI;
        this.sectionMasterDetailGUI = sectionMasterDetailGUI;
        this.enrollmentManagementGUI = enrollmentManagementGUI; // 新增
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("教育管理系统 - 主菜单");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 450); // 调整高度
        setLayout(new BorderLayout(10, 10));

        // 标题面板
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("教育管理系统");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 28));
        titlePanel.add(titleLabel);

        // 菜单按钮面板
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(5, 1, 10, 15)); // 改为5行
        menuPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // 创建菜单按钮
        JButton sectionButton = createMenuButton("课程章节管理");
        JButton enrollmentButton = createMenuButton("选课管理"); // 新增
        JButton userButton = createMenuButton("用户管理");
        JButton aboutButton = createMenuButton("关于系统");
        JButton exitButton = createMenuButton("退出系统");

        // 添加按钮事件
        sectionButton.addActionListener(e -> openSectionManagement());
        enrollmentButton.addActionListener(e -> openEnrollmentManagement()); // 新增
        userButton.addActionListener(e -> openUserManagement());
        aboutButton.addActionListener(e -> showAbout());
        exitButton.addActionListener(e -> exitApplication());

        menuPanel.add(sectionButton);
        menuPanel.add(enrollmentButton); // 新增
        menuPanel.add(userButton);
        menuPanel.add(aboutButton);
        menuPanel.add(exitButton);

        // 版权信息
        JPanel footerPanel = new JPanel();
        JLabel footerLabel = new JLabel("© 2024 MySchool Education System");
        footerLabel.setFont(new Font("微软雅黑", Font.PLAIN, 10));
        footerLabel.setForeground(Color.GRAY);
        footerPanel.add(footerLabel);

        add(titlePanel, BorderLayout.NORTH);
        add(menuPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(300, 50));
        return button;
    }

    private void openSectionManagement() {
        sectionMasterDetailGUI.setVisible(true);
    }

    // 新增方法
    private void openEnrollmentManagement() {
        enrollmentManagementGUI.setVisible(true);
    }

    private void openUserManagement() {
        userViewerGUI.setVisible(true);
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(this,
                "教育管理系统 v1.0\n\n" +
                        "功能模块:\n" +
                        "- 课程章节管理\n" +
                        "- 选课管理 (新增)\n" + // 更新
                        "- 用户管理\n\n" +
                        "开发者: Nan\n" +
                        "Northeastern University",
                "关于系统",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void exitApplication() {
        int option = JOptionPane.showConfirmDialog(this,
                "确定要退出系统吗？",
                "退出确认",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}