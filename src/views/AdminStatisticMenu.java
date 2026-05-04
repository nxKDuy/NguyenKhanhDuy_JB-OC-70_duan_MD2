package views;

import java.sql.*;
import java.util.Scanner;
import services.DatabaseConnection;

public class AdminStatisticMenu {
    private Scanner scanner;
    
    public AdminStatisticMenu(Scanner scanner) {
        this.scanner = scanner;
    }
    
    public void start() {
        int choice = -1;
        
        while (true) {
            System.out.println("\n========= THONG KE =========");
            System.out.println("1. Tong khoa hoc & hoc vien");
            System.out.println("2. So hoc vien theo khoa hoc");
            System.out.println("3. Top 5 khoa hoc dong nhat");
            System.out.println("4. Khoa hoc co tren 10 hoc vien");
            System.out.println("5. Quay lai");
            System.out.println("===========================");
            System.out.print("Nhap lua chon: ");
            
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Vui long nhap so hop le!");
                continue;
            }
            
            switch (choice) {
                case 1: statTotal(); break;
                case 2: statByEachCourse(); break;
                case 3: statTop5Courses(); break;
                case 4: statCoursesOver10Students(); break;
                case 5: return;
                default: System.out.println("Lua chon khong hop le!");
            }
        }
    }
    
    private void statTotal() {
        System.out.println("\n========= THONG KE TONG KHOA HOC & HOC VIEN =========");
        
        String sql = "SELECT " +
                     "(SELECT COUNT(*) FROM course) as total_courses, " +
                     "(SELECT COUNT(*) FROM student) as total_students, " +
                     "(SELECT COUNT(*) FROM enrollment) as total_enrollments";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                System.out.println("\nKET QUA THONG KE:");
                System.out.println("========================================");
                System.out.println("Tong khoa hoc: " + rs.getInt("total_courses"));
                System.out.println("Tong hoc vien: " + rs.getInt("total_students"));
                System.out.println("Tong don dang ky: " + rs.getInt("total_enrollments"));
                System.out.println("========================================");
            }
        } catch (SQLException e) {
            System.out.println("Loi: " + e.getMessage());
        }
    }
    
    private void statByEachCourse() {
        System.out.println("\n========= SO HOC VIEN THEO TUNG KHOA HOC =========");
        
        String sql = "SELECT c.id, c.name, " +
                     "COUNT(DISTINCT CASE WHEN e.status = 'CONFIRM' THEN e.student_id END) as total " +
                     "FROM course c " +
                     "LEFT JOIN enrollment e ON c.id = e.course_id AND e.status = 'CONFIRM' " +
                     "GROUP BY c.id, c.name " +
                     "ORDER BY c.id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("\nID | Ten khoa hoc | So HV (Confirm)");
            System.out.println("----|-------------|----------------");
            
            while (rs.next()) {
                String name = rs.getString("name");
                if (name.length() > 15) name = name.substring(0, 15);
                
                System.out.printf("%2d | %-15s | %6d\n",
                    rs.getInt("id"),
                    name,
                    rs.getInt("total"));
            }
        } catch (SQLException e) {
            System.out.println("Loi: " + e.getMessage());
        }
    }
    
    private void statTop5Courses() {
        System.out.println("\n========= TOP 5 KHOA HOC DONG HOC VIEN NHAT =========");
        
        String sql = "SELECT c.id, c.name, c.instructor, " +
                     "COUNT(DISTINCT e.student_id) as student_count " +
                     "FROM course c " +
                     "LEFT JOIN enrollment e ON c.id = e.course_id AND e.status = 'CONFIRM' " +
                     "GROUP BY c.id, c.name, c.instructor " +
                     "ORDER BY student_count DESC " +
                     "LIMIT 5";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("\nXep | Ten khoa hoc | Giang vien | So HV");
            System.out.println("----|-------------|-----------|-------");
            
            int rank = 1;
            while (rs.next()) {
                String name = rs.getString("name");
                if (name.length() > 12) name = name.substring(0, 12);
                String instructor = rs.getString("instructor");
                if (instructor.length() > 10) instructor = instructor.substring(0, 10);
                
                System.out.printf(" %d  | %-12s | %-10s | %5d\n",
                    rank++,
                    name,
                    instructor,
                    rs.getInt("student_count"));
            }
        } catch (SQLException e) {
            System.out.println("Loi: " + e.getMessage());
        }
    }
    
    private void statCoursesOver10Students() {
        System.out.println("\n========= KHOA HOC CO TREN 10 HOC VIEN (CONFIRM) =========");
        
        String sql = "SELECT c.id, c.name, c.instructor, " +
                     "COUNT(DISTINCT e.student_id) as student_count " +
                     "FROM course c " +
                     "LEFT JOIN enrollment e ON c.id = e.course_id AND e.status = 'CONFIRM' " +
                     "GROUP BY c.id, c.name, c.instructor " +
                     "HAVING COUNT(DISTINCT e.student_id) > 10 " +
                     "ORDER BY student_count DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("\nID | Ten khoa hoc | Giang vien | So HV");
            System.out.println("----|-------------|-----------|-------");
            
            int count = 0;
            while (rs.next()) {
                String name = rs.getString("name");
                if (name.length() > 12) name = name.substring(0, 12);
                String instructor = rs.getString("instructor");
                if (instructor.length() > 10) instructor = instructor.substring(0, 10);
                
                System.out.printf("%2d | %-12s | %-10s | %5d\n",
                    rs.getInt("id"),
                    name,
                    instructor,
                    rs.getInt("student_count"));
                count++;
            }
            
            if (count == 0) {
                System.out.println("Khong co khoa hoc nao co tren 10 hoc vien.");
            }
        } catch (SQLException e) {
            System.out.println("Loi: " + e.getMessage());
        }
    }
}
