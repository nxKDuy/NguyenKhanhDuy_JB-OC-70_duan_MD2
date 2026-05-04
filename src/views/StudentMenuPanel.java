package views;

import services.CourseService;
import services.EnrollmentService;
import services.StudentService;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class StudentMenuPanel {
    private Scanner scanner;
    private int studentId;
    
    public StudentMenuPanel(Scanner scanner, int studentId) {
        this.scanner = scanner;
        this.studentId = studentId;
    }
    
    public void start() {
        int choice = -1;
        
        while (true) {
            System.out.println("\n========= MENU HOC VIEN =========");
            System.out.println("1. Xem danh sach khoa hoc");
            System.out.println("2. Xem khoa hoc da dang ky");
            System.out.println("3. Dang ky khoa hoc");
            System.out.println("4. Huy dang ky");
            System.out.println("5. Doi mat khau");
            System.out.println("6. Dang xuat");
            System.out.println("================================");
            System.out.print("Nhap lua chon: ");
            
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Vui long nhap so hop le!");
                continue;
            }
            
            switch (choice) {
                case 1: viewAvailableCourses(); break;
                case 2: viewRegisteredCourses(); break;
                case 3: registerCourse(); break;
                case 4: cancelRegistration(); break;
                case 5: changePassword(); break;
                case 6:
                    System.out.println("Dang xuat thanh cong!");
                    return;
                default:
                    System.out.println("Lua chon khong hop le!");
            }
        }
    }
    
    private void viewAvailableCourses() {
        System.out.println("\n========= DANH SACH KHOA HOC KHAC =========");
        
        List<Map<String, Object>> courses = CourseService.getAllCourses();
        CourseService.printCourses(courses);
    }
    
    private void viewRegisteredCourses() {
        System.out.println("\n========= KHOA HOC DA DANG KY =========");
        
        List<Map<String, Object>> courses = EnrollmentService.getCoursesByStudent(studentId);
        
        if (courses.isEmpty()) {
            System.out.println("Ban chua dang ky khoa hoc nao!");
        } else {
            System.out.println("\nID | Ten khoa hoc | Thoi luong | Trang thai");
            System.out.println("----|-------------|-----------|----------");
            
            for (Map<String, Object> c : courses) {
                String status = c.get("status").toString().equals("WAITING") ? "Cho duyet" :
                               c.get("status").toString().equals("CONFIRM") ? "Xac nhan" : "Tu choi";
                String name = c.get("name").toString();
                if (name.length() > 15) name = name.substring(0, 15);
                
                System.out.printf("%2d | %-15s | %9d | %s\n",
                    c.get("id"),
                    name,
                    c.get("duration"),
                    status);
            }
        }
    }
    
    private void registerCourse() {
        System.out.println("\n========= DANG KY KHOA HOC =========");
        
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
        
        EnrollmentService.registerCourse(studentId, courseId);
    }
    
    private void cancelRegistration() {
        System.out.println("\n========= HUY DANG KY KHOA HOC =========");
        
        System.out.print("Nhap ID don dang ky: ");
        int enrollmentId;
        try {
            enrollmentId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID phai la so!");
            return;
        }
        
        System.out.print("Xac nhan huy? (Y/N): ");
        if (scanner.nextLine().trim().toUpperCase().equals("Y")) {
            EnrollmentService.cancelEnrollment(enrollmentId);
        }
    }
    
    private void changePassword() {
        System.out.println("\n========= DOI MAT KHAU =========");
        
        System.out.print("Mat khau cu: ");
        String oldPassword = scanner.nextLine().trim();
        
        Map<String, Object> student = StudentService.getStudentById(studentId);
        if (student == null || student.get("password") == null) {
            System.out.println("Khong the lay thong tin hoc vien!");
            return;
        }
        
        if (!student.get("password").equals(oldPassword)) {
            System.out.println("Mat khau cu khong dung!");
            return;
        }
        
        System.out.print("Mat khau moi: ");
        String newPassword = scanner.nextLine().trim();
        
        System.out.print("Xac nhan mat khau: ");
        String confirmPassword = scanner.nextLine().trim();
        
        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Mat khau xac nhan khong trung!");
            return;
        }
        
        if (StudentService.changePassword(studentId, newPassword)) {
            System.out.println("Doi mat khau thanh cong!");
        }
    }
}
