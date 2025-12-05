package com.nan.myschool.gui;

import com.nan.myschool.config.SessionManager;
import com.nan.myschool.entity.User;
import com.nan.myschool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@Component
public class SimpleLoginGUI extends JFrame {

    private final UserService userService;
    private final SessionManager sessionManager;
    private final MainMenuGUI mainMenuGUI;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton exitButton;  // 改为退出按钮，移除游客按钮
    private JLabel statusLabel;

    @Autowired
    public SimpleLoginGUI(UserService userService,
                          SessionManager sessionManager,
                          MainMenuGUI mainMenuGUI) {
        this.userService = userService;
        this.sessionManager = sessionManager;
        this.mainMenuGUI = mainMenuGUI;

        // 设置 MainMenuGUI 的 loginGUI 引用
        this.mainMenuGUI.setLoginGUI(this);

        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("系统登录");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 400);
        setLayout(new BorderLayout());

        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(52, 152, 219));
        logoPanel.setPreferredSize(new Dimension(450, 100));

        JLabel logoLabel = new JLabel("Pet Training School");
        logoLabel.setFont(new Font("微软雅黑", Font.BOLD, 26));
        logoLabel.setForeground(Color.WHITE);
        logoPanel.add(logoLabel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // 用户名
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel usernameLabel = new JLabel("用户名：");
        usernameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        usernameField = new JTextField();
        usernameField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(200, 32));
        formPanel.add(usernameField, gbc);

        // 密码
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel passwordLabel = new JLabel("密码：");
        passwordLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(200, 32));
        formPanel.add(passwordField, gbc);

        // 状态标签
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        statusLabel.setForeground(Color.RED);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(statusLabel, gbc);

        // 提示信息
        gbc.gridy = 3;
        JLabel hintLabel = new JLabel("<html><center>测试账号：<br>admin/admin123 (管理员)<br>trainer_john/pass123 (训练师)<br>owner_alice/pass123 (宠物主人)</center></html>");
        hintLabel.setFont(new Font("微软雅黑", Font.PLAIN, 11));
        hintLabel.setForeground(Color.GRAY);
        hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(hintLabel, gbc);

        // 按钮区域
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));

        loginButton = new JButton("登录");
        loginButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(120, 40));
        loginButton.setBackground(new Color(52, 152, 219));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        exitButton = new JButton("退出");
        exitButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        exitButton.setPreferredSize(new Dimension(120, 40));
        exitButton.setFocusPainted(false);

        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);

        loginButton.addActionListener(e -> performLogin());
        exitButton.addActionListener(e -> System.exit(0));
        passwordField.addActionListener(e -> performLogin());

        add(logoPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("用户名和密码不能为空！");
            statusLabel.setForeground(Color.RED);
            return;
        }

        List<User> allUsers = userService.getAllUsers();
        User matchedUser = null;

        for (User user : allUsers) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                matchedUser = user;
                break;
            }
        }

        if (matchedUser != null) {
            sessionManager.setCurrentUser(matchedUser);
            statusLabel.setText("登录成功！");
            statusLabel.setForeground(new Color(46, 204, 113));

            Timer timer = new Timer(500, e -> {
                mainMenuGUI.refreshMenu();
                mainMenuGUI.setVisible(true);
                this.setVisible(false);  // 隐藏登录窗口
            });
            timer.setRepeats(false);
            timer.start();

        } else {
            statusLabel.setText("用户名或密码错误！");
            statusLabel.setForeground(Color.RED);
            passwordField.setText("");
        }
    }

    /**
     * 清空登录表单
     */
    public void clearForm() {
        usernameField.setText("");
        passwordField.setText("");
        statusLabel.setText(" ");
    }
}


