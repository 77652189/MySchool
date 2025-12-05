-- 插入用户数据
INSERT INTO user (username, password, role) VALUES
                                                ('admin', 'admin123', 'Admin'),
                                                ('trainer_john', 'pass123', 'Trainer'),
                                                ('trainer_mary', 'pass123', 'Trainer'),
                                                ('owner_alice', 'pass123', 'PetOwner'),
                                                ('owner_bob', 'pass123', 'PetOwner'),
                                                ('owner_charlie', 'pass123', 'PetOwner');

-- 插入训练师数据
INSERT INTO trainer (trainer_id, name, specialization, certification_level) VALUES
                                                                                (2, '约翰·史密斯', '服从训练', '高级认证训练师'),
                                                                                (3, '玛丽·琼斯', '敏捷训练', '专业认证训练师');

-- 插入宠物数据
INSERT INTO pet (pet_id, name, breed, species, age_in_months, temperament, owner_name, owner_contact) VALUES
                                                                                                          (4, '皮皮', '金毛寻回犬', '狗', 18, '活泼友好，精力充沛', '李小明', '138-1234-5678'),
                                                                                                          (5, '豆豆', '边境牧羊犬', '狗', 24, '聪明好动，服从性强', '王丽华', '139-8765-4321'),
                                                                                                          (6, '咪咪', '英国短毛猫', '猫', 12, '温顺安静，偶尔调皮', '张伟', '136-5555-6666');

-- 插入训练课程
INSERT INTO course (title, description, session_count, training_type, suitable_for) VALUES
                                                                                        ('基础服从训练', '教授宠物基本指令：坐、卧、等待、召回等', 12, '基础服从', '所有犬类'),
                                                                                        ('敏捷障碍训练', '通过障碍物训练提升宠物的协调性和服从性', 16, '敏捷训练', '中型及大型犬'),
                                                                                        ('社交礼仪课程', '帮助宠物学习与人和其他动物友好相处', 8, '社交训练', '所有宠物'),
                                                                                        ('行为矫正课程', '纠正不良行为如吠叫、撕咬、分离焦虑等', 10, '行为矫正', '所有犬类');

-- 插入训练班级
INSERT INTO course_section (course_id, trainer_id, training_ground, schedule, capacity) VALUES
                                                                                            (1, 2, 'A区训练场', '周一/周三 9:00-10:30', 8),
                                                                                            (1, 2, 'A区训练场', '周二/周四 14:00-15:30', 8),
                                                                                            (2, 3, 'B区敏捷场', '周一/周三 14:00-16:00', 6),
                                                                                            (3, 2, 'C区社交区', '周六 10:00-12:00', 10),
                                                                                            (4, 3, 'A区训练场', '周三/周五 15:00-16:30', 6);

-- 插入报名记录
INSERT INTO enrollment (pet_id, course_section_id, rating, progress_notes, status) VALUES
                                                                                       (4, 1, 'Good', '皮皮学习"坐"和"卧"指令进展顺利，召回能力需加强', 'InProgress'),
                                                                                       (4, 3, 'Excellent', '社交能力优秀，与其他狗狗相处融洽', 'Completed'),
                                                                                       (5, 1, 'Excellent', '边牧豆豆学习能力超强，已掌握所有基础指令', 'Completed'),
                                                                                       (5, 2, 'Good', '敏捷训练中表现出色，跳跃和穿越技巧娴熟', 'InProgress'),
                                                                                       (6, 3, NULL, '咪咪刚开始社交训练，需要逐步适应环境', 'InProgress'),
                                                                                       (6, 4, 'Fair', '在纠正抓沙发行为，有一定进展', 'InProgress');