INSERT INTO users (name, email, immutable) VALUES 
("Raymond", "raymondemail@gmail.com", true),
("Richard", "richardemail@hotmail.com", true),
("Aline", "alineemail@icloud.com", true);

INSERT INTO courses (name, category, price, description, user_id, immutable) VALUES
("Curso de tecnologia 1", "Tecnologia", 29.99, "Este é um curso exemplo de tecnologia", 1, true),
("Curso de administração 1", "Administração", 49.90, "Este é um curso exemplo de administração", 2, true),
("Curso de física", "Ciência", 35.90, "Este é um exemplo de curso de física", 3, true);

INSERT INTO sales (user_id, course_id, immutable) VALUES 
(1, 1, true),
(2, 2, true),
(3, 3, true);

INSERT INTO purchases (user_id, course_id, immutable) VALUES 
(1, 2, true),
(1, 3, true),
(2, 1, true);
