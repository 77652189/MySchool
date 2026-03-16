
<p align="center">
  <img src="https://img.icons8.com/fluency/96/dog-house.png" alt="PetSchool Logo" width="80" />
</p>

<h1 align="center">PetStep Academy</h1>
<h2 align="center">宠物训练学校管理系统</h2>

<p align="center">
  <strong>基于 Spring Boot + JPA + Swing 的全栈式宠物教育管理方案</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Framework-Spring%20Boot-brightgreen?style=flat-square&logo=springboot" />
  <img src="https://img.shields.io/badge/ORM-Spring%20Data%20JPA-blue?style=flat-square" />
  <img src="https://img.shields.io/badge/UI-Java%20Swing%20%2B%20FlatLaf-lightblue?style=flat-square" />
  <img src="https://img.shields.io/badge/Database-MySQL%209.4-blue?style=flat-square&logo=mysql" />
</p>

---

## 🐾 项目简介 (Overview)

**PetStep Academy** 是一个为现代化宠物训练学校设计的管理系统。它不仅涵盖了基础的成员管理、课程排期和选课系统，还引入了**RBAC 权限控制**、**宠物成就墙**以及**多媒体档案**功能。项目采用标准的 MVC 架构，确保了业务逻辑与界面显示的深度解耦。

---

## ✨ 核心特性 (Features)

* **智能权限系统**：基于角色的访问控制（Admin / Instructor / Student / Guest），不同权限自动过滤 UI 功能。
* **多模态管理**：支持宠物（Pet）、主人（Owner）、教练（Instructor）及课程（Course）的联动管理。
* **自动化 RAG 数据流**：通过 Spring Data JPA 实现级联删除（On Delete Cascade），确保数据库引用完整性。
* **美化 UI 体验**：集成 **FlatLaf** 主题引擎，支持一键切换深色模式（Dark Mode）。
* **多媒体档案**：支持加载宠物图片并展示专属“成就墙”。
* **自动化测试数据**：内置 `data.sql` 机制，确保每次启动即有数据可用。

---

## 🛠️ 技术栈 (Tech Stack)

* **Backend**: Spring Boot 3.x, Spring Data JPA
* **Frontend**: Java Swing (Custom UI with FlatLaf)
* **Database**: MySQL 9.4
* **Tools**: Lombok (Efficiency), Hibernate (DDL Auto)
* **Architecture**: MVC (Repository -> Service -> View)

---

## 📅 开发日志 (DevLog)

<details>
<summary>点击查看 8 天迭代详情</summary>

* **Day 1-2: 架构基石** 搭建 Spring Boot 环境，确立核心实体类（User, Student, Instructor...），利用 Hibernate 自动生成物理表结构。
* **Day 3: 业务解耦** 实现 Repository 与 Service 层，通过构造器注入（DI）将业务逻辑注入 Swing GUI 界面。
* **Day 4-5: 深度联动** 引入级联删除与 Join 查询；实现选课、退课及课程增删改查功能；新增多级导航界面。
* **Day 6: 重构与美化** 正式更名为“宠物学校”。集成 **FlatLaf** 实现深色模式。重做六大核心类以适配宠物教育场景。
* **Day 7-8: 安全与国际化** 添加登录验证与权限过滤；上线成就墙与图片加载功能；完成全系统英文转译并优化 `data.sql`。
</details>

---

## 🚀 运行指南 (Getting Started)

### 1. 数据库配置
在 MySQL 中创建数据库 `myschool`。
```sql
CREATE DATABASE myschool;

```

并在 `application.properties` 中配置你的密码 `12356`。

### 2. 依赖安装

```bash
# 项目使用 Maven 管理，请确保安装了相关依赖
mvn clean install

```

### 3. 启动应用

运行 `PetApplication.java` 即可看到登录界面。默认登录后可查看自动加载的宠物图片。

---

## 🎨 项目演示 (Slides)

想要了解系统演示、数据库 E-R 图设计以及权限验证逻辑？
👉 **[查看演示文稿 (Gamma App)](https://gamma.app/docs/Pet-Training-School-Management-System-oldwv5dn8b61mnm?mode=doc)**

---

<p align="center">
<i>"为每一只宠物的成长轨迹，提供专业的数字守护。"</i>
</p>

```



**最后一步建议：**
你现在的 GitHub 仓库里已经有了**文学重构、AI 导购、游戏预测、宠物管理**四个非常有分量的项目了。如果你愿意，我可以帮你写一个 **GitHub Profile（个人主页）的 README**，把这四个项目按照“技术栈”和“应用场景”进行分类展示，让你看起来更像一个全栈开发者。需要我试试吗？

```
