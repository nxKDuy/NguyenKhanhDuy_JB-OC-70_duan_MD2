package services;

import java.sql.*;
import java.util.*;

/**
 * CourseService: Xử lý các thao tác CRUD cho khóa học
 */
public class CourseService {
    
    /**
     * Lấy tất cả khóa học
     */
    public static List<Map<String, Object>> getAllCourses() {
        List<Map<String, Object>> courses = new ArrayList<>();
        String sql = "SELECT id, name, duration, instructor, create_at FROM course ORDER BY id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Map<String, Object> course = new HashMap<>();
                course.put("id", rs.getInt("id"));
                course.put("name", rs.getString("name"));
                course.put("duration", rs.getInt("duration"));
                course.put("instructor", rs.getString("instructor"));
                course.put("create_at", rs.getDate("create_at"));
                courses.add(course);
            }
        } catch (SQLException e) {
            System.out.println("✗ Lỗi khi lấy danh sách khóa học: " + e.getMessage());
        }
        return courses;
    }
    
    /**
     * Lấy khóa học theo ID
     */
    public static Map<String, Object> getCourseById(int courseId) {
        Map<String, Object> course = null;
        String sql = "SELECT id, name, duration, instructor, create_at FROM course WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                course = new HashMap<>();
                course.put("id", rs.getInt("id"));
                course.put("name", rs.getString("name"));
                course.put("duration", rs.getInt("duration"));
                course.put("instructor", rs.getString("instructor"));
                course.put("create_at", rs.getDate("create_at"));
            }
        } catch (SQLException e) {
            System.out.println("✗ Lỗi khi lấy khóa học: " + e.getMessage());
        }
        return course;
    }
    
    /**
     * Thêm khóa học mới
     */
    public static boolean addCourse(String name, int duration, String instructor) {
        if (name == null || name.trim().isEmpty() || duration <= 0 || instructor == null || instructor.trim().isEmpty()) {
            System.out.println("✗ Vui lòng nhập đủ thông tin!");
            return false;
        }
        
        String sql = "INSERT INTO course (name, duration, instructor) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name.trim());
            pstmt.setInt(2, duration);
            pstmt.setString(3, instructor.trim());
            
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✓ Thêm khóa học thành công!");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("✗ Lỗi khi thêm khóa học: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Cập nhật khóa học
     */
    public static boolean updateCourse(int courseId, String name, int duration, String instructor) {
        if (name == null || name.trim().isEmpty() || duration <= 0 || instructor == null || instructor.trim().isEmpty()) {
            System.out.println("✗ Vui lòng nhập đủ thông tin!");
            return false;
        }
        
        String sql = "UPDATE course SET name = ?, duration = ?, instructor = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name.trim());
            pstmt.setInt(2, duration);
            pstmt.setString(3, instructor.trim());
            pstmt.setInt(4, courseId);
            
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✓ Cập nhật khóa học thành công!");
                return true;
            } else {
                System.out.println("✗ Không tìm thấy khóa học để cập nhật!");
            }
        } catch (SQLException e) {
            System.out.println("✗ Lỗi khi cập nhật khóa học: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Xóa khóa học
     */
    public static boolean deleteCourse(int courseId) {
        String sql = "DELETE FROM course WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, courseId);
            int rowsDeleted = pstmt.executeUpdate();
            
            if (rowsDeleted > 0) {
                System.out.println("✓ Xóa khóa học thành công!");
                return true;
            } else {
                System.out.println("✗ Không tìm thấy khóa học để xóa!");
            }
        } catch (SQLException e) {
            System.out.println("✗ Lỗi khi xóa khóa học: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Tìm kiếm khóa học theo tên (tìm kiếm tương đối)
     */
    public static List<Map<String, Object>> searchCourseByName(String searchName) {
        List<Map<String, Object>> courses = new ArrayList<>();
        String sql = "SELECT id, name, duration, instructor, create_at FROM course WHERE LOWER(name) LIKE LOWER(?) ORDER BY id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + searchName + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> course = new HashMap<>();
                course.put("id", rs.getInt("id"));
                course.put("name", rs.getString("name"));
                course.put("duration", rs.getInt("duration"));
                course.put("instructor", rs.getString("instructor"));
                course.put("create_at", rs.getDate("create_at"));
                courses.add(course);
            }
        } catch (SQLException e) {
            System.out.println("✗ Lỗi khi tìm kiếm: " + e.getMessage());
        }
        return courses;
    }
    
    /**
     * Sắp xếp khóa học (sortBy: 1=ID, 2=Tên; order: 1=ASC, 2=DESC)
     */
    public static List<Map<String, Object>> sortCourses(int sortBy, int order) {
        List<Map<String, Object>> courses = new ArrayList<>();
        String sortColumn = (sortBy == 1) ? "id" : "name";
        String orderBy = (order == 1) ? "ASC" : "DESC";
        String sql = "SELECT id, name, duration, instructor, create_at FROM course ORDER BY " + sortColumn + " " + orderBy;
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Map<String, Object> course = new HashMap<>();
                course.put("id", rs.getInt("id"));
                course.put("name", rs.getString("name"));
                course.put("duration", rs.getInt("duration"));
                course.put("instructor", rs.getString("instructor"));
                course.put("create_at", rs.getDate("create_at"));
                courses.add(course);
            }
        } catch (SQLException e) {
            System.out.println("✗ Lỗi khi sắp xếp: " + e.getMessage());
        }
        return courses;
    }
    
    /**
     * In danh sách khóa học dạng bảng
     */
    public static void printCourses(List<Map<String, Object>> courses) {
        if (courses.isEmpty()) {
            System.out.println("Khong co khoa hoc nao!");
            return;
        }
        
        System.out.println("\nID | Ten khoa hoc | Thoi luong (gio) | Giang vien");
        System.out.println("---|--------------|-----------------|----------");
        
        for (Map<String, Object> course : courses) {
            String name = course.get("name").toString();
            if (name.length() > 18) name = name.substring(0, 18);
            
            String instructor = course.get("instructor").toString();
            if (instructor.length() > 12) instructor = instructor.substring(0, 12);
            
            System.out.printf("%2d | %-18s | %-15d | %s\n",
                course.get("id"),
                name,
                course.get("duration"),
                instructor);
        }
    }
    
    /**
     * Kiểm tra khóa học có tồn tại không
     */
    public static boolean existsCourse(int courseId) {
        return getCourseById(courseId) != null;
    }
}
