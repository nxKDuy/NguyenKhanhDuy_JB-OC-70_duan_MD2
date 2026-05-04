package views;

import java.util.Scanner;

public class AdminMenuPanel {
    private Scanner scanner;
    
    public AdminMenuPanel(Scanner scanner) {
        this.scanner = scanner;
    }
    
    public void start() {
        int choice = -1;
        
        while (true) {
            System.out.println("\n========= MENU QUAN LY ADMIN =========");
            System.out.println("1. Quan ly Khoa hoc");
            System.out.println("2. Quan ly Hoc vien");
            System.out.println("3. Quan ly Don dang ky");
            System.out.println("4. Thong ke");
            System.out.println("5. Dang xuat");
            System.out.println("=====================================");
            System.out.print("Nhap lua chon: ");
            
            String input = scanner.nextLine();
            
            if (input.trim().isEmpty()) {
                System.out.println("Lua chon khong duoc de trong!");
                continue;
            }
            
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Vui long nhap mot so hop le!");
                continue;
            }
            
            switch (choice) {
                case 1:
                    new AdminCourseManagement(scanner).start();
                    break;
                case 2:
                    new AdminStudentManagement(scanner).start();
                    break;
                case 3:
                    new AdminEnrollmentManagement(scanner).start();
                    break;
                case 4:
                    new AdminStatisticMenu(scanner).start();
                    break;
                case 5:
                    System.out.println("Dang xuat thanh cong!");
                    return;
                default:
                    System.out.println("Lua chon khong hop le!");
            }
        }
    }
}

