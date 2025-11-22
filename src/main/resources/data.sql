-- 插入测试用户数据
INSERT IGNORE INTO user (id, username, password, role) VALUES
(1, 'admin', 'admin123', 'Admin'),
(2, 'john_teacher', 'pass123', 'Instructor'),
(3, 'mary_teacher', 'pass123', 'Instructor'),
(4, 'alice_student', 'pass123', 'Student'),
(5, 'bob_student', 'pass123', 'Student'),
(6, 'charlie_student', 'pass123', 'Student');

-- 插入教师数据
INSERT IGNORE INTO instructor (instructor_id, name, department) VALUES
(2, '张教授', '计算机科学系'),
(3, '李老师', '数学系');

-- 插入学生数据
INSERT IGNORE INTO student (student_id, name, email, major, level) VALUES
(4, '王小明', 'alice@school.edu', '计算机科学', 'Junior'),
(5, '李华', 'bob@school.edu', '软件工程', 'Sophomore'),
(6, '陈思', 'charlie@school.edu', '信息系统', 'Freshman');

-- 插入课程数据
INSERT IGNORE INTO course (course_id, title, description, credits, department) VALUES
(1, '数据结构与算法', '学习基本数据结构和算法设计', 4, '计算机科学系'),
(2, '数据库系统', '关系数据库理论与实践', 3, '计算机科学系'),
(3, '高等数学', '微积分和线性代数', 4, '数学系'),
(4, '操作系统', '操作系统原理与实现', 3, '计算机科学系');

-- 插入课程章节数据
INSERT IGNORE INTO course_section (section_id, course_id, instructor_id, room, schedule, capacity) VALUES
(1, 1, 2, 'A101', 'Mon & Wed 9:00-10:30 AM', 30),
(2, 1, 2, 'A102', 'Tue & Thu 2:00-3:30 PM', 25),
(3, 2, 2, 'B201', 'Mon & Wed 2:00-3:30 PM', 35),
(4, 3, 3, 'C301', 'Tue & Thu 10:00-11:30 AM', 40),
(5, 4, 2, 'A103', 'Wed & Fri 3:00-4:30 PM', 28);

-- 插入选课记录
INSERT IGNORE INTO enrollment (enrollment_id, student_id, course_section_id, grade) VALUES
(1, 4, 1, 'A'),
(2, 4, 3, 'B+'),
(3, 5, 1, 'B'),
(4, 5, 2, 'A-'),
(5, 6, 1, NULL),
(6, 6, 4, 'C+');