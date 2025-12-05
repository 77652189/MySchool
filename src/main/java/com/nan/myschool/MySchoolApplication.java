package com.nan.myschool;

import com.formdev.flatlaf.FlatDarkLaf;
import com.nan.myschool.gui.SimpleLoginGUI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
import java.awt.*;

@SpringBootApplication
public class MySchoolApplication {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.setProperty("java.awt.headless", "false");

        ConfigurableApplicationContext context = SpringApplication.run(MySchoolApplication.class, args);

        if (!GraphicsEnvironment.isHeadless()) {
            SwingUtilities.invokeLater(() -> {
                try {
                    // 启动登录界面
                    SimpleLoginGUI loginGUI = context.getBean(SimpleLoginGUI.class);
                    loginGUI.setVisible(true);
                    System.out.println("登录界面启动成功！");

                } catch (Exception e) {
                    System.err.println("GUI启动失败：" + e.getMessage());
                    e.printStackTrace();
                }
            });
        } else {
            System.err.println("当前环境不支持图形界面！");
        }
    }
}