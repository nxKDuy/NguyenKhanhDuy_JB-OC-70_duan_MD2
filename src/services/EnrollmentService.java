package services;

import java.sql.*;
import java.util.*;

/**
 * EnrollmentService: Xử lý các thao tác CRUD cho đăng ký khóa học
 */
public class EnrollmentService {
    
    /**
     * Lấy tất cả đơn đăng ký
     */
    public static List<Map<String, Object>> getAllEnrollments() {
        List<Map<String, Object>> enrollments = new ArrayList<>();
        String sql = "SELECT e.id, e.student_id, s.name as student_name, e.course_id, c.name as course_name, e.status, e.registered_at " +
                     "FROM enrollment e " +
                     "JOIN student s ON e.student_id = s.id " +
                     "JOIN course c ON e.course_id = c.id " +
                     "ORDER BY e.id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Map<String, Object> enrollment = new HashMap<>();
                enrollment.put("id", rs.getInt("id"));
                enrollment.put("student_id", rs.getInt("student_id"));
                enrollment.put("student_name", rs.getString("student_name"));
                enrollment.put("course_id", rs.getInt("course_id"));
                enrollment.put("course_name", rs.getString("course_name"));
                enrollment.put("status", rs.getString("status"));
                enrollment.put("registered_at", rs.getTimestamp("registered_at"));
                enrollments.add(enrollment);
            }
        } catch (SQLException e) {
            System.out.println("✗ Lỗi khi lấy danh sách đăng ký: " + e.getMessage());
        }
        return enrollments;
    }
    
    /**
     * Lấy danh sách đăng ký theo khóa học
     */
    public static List<Map<String, Object>> getEnrollmentsByCourse(int courseId) {
        List<Map<String, Object>> enrollments = new ArrayList<>();
        String sql = "SELECT e.id, e.student_id, s.name, s.email, e.status, e.registered_at " +
                     "FROM enrollment e " +
                     "JOIN student s ON e.student_id = s.id " +
                     "WHERE e.course_id = ? " +
                     "ORDER BY e.registered_at";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> enrollment = new HashMap<>();
                enrollment.put("id", rs.getInt("id"));
                enrollment.put("student_id", rs.getInt("student_id"));
                enrollment.put("name", rs.getString("name"));
                enrollment.put("email", rs.getString("email"));
                enrollment.put("status", rs.getString("status"));
                enrollment.put("registered_at", rs.getTimestamp("registered_at"));
                enrollments.add(enrollment);
            }
        } catch (SQLException e) {
            System.out.println("✗ Lỗi: " + e.getMessage());
        }
        return enrollments;
    }
    
    /**
     * Lấy danh sách khóa học của học viên
     */
    public static List<Map<String, Object>> getCoursesByStudent(int studentId) {
        List<Map<String, Object>> courses = new ArrayList<>();
        String sql = "SELECT e.id as enrollment_id, c.id, c.name, c.duration, c.instructor, e.status, e.registered_at " +
                     "FROM enrollment e " +
                     "JOIN course c ON e.course_id = c.id " +
                     "WHERE e.student_id = ? " +
                     "ORDER BY e.registered_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> course = new HashMap<>();
                course.put("enrollment_id", rs.getInt("enrollment_id"));
                course.put("id", rs.getInt("id"));
                course.put("name", rs.getString("name"));
                course.put("duration", rs.getInt("duration"));
                course.put("instructor", rs.getString("instructor"));
                course.put("status", rs.getString("status"));
                course.put("registered_at", rs.getTimestamp("registered_at"));
                courses.add(course);
            }
        } catch (SQLException e) {
            System.out.println("✗ Lỗi: " + e.getMessage());
        }
        return courses;
    }
    
    /**
     * Đăng ký khóa học
     */
    public static boolean registerCourse(int studentId, int courseId) {
        // Kiểm tra student và course tồn tại
        if (!StudentService.existsStudent(studentId)) {
            System.out.println("✗ Học viên không tồn tại!");
            return false;
        }
        if (!CourseService.existsCourse(courseId)) {
            System.out.println("✗ Khóa học không tồn tại!");
            return false;
        }
        
        // Kiểm tra đã đăng ký chưa
        String checkSql = "SELECT id FROM enrollment WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
            
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("✗ Bạn đã đăng ký khóa học này rồi!");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("✗ Lỗi: " + e.getMessage());
            return false;
        }
        
        String sql = "INSERT INTO enrollment (student_id, course_id, status) VALUES (?, ?, 'WAITING')";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);
            
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✓ Đăng ký khóa học thành công!");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("✗ Lỗi khi đăng ký: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Duyệt đơn đăng ký
     */
    public static boolean approveEnrollment(int enrollmentId) {
        String sql = "UPDATE enrollment SET status = 'CONFIRM' WHERE id = ? AND status = 'WAITING'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, enrollmentId);
            int rowsUpdated = pstmt.executeUpdate();
            
            if (rowsUpdated > 0) {
                System.out.println("✓ Duyệt đơn thành công!");
                return true;
            } else {
                System.out.println("✗ Không tìm thấy đơn hoặc đơn không ở trạng thái chờ duyệt!");
            }
        } catch (SQLException e) {
            System.out.println("✗ Lỗi: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Từ chối đơn đăng ký
     */
    public static boolean denyEnrollment(int enrollmentId) {
        String sql = "UPDATE enrollment SET status = 'DENIED' WHERE id = ? AND status = 'WAITING'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, enrollmentId);
            int rowsUpdated = pstmt.executeUpdate();
            
            if (rowsUpdated > 0) {
                System.out.println("✓ Từ chối đơn thành công!");
                return true;
            } else {
                System.out.println("✗ Không tìm thấy đơn hoặc đơn không ở trạng thái chờ duyệt!");
            }
        } catch (SQLException e) {
            System.out.println("✗ Lỗi: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Hủy đơn đăng ký (học viên tự hủy nếu chưa được xác nhận)
     */
    public static boolean cancelEnrollment(int enrollmentId) {
        String sql = "UPDATE enrollment SET status = 'CANCEL' WHERE id = ? AND status = 'WAITING'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, enrollmentId);
            int rowsUpdated = pstmt.executeUpdate();
            
            if (rowsUpdated > 0) {
                System.out.println("✓ Hủy đơn thành công!");
                return true;
            } else {
                System.out.println("✗ Không tìm thấy đơn hoặc đơn không ở trạng thái chờ duyệt!");
            }
        } catch (SQLException e) {
            System.out.println("✗ Lỗi: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Xóa học viên khỏi khóa học
     */
    public static boolean removeStudentFromCourse(int enrollmentId) {
        String sql = "DELETE FROM enrollment WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, enrollmentId);
            int rowsDeleted = pstmt.executeUpdate();
            
            if (rowsDeleted > 0) {
                System.out.println("✓ Xóa học viên khỏi khóa học thành công!");
                return true;
            } else {
                System.out.println("✗ Không tìm thấy đơn đăng ký!");
            }
        } catch (SQLException e) {
            System.out.println("✗ Lỗi: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * In danh sách đăng ký dạng bảng
     */
    public static void printEnrollments(List<Map<String, Object>> enrollments) {
        if (enrollments.isEmpty()) {
            System.out.println("Khong co don dang ky nao!");
            return;
        }
        
        System.out.println("\nID | Ten hoc vien | Ten khoa hoc | Trang thai");
        System.out.println("---|--------------|-------------|----------");
        
        for (Map<String, Object> e : enrollments) {
            String status = e.get("status").toString();
            String statusDisplay = status.equals("WAITING") ? "Cho duyet" : 
                                  status.equals("CONFIRM") ? "Xac nhan" :
                                  status.equals("DENIED") ? "Tu choi" : "Huy";
            
            String studentName = e.get("student_name").toString();
            if (studentName.length() > 14) studentName = studentName.substring(0, 14);
            
            String courseName = e.get("course_name").toString();
            if (courseName.length() > 13) courseName = courseName.substring(0, 13);
            
            System.out.printf("%2d | %-14s | %-13s | %s\n",
                e.get("id"),
                studentName,
                courseName,
                statusDisplay);
        }
    }
}
