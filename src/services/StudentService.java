package services;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class StudentService: Xử lý các thao tác liên quan đến học viên
 * - Lấy danh sách học viên
 * - Thêm, cập nhật, xóa học viên
 * - Tìm kiếm học viên theo ID hoặc email
 */
public class StudentService {
    
    /**
     * Lấy tất cả học viên
     */
    public static List<Map<String, Object>> getAllStudents() {
        List<Map<String, Object>> students = new ArrayList<>();
        String sql = "SELECT id, name, email, dob, sex, phone, create_at FROM student ORDER BY id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Map<String, Object> student = new HashMap<>();
                student.put("id", rs.getInt("id"));
                student.put("name", rs.getString("name"));
                student.put("email", rs.getString("email"));
                student.put("dob", rs.getDate("dob"));
                student.put("sex", rs.getBoolean("sex") ? "Nam" : "Nữ");
                student.put("phone", rs.getString("phone"));
                student.put("create_at", rs.getDate("create_at"));
                students.add(student);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách học viên:");
            e.printStackTrace();
        }
        return students;
    }
    
    /**
     * Lấy thông tin học viên theo ID
     */
    public static Map<String, Object> getStudentById(int studentId) {
        Map<String, Object> student = null;
        String sql = "SELECT id, name, email, dob, sex, phone, password, create_at FROM student WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                student = new HashMap<>();
                student.put("id", rs.getInt("id"));
                student.put("name", rs.getString("name"));
                student.put("email", rs.getString("email"));
                student.put("dob", rs.getDate("dob"));
                student.put("sex", rs.getBoolean("sex") ? "Nam" : "Nữ");
                student.put("phone", rs.getString("phone"));
                student.put("password", rs.getString("password"));
                student.put("create_at", rs.getDate("create_at"));
            } else {
                System.out.println("Không tìm thấy học viên có ID: " + studentId);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy thông tin học viên:");
            e.printStackTrace();
        }
        return student;
    }
    
    /**
     * Thêm học viên mới
     */
    public static boolean addStudent(String name, String dob, String email, int sex, String phone, String password) {
        String sql = "INSERT INTO student (name, dob, email, sex, phone, password) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setDate(2, java.sql.Date.valueOf(dob)); // Format: "2005-01-15"
            pstmt.setString(3, email);
            pstmt.setBoolean(4, sex == 1); // 1: Nam (true), 0: Nữ (false)
            pstmt.setString(5, phone);
            pstmt.setString(6, password);
            
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Thêm học viên thành công!");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm học viên:");
            if (e.getMessage().contains("duplicate key")) {
                System.out.println("  Email này đã tồn tại!");
            }
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Cập nhật thông tin học viên
     */
    public static boolean updateStudent(int studentId, String name, String dob, String email, int sex, String phone) {
        String sql = "UPDATE student SET name = ?, dob = ?, email = ?, sex = ?, phone = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setDate(2, java.sql.Date.valueOf(dob));
            pstmt.setString(3, email);
            pstmt.setBoolean(4, sex == 1); // 1: Nam (true), 0: Nữ (false)
            pstmt.setString(5, phone);
            pstmt.setInt(6, studentId);
            
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Cập nhật học viên thành công!");
                return true;
            } else {
                System.out.println("Không tìm thấy học viên để cập nhật!");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật học viên:");
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Xóa học viên
     */
    public static boolean deleteStudent(int studentId) {
        String sql = "DELETE FROM student WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            int rowsDeleted = pstmt.executeUpdate();
            
            if (rowsDeleted > 0) {
                System.out.println("✓ Xóa học viên thành công!");
                return true;
            } else {
                System.out.println("✗ Không tìm thấy học viên để xóa!");
            }
        } catch (SQLException e) {
            System.out.println("✗ Lỗi khi xóa học viên:");
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Tìm học viên theo email (dùng cho login)
     */
    public static Map<String, Object> getStudentByEmail(String email) {
        Map<String, Object> student = null;
        String sql = "SELECT id, name, email, password FROM student WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                student = new HashMap<>();
                student.put("id", rs.getInt("id"));
                student.put("name", rs.getString("name"));
                student.put("email", rs.getString("email"));
                student.put("password", rs.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println("✗ Lỗi khi tìm học viên:");
            e.printStackTrace();
        }
        return student;
    }
    
    /**
     * Đổi mật khẩu học viên
     */
    public static boolean changePassword(int studentId, String newPassword) {
        String sql = "UPDATE student SET password = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, studentId);
            
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✓ Đổi mật khẩu thành công!");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("✗ Lỗi khi đổi mật khẩu:");
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * In danh sách học viên ra console (dùng cho menu)
     */
    public static void printAllStudents() {
        List<Map<String, Object>> students = getAllStudents();
        if (students.isEmpty()) {
            System.out.println("Khong co hoc vien nao!");
            return;
        }
        
        System.out.println("\nID | Ten | Email | Gioi tinh | Dien thoai | Ngay tao");
        System.out.println("----|-----|-------|-----------|-----------|----------");
        
        for (Map<String, Object> student : students) {
            String name = student.get("name").toString();
            if (name.length() > 15) name = name.substring(0, 15);
            
            String email = student.get("email").toString();
            if (email.length() > 15) email = email.substring(0, 15);
            
            String phone = student.get("phone") != null ? student.get("phone").toString() : "";
            if (phone.length() > 12) phone = phone.substring(0, 12);
            
            java.util.Date createAt = (java.util.Date) student.get("create_at");
            String dateStr = formatDate(createAt, "dd-MM-yy");
            
            System.out.printf("%2d | %-15s | %-15s | %-9s | %-12s | %s\n",
                student.get("id"),
                name,
                email,
                student.get("sex"),
                phone,
                dateStr);
        }
    }
    
    /**
     * Tìm kiếm học viên theo tên (tìm kiếm tương đối)
     */
    public static List<Map<String, Object>> searchStudentByName(String searchName) {
        List<Map<String, Object>> students = new ArrayList<>();
        String sql = "SELECT id, name, email, dob, sex, phone, create_at FROM student WHERE LOWER(name) LIKE LOWER(?) ORDER BY id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + searchName + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> student = new HashMap<>();
                student.put("id", rs.getInt("id"));
                student.put("name", rs.getString("name"));
                student.put("email", rs.getString("email"));
                student.put("dob", rs.getDate("dob"));
                student.put("sex", rs.getBoolean("sex") ? "Nam" : "Nữ");
                student.put("phone", rs.getString("phone"));
                student.put("create_at", rs.getDate("create_at"));
                students.add(student);
            }
        } catch (SQLException e) {
            System.out.println("✗ Lỗi khi tìm kiếm: " + e.getMessage());
        }
        return students;
    }
    
    /**
     * Tìm kiếm học viên theo email
     */
    public static List<Map<String, Object>> searchStudentByEmail(String searchEmail) {
        List<Map<String, Object>> students = new ArrayList<>();
        String sql = "SELECT id, name, email, dob, sex, phone, create_at FROM student WHERE LOWER(email) LIKE LOWER(?) ORDER BY id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + searchEmail + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> student = new HashMap<>();
                student.put("id", rs.getInt("id"));
                student.put("name", rs.getString("name"));
                student.put("email", rs.getString("email"));
                student.put("dob", rs.getDate("dob"));
                student.put("sex", rs.getBoolean("sex") ? "Nam" : "Nữ");
                student.put("phone", rs.getString("phone"));
                student.put("create_at", rs.getDate("create_at"));
                students.add(student);
            }
        } catch (SQLException e) {
            System.out.println("✗ Lỗi khi tìm kiếm: " + e.getMessage());
        }
        return students;
    }
    
    /**
     * Sắp xếp học viên (sortBy: 1=ID, 2=Tên; order: 1=ASC, 2=DESC)
     */
    public static List<Map<String, Object>> sortStudents(int sortBy, int order) {
        List<Map<String, Object>> students = new ArrayList<>();
        String sortColumn = (sortBy == 1) ? "id" : "name";
        String orderBy = (order == 1) ? "ASC" : "DESC";
        String sql = "SELECT id, name, email, dob, sex, phone, create_at FROM student ORDER BY " + sortColumn + " " + orderBy;
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Map<String, Object> student = new HashMap<>();
                student.put("id", rs.getInt("id"));
                student.put("name", rs.getString("name"));
                student.put("email", rs.getString("email"));
                student.put("dob", rs.getDate("dob"));
                student.put("sex", rs.getBoolean("sex") ? "Nam" : "Nữ");
                student.put("phone", rs.getString("phone"));
                student.put("create_at", rs.getDate("create_at"));
                students.add(student);
            }
        } catch (SQLException e) {
            System.out.println("✗ Lỗi khi sắp xếp: " + e.getMessage());
        }
        return students;
    }
    
    /**
     * Kiểm tra học viên có tồn tại không
     */
    public static boolean existsStudent(int studentId) {
        return getStudentById(studentId) != null;
    }
    
    /**
     * Kiểm tra email hợp lệ
     */
    public static boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email != null && !email.isEmpty() && email.matches(regex);
    }
    
    /**
     * Kiểm tra số điện thoại hợp lệ (10-11 chữ số, bắt đầu bằng 0)
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^0\\d{9,10}$");
    }
    
    /**
     * Kiểm tra định dạng ngày (DD-MM-YYYY)
     */
    public static boolean isValidDate(String dateStr) {
        if (!dateStr.matches("^\\d{2}-\\d{2}-\\d{4}$")) {
            return false;
        }
        
        try {
            String[] parts = dateStr.split("-");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);
            
            if (month < 1 || month > 12) return false;
            if (day < 1 || day > 31) return false;
            
            int[] daysInMonth = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                daysInMonth[2] = 29;
            }
            
            return day <= daysInMonth[month];
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Chuyển đổi từ định dạng DD-MM-YYYY sang YYYY-MM-DD để lưu vào DB
     */
    public static String convertDateToDB(String dateStr) {
        String[] parts = dateStr.split("-");
        return parts[2] + "-" + parts[1] + "-" + parts[0];
    }
    
    /**
     * Format ngày tháng
     */
    public static String formatDate(java.util.Date date, String format) {
        if (date == null) return "-";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
        return sdf.format(date);
    }
}
