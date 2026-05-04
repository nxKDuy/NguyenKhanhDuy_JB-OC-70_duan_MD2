package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/duanmd2";
    private static final String USER = "postgres";
    private static final String PASSWORD = "388999";
    private static final String DRIVER = "org.postgresql.Driver";
    
    private static Connection connection;

    public static Connection getConnection() {
        try {
            // Load driver PostgreSQL
            Class.forName(DRIVER);
            
            // Kiểm tra nếu chưa kết nối hoặc kết nối đã đóng
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    
    /**
     * Đóng kết nối database
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Kiểm tra kết nối có hoạt động không
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
