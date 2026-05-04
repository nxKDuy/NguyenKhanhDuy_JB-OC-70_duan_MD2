package views;

import services.AdminService;
import java.util.Map;
import java.util.Scanner;

public class AdminLoginMenu {
    private Scanner scanner;
    private int maxAttempts = 3;
    
    public AdminLoginMenu(Scanner scanner) {
        this.scanner = scanner;
    }
    
    public void start() {
        int attempts = 0;
        
        while (attempts < maxAttempts) {
            System.out.println("\n========= DANG NHAP QUAN TRI VIEN =========");
            System.out.print("Ten dang nhap: ");
            String username = scanner.nextLine().trim();
            
            System.out.print("Mat khau: ");
            String password = scanner.nextLine().trim();
            
            if (username.isEmpty() || password.isEmpty()) {
                System.out.println("Vui long nhap day du thong tin!");
                attempts++;
                continue;
            }
            
            Map<String, Object> admin = AdminService.login(username, password);
            
            if (admin != null) {
                System.out.println("Dang nhap thanh cong!");
                new AdminMenuPanel(scanner).start();
                return;
            } else {
                attempts++;
                System.out.println("Ten dang nhap hoac mat khau sai! (Lan " + attempts + "/" + maxAttempts + ")");
            }
        }
        
        System.out.println("Ban da nhap sai qua nhieu lan. Quay ve menu chinh.");
    }
}
