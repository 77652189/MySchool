package com.nan.myschool.gui;

import com.nan.myschool.entity.User;
import com.nan.myschool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@Component
public class UserViewerGUI extends JFrame {
    private final UserService userService;
    private List<User> users;
    private int currentIndex = 0;

    private JLabel idLabel = new JLabel("用户ID: ");
    private JLabel usernameLabel = new JLabel("用户名: ");
    private JLabel roleLabel = new JLabel("角色: ");
    private JButton prevButton = new JButton("上一个");
    private JButton nextButton = new JButton("下一个");
    private JButton addButton = new JButton("添加");
    private JButton deleteButton = new JButton("删除");
    private JButton updateButton = new JButton("修改");

    @Autowired
    public UserViewerGUI(UserService userService) {
        this.userService = userService;
        initializeGUI();
        loadUsers();
    }

    private void initializeGUI() {
        setTitle("用户信息管理系统");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout(10, 10));

        // 信息面板
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoPanel.add(idLabel);
        infoPanel.add(usernameLabel);
        infoPanel.add(roleLabel);

        // 导航按钮面板
        JPanel navPanel = new JPanel(new FlowLayout());
        navPanel.add(prevButton);
        navPanel.add(nextButton);

        // 操作按钮面板
        JPanel actionPanel = new JPanel(new FlowLayout());
        actionPanel.add(addButton);
        actionPanel.add(deleteButton);
        actionPanel.add(updateButton);

        // 将所有面板添加到主窗口
        add(infoPanel, BorderLayout.CENTER);
        add(navPanel, BorderLayout.NORTH);
        add(actionPanel, BorderLayout.SOUTH);

        // 添加按钮事件
        prevButton.addActionListener(e -> showPreviousUser());
        nextButton.addActionListener(e -> showNextUser());
        addButton.addActionListener(e -> addNewUser());
        deleteButton.addActionListener(e -> deleteCurrentUser());
        updateButton.addActionListener(e -> updateCurrentUser());

        // 设置窗口居中显示
        setLocationRelativeTo(null);
    }

    private void loadUsers() {
        users = userService.getAllUsers();
        if (!users.isEmpty()) {
            displayCurrentUser();
        }
    }

    private void displayCurrentUser() {
        if (users != null && !users.isEmpty()) {
            User currentUser = users.get(currentIndex);
            idLabel.setText("用户ID: " + currentUser.getId());
            usernameLabel.setText("用户名: " + currentUser.getUsername());
            roleLabel.setText("角色: " + currentUser.getRole());
        }
    }

    private void showPreviousUser() {
        if (currentIndex > 0) {
            currentIndex--;
            displayCurrentUser();
        }
    }

    private void showNextUser() {
        if (currentIndex < users.size() - 1) {
            currentIndex++;
            displayCurrentUser();
        }
    }

    private void addNewUser() {
        // 弹出添加用户对话框
        JDialog dialog = new JDialog(this, "添加新用户", true);
        dialog.setLayout(new GridLayout(4, 2, 5, 5));

        JTextField usernameField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField roleField = new JTextField();

        dialog.add(new JLabel("用户名:"));
        dialog.add(usernameField);
        dialog.add(new JLabel("密码:"));
        dialog.add(passwordField);
        dialog.add(new JLabel("角色:"));
        dialog.add(roleField);

        JButton submitButton = new JButton("确定");
        submitButton.addActionListener(e -> {
            User newUser = new User();
            newUser.setUsername(usernameField.getText());
            newUser.setPassword(passwordField.getText());
            newUser.setRole(roleField.getText());
            userService.saveUser(newUser);
            loadUsers();
            dialog.dispose();
        });

        dialog.add(submitButton);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void deleteCurrentUser() {
        if (users != null && !users.isEmpty()) {
            User currentUser = users.get(currentIndex);
            int option = JOptionPane.showConfirmDialog(this,
                    "确定要删除用户 " + currentUser.getUsername() + " 吗？",
                    "确认删除",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                userService.deleteUser(currentUser.getId());
                loadUsers();
            }
        }
    }

    private void updateCurrentUser() {
        if (users == null || users.isEmpty()) return;

        User currentUser = users.get(currentIndex);
        JDialog dialog = new JDialog(this, "修改用户信息", true);
        dialog.setLayout(new GridLayout(4, 2, 5, 5));

        JTextField usernameField = new JTextField(currentUser.getUsername());
        JTextField passwordField = new JTextField(currentUser.getPassword());
        JTextField roleField = new JTextField(currentUser.getRole());

        dialog.add(new JLabel("用户名:"));
        dialog.add(usernameField);
        dialog.add(new JLabel("密码:"));
        dialog.add(passwordField);
        dialog.add(new JLabel("角色:"));
        dialog.add(roleField);

        JButton submitButton = new JButton("保存");
        submitButton.addActionListener(e -> {
            currentUser.setUsername(usernameField.getText());
            currentUser.setPassword(passwordField.getText());
            currentUser.setRole(roleField.getText());
            userService.saveUser(currentUser);
            loadUsers();
            dialog.dispose();
        });

        dialog.add(submitButton);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
