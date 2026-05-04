package views;

import services.CourseService;
import services.EnrollmentService;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AdminEnrollmentManagement {
    private Scanner scanner;
    
    public AdminEnrollmentManagement(Scanner scanner) {
        this.scanner = scanner;
    }
    
    public void start() {
        int choice = -1;
        
        while (true) {
            System.out.println("\n========= QUAN LY DON DANG KY =========");
            System.out.println("1. Xem danh sach don dang ky");
            System.out.println("2. Xem don theo khoa hoc");
            System.out.println("3. Duyet don dang ky");
            System.out.println("4. Tu choi don dang ky");
            System.out.println("5. Xoa hoc vien khoi khoa hoc");
            System.out.println("6. Quay lai");
            System.out.println("======================================");
            System.out.print("Nhap lua chon: ");
            
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Vui long nhap so hop le!");
                continue;
            }
            
            switch (choice) {
                case 1: viewAllEnrollments(); break;
                case 2: viewEnrollmentsByCourse(); break;
                case 3: approveEnrollment(); break;
                case 4: denyEnrollment(); break;
                case 5: removeStudent(); break;
                case 6: return;
                default: System.out.println("Lua chon khong hop le!");
            }
        }
    }
    
    private void viewAllEnrollments() {
        System.out.println("\n========= DANH SACH TAN CA DON DANG KY =========");
        
        List<Map<String, Object>> enrollments = EnrollmentService.getAllEnrollments();
        EnrollmentService.printEnrollments(enrollments);
    }
    
    private void viewEnrollmentsByCourse() {
        System.out.println("\n========= DANH SACH DON THEO KHOA HOC =========");
        
        System.out.print("Nhap ID khoa hoc: ");
        int courseId;
        try {
            courseId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID phai la so!");
            return;
        }
        
        if (!CourseService.existsCourse(courseId)) {
            System.out.println("Khoa hoc khong ton tai!");
            return;
        }
        
        List<Map<String, Object>> enrollments = EnrollmentService.getEnrollmentsByCourse(courseId);
        
        if (enrollments.isEmpty()) {
            System.out.println("Khong co don dang ky nao cho khoa hoc nay!");
        } else {
            System.out.println("\nID | Ten | Email | Trang thai");
            System.out.println("---|-----|-------|----------");
            
            for (Map<String, Object> e : enrollments) {
                String status = e.get("status").toString().equals("WAITING") ? "Cho duyet" : 
                               e.get("status").toString().equals("CONFIRM") ? "Xac nhan" : "Tu choi";
                System.out.printf("%2d | %s | %s | %s\n",
                    e.get("id"), e.get("name"), e.get("email"), status);
            }
        }
    }
    
    private void approveEnrollment() {
        System.out.println("\n========= DUYET DON DANG KY =========");
        
        System.out.print("Nhap ID don dang ky: ");
        int enrollmentId;
        try {
            enrollmentId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID phai la so!");
            return;
        }
        
        EnrollmentService.approveEnrollment(enrollmentId);
    }
    
    private void denyEnrollment() {
        System.out.println("\n========= TU CHOI DON DANG KY =========");
        
        System.out.print("Nhap ID don dang ky: ");
        int enrollmentId;
        try {
            enrollmentId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID phai la so!");
            return;
        }
        
        EnrollmentService.denyEnrollment(enrollmentId);
    }
    
    private void removeStudent() {
        System.out.println("\n========= XOA HOC VIEN KHOI KHOA HOC =========");
        
        System.out.print("Nhap ID don dang ky: ");
        int enrollmentId;
        try {
            enrollmentId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID phai la so!");
            return;
        }
        
        System.out.print("Xac nhan xoa? (Y/N): ");
        if (scanner.nextLine().trim().toUpperCase().equals("Y")) {
            EnrollmentService.removeStudentFromCourse(enrollmentId);
        }
    }
}
