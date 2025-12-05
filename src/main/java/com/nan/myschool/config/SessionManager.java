package com.nan.myschool.config;

import com.nan.myschool.entity.User;
import org.springframework.stereotype.Component;

@Component
public class SessionManager {

    private static User currentUser;

    /**
     * 设置当前登录用户
     */
    public void setCurrentUser(User user) {
        currentUser = user;
    }

    /**
     * 获取当前用户
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * 获取当前角色
     */
    public String getCurrentRole() {
        return currentUser != null ? currentUser.getRole() : null;
    }

    /**
     * 检查是否已登录
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * 登出
     */
    public void logout() {
        currentUser = null;
    }

    /**
     * 检查权限
     */
    public boolean isAdmin() {
        return "Admin".equals(getCurrentRole());
    }

    public boolean isTrainer() {
        return "Trainer".equals(getCurrentRole());
    }

    public boolean isPetOwner() {
        return "PetOwner".equals(getCurrentRole());
    }
}