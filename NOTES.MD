day1
创建了一个spring boot项目，安装了四个依赖
spring web   用来连接前端
spring data jpa 用来简化数据库的操作，自动获得增删改查功能
mysql driver  用来连接mysql数据库
lombok  自动生成set get方法
安装了mysql,创建了一个数据库，名字是mysql94，密码是12356
尝试了mysql官方的工具，UI太丑，没有夜间模式，卸载了

通过idea内置的数据库连接上了数据库
创建了一个数据库名为theschool，并将项目连接到数据库上
创建了一个实体类TestUser,更改它的属性并生成了一个表
遇到一个问题是方言配置错误，删除这行后解决。

day2 
删除testuser实体类，创建了六个新的实体类，还有一些值
实体类 | 说明 | 关键字段 |
|--------|------|----------|
| `User` | 用户基础类 | id, username, password, role |
| `Student` | 学生信息 | studentId (FK), name, email, major, level |
| `Instructor` | 教师信息 | instructorId (FK), name, department |
| `Course` | 课程信息 | courseId, title, credits, department |
| `CourseSection` | 课程章节 | sectionId, courseId (FK), instructorId (FK), schedule |
| `Enrollment` | 选课记录 | enrollmentId, studentId (FK), courseSectionId (FK), grade |
运行后，hibernate自动创建了六个表和外键关系
通过idea内置的数据库展示了表格的图形界面
今天一切顺利

day3
创建了user类的仓库类，可以自动实现增删改查的SQL语句
创建了user类的服务类，封装了对仓库类的调用，实现了业务和逻辑的分离，如果未来需要增加新业务，不需要修改仓库类

创建一个GUI类，实现图形界面，使用构造器注入GUI类需要的service类，确保依赖注入正确
添加对应的按钮，调用service类的方法实现功能
遇到了表是空的问题，在mysql的终端添加了三个测试用户，解决

在启动类添加GUI的初始化代码，并输出启动成功或启动失败

在MVC模式中，GUI类充当了View的角色，Service类充当了Controller的角色，Repository类充当了Model的角色
如果需要验证用户输入的合法性，可以在Service类中添加验证逻辑，也可以在GUI类中添加输入验证格式