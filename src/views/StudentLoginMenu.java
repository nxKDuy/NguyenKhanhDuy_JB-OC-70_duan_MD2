package views;

import services.StudentService;
import java.util.Map;
import java.util.Scanner;

public class StudentLoginMenu {
    private Scanner scanner;
    private int maxAttempts = 3;
    
    public StudentLoginMenu(Scanner scanner) {
        this.scanner = scanner;
    }
    
    public void start() {
        int attempts = 0;
        
        while (attempts < maxAttempts) {
            System.out.println("\n========= DANG NHAP HOC VIEN =========");
            
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            
            System.out.print("Mat khau: ");
            String password = scanner.nextLine().trim();
            
            if (email.isEmpty() || password.isEmpty()) {
                System.out.println("Vui long nhap day du thong tin!");
                attempts++;
                continue;
            }
            
            Map<String, Object> student = StudentService.getStudentByEmail(email);
            
            if (student != null && student.get("password").equals(password)) {
                System.out.println("Dang nhap thanh cong!");
                new StudentMenuPanel(scanner, (int)student.get("id")).start();
                return;
            } else {
                attempts++;
                System.out.println("Email hoac mat khau sai! (Lan " + attempts + "/" + maxAttempts + ")");
            }
        }
        
        System.out.println("Ban da nhap sai qua nhieu lan. Quay ve menu chinh.");
    }
}
