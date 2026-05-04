package services;

import java.util.Scanner;

public class AuthService {
    
    /**
     * Ham xu ly chuc nang dang nhap cua Quan tri vien (Admin)
     * @return true neu dang nhap thanh cong, false neu nguoi dung muon quay lai menu truoc
     */
    public boolean loginAdmin(Scanner scanner) {
        while (true) {
            System.out.print("Nhap mat khau Admin (hoac 'q' de quay lai): ");
            String password = scanner.nextLine();
            
            // Validate: khong duoc de trong mat khau
            if (password.trim().isEmpty()) {
                System.out.println("Mat khau khong duoc de trong, vui long nhap lai!");
                continue; // Yeu cau nhap lai
            }
            
            // Cho nguoi dung quay lai menu truoc
            if (password.equalsIgnoreCase("q")) {
                return false; 
            }
            
            // Gia dinh mat khau mac dinh la "admin" (co the thay doi sau nay)
            if (password.equals("admin")) {
                System.out.println("Dang nhap thanh cong vao he thong Admin!");
                return true; // Password dung -> Cho phep tiep tuc
            } else {
                System.out.println("Mat khau sai, vui long nhap lai!");
            }
        }
    }
}
