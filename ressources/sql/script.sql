CREATE TABLE `TEACHERS` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `last_name` VARCHAR(40),
  `first_name` VARCHAR(40),
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `SESSIONS` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(50),
  `description` VARCHAR(2000),
  `date` TIMESTAMP,
  `teacher_id` int,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `USERS` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `last_name` VARCHAR(40),
  `first_name` VARCHAR(40),
  `admin` BOOLEAN NOT NULL DEFAULT false,
  `email` VARCHAR(255),
  `password` VARCHAR(255),
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `PARTICIPATE` (
  `user_id` INT, 
  `session_id` INT
);

ALTER TABLE `SESSIONS` ADD FOREIGN KEY (`teacher_id`) REFERENCES `TEACHERS` (`id`);
ALTER TABLE `PARTICIPATE` ADD FOREIGN KEY (`user_id`) REFERENCES `USERS` (`id`);
ALTER TABLE `PARTICIPATE` ADD FOREIGN KEY (`session_id`) REFERENCES `SESSIONS` (`id`);

INSERT INTO TEACHERS (first_name, last_name)
VALUES ('Margot', 'DELAHAYE'),
       ('Hélène', 'THIERCELIN');


INSERT INTO USERS (first_name, last_name, admin, email, password)
VALUES ('Admin', 'Admin', true, 'yoga@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq'),
VALUES ('Test', 'TEST', true, 'test@test.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq');


INSERT INTO `SESSIONS` (`name`, `description`, `date`, `teacher_id`)
VALUES 
('Yoga session 1', 'Yoga description 1', '2024-07-28 10:00:00', 1),
('Yoga session 2', 'Yoga description 2', '2024-08-01 15:00:00', 2);

INSERT INTO `participate` (`user_id`, `session_id`) VALUES ('1', '1')