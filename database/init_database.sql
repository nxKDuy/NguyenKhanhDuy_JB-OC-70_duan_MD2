-- ============================================================
-- SCRIPT KHỞI TẠO CƠ SỞ DỮ LIỆU POSTGRESQL
-- Ứng dụng Quản lý Khóa học và Học viên
-- ============================================================

-- 1. Tạo bảng ADMIN
CREATE TABLE admin (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- 2. Tạo bảng STUDENT
CREATE TABLE student (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    dob DATE NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    sex BOOLEAN NOT NULL,
    phone VARCHAR(20),
    password VARCHAR(255) NOT NULL,
    create_at DATE DEFAULT CURRENT_DATE
);

-- 3. Tạo bảng COURSE
CREATE TABLE course (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    duration INT NOT NULL,
    instructor VARCHAR(100) NOT NULL,
    create_at DATE DEFAULT CURRENT_DATE
);

-- 4. Tạo bảng ENROLLMENT (bảng trung gian)
CREATE TABLE enrollment (
    id SERIAL PRIMARY KEY,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'WAITING',
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
    CONSTRAINT unique_enrollment UNIQUE(student_id, course_id)
);

-- ============================================================
-- DỮ LIỆU MẪU (dùng để test)
-- ============================================================

-- Thêm admin
INSERT INTO admin (username, password) VALUES ('admin', 'admin123');

-- Thêm học viên
INSERT INTO student (name, dob, email, sex, phone, password) VALUES
('Nguyễn Văn A', '2005-01-15', 'nguyenvana@email.com', true, '0912345678', 'password123'),
('Trần Thị B', '2004-08-20', 'tranthib@email.com', false, '0987654321', 'password123'),
('Hoàng Văn C', '2006-03-10', 'hoangvanc@email.com', true, '0934567890', 'password123');

-- Thêm khóa học
INSERT INTO course (name, duration, instructor) VALUES
('Lập trình Java cơ bản', 40, 'Thầy Hùng'),
('Web Development với Spring Boot', 50, 'Thầy Minh'),
('Database Design', 30, 'Cô Linh'),
('Front-end React.js', 35, 'Thầy Huy');

-- Thêm đăng ký khóa học
INSERT INTO enrollment (student_id, course_id, status) VALUES
(1, 1, 'CONFIRM'),
(1, 2, 'WAITING'),
(2, 1, 'CONFIRM'),
(2, 3, 'CONFIRM'),
(3, 2, 'WAITING'),
(3, 4, 'CONFIRM');

-- ============================================================
-- CÁC QUERY THỐNG KÊ HỮU ÍCH
-- ============================================================

-- Đếm tổng học viên
SELECT COUNT(*) as "Tổng học viên" FROM student;

-- Đếm tổng khóa học
SELECT COUNT(*) as "Tổng khóa học" FROM course;

-- Xem học viên cùng với khóa học đã đăng ký
SELECT 
    s.id, s.name as "Tên học viên", 
    c.name as "Tên khóa học", 
    e.status as "Trạng thái",
    e.registered_at
FROM student s
JOIN enrollment e ON s.id = e.student_id
JOIN course c ON e.course_id = c.id
ORDER BY s.id, c.id;

-- Top 5 khóa học đông nhất (chỉ tính những đăng ký CONFIRM)
SELECT 
    c.id,
    c.name as "Tên khóa học",
    c.instructor as "Giảng viên",
    COUNT(e.id) as "Số học viên"
FROM course c
LEFT JOIN enrollment e ON c.id = e.course_id AND e.status = 'CONFIRM'
GROUP BY c.id, c.name, c.instructor
ORDER BY COUNT(e.id) DESC
LIMIT 5;

-- Hiển thị khóa học mà một học viên chưa đăng ký (ID = 1)
SELECT c.*
FROM course c
WHERE c.id NOT IN (
    SELECT DISTINCT course_id 
    FROM enrollment 
    WHERE student_id = 1
);
