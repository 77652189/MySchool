This project is a pet school management system.
day1
创建了一个spring boot项目，安装了四个依赖
spring web   用来连接前端
spring data jpa 用来简化数据库的操作，自动获得增删改查功能
mysql driver  用来连接mysql数据库
lombok  自动生成set get方法
安装了mysql,创建了一个数据库，名字是mysql94，密码是12356
尝试了mysql workbench，UI太丑，没有夜间模式，卸载了

通过idea内置的数据库连接上了数据库
创建了一个数据库名为myschool，并将项目连接到数据库上
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

day4
学习了ON DELETE CASCADE的作用和 JOIN的使用,在Course CourseSection Student Instructor实体类中添加了级联删除的注解

day5
创建了实体类对应的仓库类和服务类，并在GUI类中添加了对应的按钮和功能
通过这些类实现了选课，退课，查看课程等功能。以及增加课程，删除课程等功能
增加了两个GUI界面，用于展示以上的功能
增加了一个总的GUI界面，用于导航到各个功能界面
遇到了一些小bug，主要是拼写错误和类没有正确导入，通过仔细检查代码和IDEA的提示解决
没有添加数据导致无法运行，新建了一个data类，添加了一些测试数据，每次运行时删除旧数据，添加新数据，确保每次运行时都有数据可用
整体进展顺利，基本实现了预期的功能

day6
添加了flatlaf作为依赖，让界面更加美化，实现深色模式
遇到一个小bug，将深色模式作为默认选项后，切换回浅色模式时，界面没有变化，通过重新设置UIManager解决

重做几乎全部内容，更改为宠物学校
实现了六个类
Pet Owner Pet Instructor Pet Course Pet Course Section Pet Enrollment
实现了对应的仓库类和服务类
在GUI类中添加了对应的按钮和功能
通过这些类实现了选课，退课，查看课程等功能。以及增加课程，删除课程等功能
增加了两个GUI界面，用于展示以上的功能
增加了一个总的GUI界面，用于导航到各个功能界面

产生大量bug，包括类没有导入，依赖的方法发生改变，拼写错误等
通过仔细检查代码和IDEA的提示解决
数据库的残余数据导致无法运行，通过删除旧数据，添加新数据解决
整体进展顺利，基本实现了预期的功能


day7
增加成就墙，宠物档案功能
增加登录功能，不同的权限可以查看的内容不同，因此在config中添加了权限验证，在service中验证，并根据权限展示不同内容
将默认启动界面改为登录界面
遇到bug，游客可以查看所有内容，通过在service类中添加权限验证解决   

day8
翻译为英文，添加了三张图片，修改了petprofile和data.sql来加载图片

slides：
https://gamma.app/docs/Pet-Training-School-Management-System-oldwv5dn8b61mnm?mode=doc
