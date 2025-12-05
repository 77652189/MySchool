package com.nan.myschool.gui;

import com.nan.myschool.config.ThemeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class ThemeSettingsDialog extends JDialog {

    private final ThemeManager themeManager;
    private JToggleButton themeToggle;
    private JLabel themeStatusLabel;

    @Autowired
    public ThemeSettingsDialog(ThemeManager themeManager) {
        this.themeManager = themeManager;
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("主题设置");
        setModal(true);
        setSize(450, 300);
        setLayout(new BorderLayout(10, 10));

        // 主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        // 标题 - 移除 emoji
        JLabel titleLabel = new JLabel("外观设置");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        titleLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        // 当前主题状态
        themeStatusLabel = new JLabel("当前主题: " + themeManager.getCurrentThemeName());
        themeStatusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        themeStatusLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        // 主题切换面板
        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 25));


        // 切换按钮
        themeToggle = new JToggleButton();
        themeToggle.setPreferredSize(new Dimension(70, 35));
        themeToggle.setSelected(themeManager.isDarkMode());
        themeToggle.setFocusPainted(false);
        themeToggle.setText(themeManager.isDarkMode() ? "深色" : "浅色");
        themeToggle.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        themeToggle.addActionListener(e -> switchTheme());



        togglePanel.add(themeToggle);


        // 说明文字
        JLabel descLabel = new JLabel("点击切换按钮以更改主题");
        descLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        descLabel.setForeground(Color.GRAY);
        descLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        // 预览面板
        JPanel previewPanel = createPreviewPanel();

        // 添加组件
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(themeStatusLabel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(togglePanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(descLabel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(previewPanel);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));

        JButton applyButton = new JButton("应用");
        applyButton.setPreferredSize(new Dimension(100, 38));
        applyButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        applyButton.setBackground(new Color(52, 152, 219));
        applyButton.setForeground(Color.WHITE);
        applyButton.setFocusPainted(false);
        applyButton.setBorderPainted(false);
        applyButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "主题已应用到所有窗口",
                    "成功",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        JButton closeButton = new JButton("关闭");
        closeButton.setPreferredSize(new Dimension(100, 38));
        closeButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(applyButton);
        buttonPanel.add(closeButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setResizable(false);
    }

    private JPanel createPreviewPanel() {
        JPanel previewPanel = new JPanel();
        previewPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        previewPanel.setMaximumSize(new Dimension(400, 50));

        // 预览色块
        JPanel lightPreview = new JPanel();
        lightPreview.setPreferredSize(new Dimension(60, 40));
        lightPreview.setBackground(Color.WHITE);
        lightPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JPanel darkPreview = new JPanel();
        darkPreview.setPreferredSize(new Dimension(60, 40));
        darkPreview.setBackground(new Color(43, 43, 43));
        darkPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JLabel arrowLabel = new JLabel("→");
        arrowLabel.setFont(new Font("Arial", Font.BOLD, 20));

        if (themeManager.isDarkMode()) {
            previewPanel.add(darkPreview);
            previewPanel.add(new JLabel("当前使用"));
        } else {
            previewPanel.add(lightPreview);
            previewPanel.add(new JLabel("当前使用"));
        }

        return previewPanel;
    }

    private void switchTheme() {
        boolean newDarkMode = themeToggle.isSelected();
        themeManager.setDarkMode(newDarkMode);
        themeStatusLabel.setText("当前主题: " + themeManager.getCurrentThemeName());
        themeToggle.setText(newDarkMode ? "深色" : "浅色");

        // 更新当前对话框
        SwingUtilities.updateComponentTreeUI(this);

        // 重新创建预览面板 - 使用完整类名
        java.awt.Component[] components = ((JPanel)getContentPane().getComponent(0)).getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof JPanel && i == 8) {  // 预览面板的位置
                ((JPanel)getContentPane().getComponent(0)).remove(i);
                ((JPanel)getContentPane().getComponent(0)).add(createPreviewPanel(), i);
                break;
            }
        }
        revalidate();
        repaint();
    }
    /**
     * 显示对话框前更新状态
     */
    public void showDialog() {
        themeToggle.setSelected(themeManager.isDarkMode());
        themeToggle.setText(themeManager.isDarkMode() ? "深色" : "浅色");
        themeStatusLabel.setText("当前主题: " + themeManager.getCurrentThemeName());
        setVisible(true);
    }
}