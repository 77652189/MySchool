package com.nan.myschool;

import com.nan.myschool.gui.UserViewerGUI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
import java.awt.*;

@SpringBootApplication
public class MySchoolApplication {
    public static void main(String[] args) {
        // 设置图形环境
        System.setProperty("java.awt.headless", "false");

        ConfigurableApplicationContext context = SpringApplication.run(MySchoolApplication.class, args);

        // 检查是否支持图形界面
        if (!GraphicsEnvironment.isHeadless()) {
            SwingUtilities.invokeLater(() -> {
                try {
                    UserViewerGUI gui = context.getBean(UserViewerGUI.class);
                    gui.setVisible(true);
                    System.out.println("GUI启动成功！");
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
