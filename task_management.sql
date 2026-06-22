CREATE DATABASE IF NOT EXISTS task_management;
USE task_management;

CREATE TABLE IF NOT EXISTS users(
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    fullname VARCHAR(100),
    role ENUM('ADMIN','USER')
);

CREATE TABLE IF NOT EXISTS categories(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) UNIQUE, 
    description TEXT
);

CREATE TABLE IF NOT EXISTS tasks(
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200),
    description TEXT,
    start_date DATE,
    end_date DATE,
    priority ENUM('LOW','MEDIUM','HIGH'),
    status ENUM('TODO','DOING','DONE'),
    category_id INT,
    user_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(category_id) REFERENCES categories(id),
    FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS logs(
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50),
    action VARCHAR(100),
    time DATETIME DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (username, password, fullname, role) 
VALUES ('admin', '123456', 'Administrator', 'ADMIN')
ON DUPLICATE KEY UPDATE 
    password = VALUES(password),
    fullname = VALUES(fullname),
    role = VALUES(role);

INSERT IGNORE INTO categories(name, description)
VALUES
('Học tập','Quản lý việc học'),
('Cá nhân','Công việc cá nhân'),
('Gia đình','Công việc gia đình'),
('Công việc','Công việc hàng ngày');

ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '123456';
FLUSH PRIVILEGES;