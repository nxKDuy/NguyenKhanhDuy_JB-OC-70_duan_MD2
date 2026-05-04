-- ============================================================
-- CÁC QUERY SQL HỮUU ÍCH CHO ỨNG DỤNG
-- Dùng để thống kê, báo cáo, gợi ý khóa học
-- ============================================================

-- ============================================================
-- 1. THỐNG KÊ CHUNG
-- ============================================================

-- Đếm tổng học viên
SELECT COUNT(*) as "Tổng học viên" FROM student;

-- Đếm tổng khóa học
SELECT COUNT(*) as "Tổng khóa học" FROM course;

-- Đếm tổng lượt đăng ký
SELECT COUNT(*) as "Tổng đăng ký" FROM enrollment;

-- Đếm đăng ký theo trạng thái
SELECT 
    status,
    COUNT(*) as "Số lượng"
FROM enrollment
GROUP BY status
ORDER BY "Số lượng" DESC;

-- ============================================================
-- 2. DANH SÁCH HỌC VIÊN VỚI CHI TIẾT
-- ============================================================

-- Xem thông tin học viên kèm số khóa họ đã đăng ký
SELECT 
    s.id,
    s.name as "Tên học viên",
    s.email,
    COUNT(e.id) as "Số khóa đã đăng ký",
    COUNT(CASE WHEN e.status = 'CONFIRM' THEN 1 END) as "Số khóa confirmed"
FROM student s
LEFT JOIN enrollment e ON s.id = e.student_id
GROUP BY s.id, s.name, s.email
ORDER BY s.id;

-- ============================================================
-- 3. DANH SÁCH KHÓA HỌC VỚI THỐNG KÊ
-- ============================================================

-- Xem chi tiết khóa học kèm số lượng học viên
SELECT 
    c.id,
    c.name as "Tên khóa học",
    c.duration as "Thời lượng (giờ)",
    c.instructor as "Giảng viên",
    COUNT(DISTINCT e.student_id) as "Tổng đăng ký",
    COUNT(DISTINCT CASE WHEN e.status = 'CONFIRM' THEN e.student_id END) as "Xác nhận",
    COUNT(DISTINCT CASE WHEN e.status = 'WAITING' THEN e.student_id END) as "Chờ duyệt",
    COUNT(DISTINCT CASE WHEN e.status = 'DENIED' THEN e.student_id END) as "Bị từ chối"
FROM course c
LEFT JOIN enrollment e ON c.id = e.course_id
GROUP BY c.id, c.name, c.duration, c.instructor
ORDER BY COUNT(DISTINCT e.student_id) DESC;

-- ============================================================
-- 4. TOP 5 KHÓA HỌC ĐÔ NHẤT
-- ============================================================

SELECT 
    c.id,
    c.name as "Tên khóa học",
    c.instructor as "Giảng viên",
    COUNT(DISTINCT CASE WHEN e.status = 'CONFIRM' THEN e.student_id END) as "Số học viên"
FROM course c
LEFT JOIN enrollment e ON c.id = e.course_id AND e.status = 'CONFIRM'
GROUP BY c.id, c.name, c.instructor
ORDER BY COUNT(DISTINCT e.student_id) DESC
LIMIT 5;

-- ============================================================
-- 5. ĐƠN CHỜ DUYỆT
-- ============================================================

-- Xem tất cả đơn đăng ký chờ duyệt
SELECT 
    e.id,
    s.name as "Tên học viên",
    s.email,
    c.name as "Tên khóa học",
    e.registered_at as "Ngày đăng ký",
    e.status
FROM enrollment e
JOIN student s ON e.student_id = s.id
JOIN course c ON e.course_id = c.id
WHERE e.status = 'WAITING'
ORDER BY e.registered_at ASC;

-- ============================================================
-- 6. GỢI Ý KHÓA HỌC CHO HỌC VIÊN
-- ============================================================

-- Gợi ý khóa học cho học viên ID = 1
-- (Dựa trên khóa học của những học viên có cùng sở thích)
SELECT DISTINCT c.id, c.name as "Tên khóa học", c.instructor
FROM course c
WHERE c.id NOT IN (
    -- Loại các khóa học học viên đã đăng ký
    SELECT DISTINCT course_id 
    FROM enrollment 
    WHERE student_id = 1
)
AND c.id IN (
    -- Chỉ lấy những khóa học có người cùng kiểu học viên đã đăng ký
    SELECT DISTINCT course_id
    FROM enrollment e
    WHERE e.student_id IN (
        -- Tìm những học viên cùng tuổi (±2 năm)
        SELECT s2.id
        FROM student s2, student s1
        WHERE s1.id = 1
        AND ABS(YEAR(s2.dob) - YEAR(s1.dob)) <= 2
        AND s2.id != s1.id
    )
    AND e.status = 'CONFIRM'
)
LIMIT 5;

-- ============================================================
-- 7. CÂU CHUYỆN ĐỀN HỌC VIÊN
-- ============================================================

-- Lịch sử đăng ký của học viên (ID = 1)
SELECT 
    s.name as "Tên học viên",
    c.name as "Tên khóa học",
    c.instructor as "Giảng viên",
    c.duration as "Thời lượng (giờ)",
    e.status as "Trạng thái",
    e.registered_at as "Ngày đăng ký"
FROM enrollment e
JOIN student s ON e.student_id = s.id
JOIN course c ON e.course_id = c.id
WHERE s.id = 1
ORDER BY e.registered_at DESC;

-- ============================================================
-- 8. TÌAN KIẾM
-- ============================================================

-- Tìm học viên theo tên (Contains search)
SELECT * FROM student 
WHERE name ILIKE '%Nguyễn%'
ORDER BY name;

-- Tìm khóa học theo tên
SELECT * FROM course
WHERE name ILIKE '%Java%'
ORDER BY name;

-- Tìm học viên bằng email
SELECT * FROM student
WHERE email = 'nguyenvana@email.com';

-- ============================================================
-- 9. CẬP NHẬT TRẠNG THÁI ĐƠN ĐĂNG KÝ
-- ============================================================

-- Duyệt đơn (chuyển từ WAITING → CONFIRM)
-- UPDATE enrollment SET status = 'CONFIRM' WHERE id = 1;

-- Từ chối đơn (chuyển từ WAITING → DENIED)
-- UPDATE enrollment SET status = 'DENIED' WHERE id = 1;

-- Hủy đơn (chuyển thành CANCEL)
-- UPDATE enrollment SET status = 'CANCEL' WHERE id = 1;

-- ============================================================
-- 10. XÓA DỮ LIỆU (CẨN THẬN!)
-- ============================================================

-- Xóa học viên ID = 1 (CASCADE xóa tất cả enrollment của họ)
-- DELETE FROM student WHERE id = 1;

-- Xóa khóa học ID = 1 (CASCADE xóa tất cả enrollment của khóa)
-- DELETE FROM course WHERE id = 1;

-- Xóa tất cả dữ liệu nhưng giữ cấu trúc bảng
-- DELETE FROM enrollment;
-- DELETE FROM student;
-- DELETE FROM course;
-- DELETE FROM admin;

-- ============================================================
-- 11. RESET AUTO_INCREMENT (sau khi xóa dữ liệu)
-- ============================================================

-- Reset ID cho bảng student
-- ALTER SEQUENCE student_id_seq RESTART WITH 1;

-- Reset ID cho bảng course
-- ALTER SEQUENCE course_id_seq RESTART WITH 1;

-- Reset ID cho bảng enrollment
-- ALTER SEQUENCE enrollment_id_seq RESTART WITH 1;
