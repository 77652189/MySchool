package com.nan.myschool.config;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import org.springframework.stereotype.Component;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Window;

@Component
public class ThemeManager {

    private boolean isDarkMode = true;  // 改为 true，默认深色

    /**
     * 切换主题
     */
    public void toggleTheme() {
        isDarkMode = !isDarkMode;
        applyTheme();
    }

    /**
     * 设置为深色模式
     */
    public void setDarkMode(boolean darkMode) {
        this.isDarkMode = darkMode;
        applyTheme();
    }

    /**
     * 应用主题
     */
    public void applyTheme() {
        try {
            if (isDarkMode) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else {
                UIManager.setLookAndFeel(new FlatIntelliJLaf());
            }

            // 更新所有已打开的窗口
            updateAllWindows();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新所有窗口
     */
    private void updateAllWindows() {
        for (Window window : Window.getWindows()) {
            SwingUtilities.updateComponentTreeUI(window);
            window.repaint();
        }
    }

    /**
     * 获取当前是否为深色模式
     */
    public boolean isDarkMode() {
        return isDarkMode;
    }

    /**
     * 获取当前主题名称
     */
    public String getCurrentThemeName() {
        return isDarkMode ? "Night mode" : "day mode";
    }
}