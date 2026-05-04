package services;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * AdminService: Xử lý xác thực admin đăng nhập
 */
public class AdminService {
    
    /**
     * Xác thực đăng nhập admin
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     * @return Admin info nếu đúng, null nếu sai
     */
    public static Map<String, Object> login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return null;
        }
        
        Map<String, Object> admin = null;
        String sql = "SELECT id, username FROM admin WHERE username = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username.trim());
            pstmt.setString(2, password); // Trong thực tế nên dùng bcrypt hoặc hash
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                admin = new HashMap<>();
                admin.put("id", rs.getInt("id"));
                admin.put("username", rs.getString("username"));
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi xác thực: " + e.getMessage());
        }
        return admin;
    }
    
    /**
     * Kiểm tra admin tồn tại
     */
    public static boolean existsAdmin(int adminId) {
        String sql = "SELECT id FROM admin WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, adminId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        }
    }
}
