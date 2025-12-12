-- Insert user data
INSERT INTO user (username, password, role) VALUES
                                                ('admin', 'admin123', 'Admin'),
                                                ('trainer_john', 'pass123', 'Trainer'),
                                                ('trainer_mary', 'pass123', 'Trainer'),
                                                ('owner_alice', 'pass123', 'PetOwner'),
                                                ('owner_bob', 'pass123', 'PetOwner'),
                                                ('owner_charlie', 'pass123', 'PetOwner');

-- Insert trainer data
INSERT INTO trainer (trainer_id, name, specialization, certification_level) VALUES
                                                                                (2, 'John Smith', 'Obedience Training', 'Senior Certified Trainer'),
                                                                                (3, 'Mary Jones', 'Agility Training', 'Professional Certified Trainer');

-- Insert pet data with photo_url
INSERT INTO pet (pet_id, name, breed, species, age_in_months, temperament, owner_name, owner_contact, photo_url) VALUES
                                                                                                                     (4, 'Pipi', 'Golden Retriever', 'Dog', 18, 'Lively and friendly, energetic', 'Li Xiaoming', '138-1234-5678', '/images/pets/pipi.png'),
                                                                                                                     (5, 'Doudou', 'Border Collie', 'Dog', 24, 'Smart and active, highly obedient', 'Wang Lihua', '139-8765-4321', '/images/pets/doudou.png'),
                                                                                                                     (6, 'Mimi', 'British Shorthair', 'Cat', 12, 'Gentle and quiet, occasionally playful', 'Zhang Wei', '136-5555-6666', '/images/pets/mimi.png');

-- Insert training courses
INSERT INTO course (title, description, session_count, training_type, suitable_for) VALUES
                                                                                        ('Basic Obedience Training', 'Teaches basic commands: sit, down, wait, recall, etc.', 12, 'Basic Obedience', 'All dog breeds'),
                                                                                        ('Agility Obstacle Training', 'Improves coordination and obedience through obstacle exercises', 16, 'Agility Training', 'Medium and large dogs'),
                                                                                        ('Social Etiquette Course', 'Helps pets learn to interact positively with humans and other animals', 8, 'Social Training', 'All pets'),
                                                                                        ('Behavior Correction Course', 'Corrects undesirable behaviors such as barking, chewing, separation anxiety, etc.', 10, 'Behavior Correction', 'All dog breeds');

-- Insert course sections
INSERT INTO course_section (course_id, trainer_id, training_ground, schedule, capacity) VALUES
                                                                                            (1, 2, 'Training Ground A', 'Mon/Wed 9:00-10:30', 8),
                                                                                            (1, 2, 'Training Ground A', 'Tue/Thu 14:00-15:30', 8),
                                                                                            (2, 3, 'Agility Field B', 'Mon/Wed 14:00-16:00', 6),
                                                                                            (3, 2, 'Social Area C', 'Sat 10:00-12:00', 10),
                                                                                            (4, 3, 'Training Ground A', 'Wed/Fri 15:00-16:30', 6);

-- Insert enrollment records
INSERT INTO enrollment (pet_id, course_section_id, rating, progress_notes, status) VALUES
                                                                                       (4, 1, 'Good', 'Pipi is making good progress with "sit" and "down" commands; recall needs improvement', 'InProgress'),
                                                                                       (4, 3, 'Excellent', 'Excellent social skills; gets along well with other dogs', 'Completed'),
                                                                                       (5, 1, 'Excellent', 'Doudou learns extremely fast and has mastered all basic commands', 'Completed'),
                                                                                       (5, 2, 'Good', 'Performs well in agility training; proficient in jumping and weaving exercises', 'InProgress'),
                                                                                       (6, 3, NULL, 'Mimi is just beginning social training and needs time to adjust to the environment', 'InProgress'),
                                                                                       (6, 4, 'Fair', 'Progress in correcting sofa-scratching behavior', 'InProgress');