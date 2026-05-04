package views;

import services.DatabaseConnection;
import java.util.Scanner;

public class MainMenu {
    private Scanner scanner;

    public MainMenu() {
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        int choice = -1;

        while (true) {
            System.out.println("\n========= HE THONG QUAN LY DAO TAO =========");
            System.out.println("1. Dang nhap Quan tri vien");
            System.out.println("2. Dang nhap Hoc vien");
            System.out.println("3. Thoat");
            System.out.println("============================================");
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
                    new AdminLoginMenu(scanner).start();
                    break;
                case 2:
                    new StudentLoginMenu(scanner).start();
                    break;
                case 3:
                    System.out.println("Cam on ban da su dung he thong!");
                    DatabaseConnection.closeConnection();
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Lua chon khong hop le, vui long chon tu 1-3!");
            }
        }
    }
}
