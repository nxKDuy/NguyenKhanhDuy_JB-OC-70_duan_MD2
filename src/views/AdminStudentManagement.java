package views;

import services.StudentService;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AdminStudentManagement {
    private Scanner scanner;
    
    public AdminStudentManagement(Scanner scanner) {
        this.scanner = scanner;
    }
    
    public void start() {
        int choice = -1;
        
        while (true) {
            System.out.println("\n========= QUAN LY HOC VIEN =========");
            System.out.println("1. Xem danh sach hoc vien");
            System.out.println("2. Them hoc vien moi");
            System.out.println("3. Chinh sua hoc vien");
            System.out.println("4. Xoa hoc vien");
            System.out.println("5. Tim kiem hoc vien");
            System.out.println("6. Sap xep hoc vien");
            System.out.println("7. Quay lai");
            System.out.println("==================================");
            System.out.print("Nhap lua chon: ");
            
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Vui long nhap so hop le!");
                continue;
            }
            
            switch (choice) {
                case 1: viewStudents(); break;
                case 2: addStudent(); break;
                case 3: editStudent(); break;
                case 4: deleteStudent(); break;
                case 5: searchStudent(); break;
                case 6: sortStudents(); break;
                case 7: return;
                default: System.out.println("Lua chon khong hop le!");
            }
        }
    }
    
    private void viewStudents() {
        System.out.println("\n========= DANH SACH HOC VIEN =========");
        StudentService.printAllStudents();
    }
    
    private void addStudent() {
        System.out.println("\n========= THEM HOC VIEN MOI =========");
        
        System.out.print("Ten: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Ten khong duoc de trong!");
            return;
        }
        
        System.out.print("Ngay sinh (DD-MM-YYYY): ");
        String dob = scanner.nextLine().trim();
        if (!StudentService.isValidDate(dob)) {
            System.out.println("Dinh dang ngay khong hop le! Vui long nhap DD-MM-YYYY (vd: 15-03-2005)");
            return;
        }
        String dobDB = StudentService.convertDateToDB(dob);
        
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        if (!StudentService.isValidEmail(email)) {
            System.out.println("Email khong hop le!");
            return;
        }
        
        System.out.print("Gioi tinh (1=Nam/0=Nu): ");
        int sex;
        try {
            sex = Integer.parseInt(scanner.nextLine().trim());
            if (sex != 0 && sex != 1) {
                System.out.println("Gioi tinh phai la 0 hoac 1!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Gioi tinh phai la so!");
            return;
        }
        
        System.out.print("So dien thoai: ");
        String phone = scanner.nextLine().trim();
        if (!StudentService.isValidPhone(phone)) {
            System.out.println("So dien thoai khong hop le! (10-11 chu so, bat dau bang 0)");
            return;
        }
        
        System.out.print("Mat khau: ");
        String password = scanner.nextLine().trim();
        if (password.isEmpty()) {
            System.out.println("Mat khau khong duoc de trong!");
            return;
        }
        
        StudentService.addStudent(name, dobDB, email, sex, phone, password);
    }
    
    private void editStudent() {
        System.out.println("\n========= CHINH SUA HOC VIEN =========");
        
        System.out.print("Nhap ID hoc vien: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID phai la so!");
            return;
        }
        
        Map<String, Object> student = StudentService.getStudentById(id);
        if (student == null) {
            System.out.println("Hoc vien khong ton tai!");
            return;
        }
        
        System.out.println("\nThong tin hien tai:");
        System.out.println("1. Ten: " + student.get("name"));
        System.out.println("2. Email: " + student.get("email"));
        System.out.println("3. So dien thoai: " + student.get("phone"));
        System.out.println("4. Quay lai");
        System.out.print("Chon: ");
        
        String choice = scanner.nextLine().trim();
        
        String name = student.get("name").toString();
        String dob = student.get("dob").toString();
        String email = student.get("email").toString();
        int sex = ((Number) student.get("sex")).intValue();
        String phone = student.get("phone") != null ? student.get("phone").toString() : "";
        
        switch (choice) {
            case "1":
                System.out.print("Ten moi: ");
                String newName = scanner.nextLine().trim();
                if (newName.isEmpty()) {
                    System.out.println("Ten khong duoc de trong!");
                    return;
                }
                name = newName;
                break;
            case "2":
                System.out.print("Email moi: ");
                String newEmail = scanner.nextLine().trim();
                if (!StudentService.isValidEmail(newEmail)) {
                    System.out.println("Email khong hop le!");
                    return;
                }
                email = newEmail;
                break;
            case "3":
                System.out.print("So dien thoai moi: ");
                String newPhone = scanner.nextLine().trim();
                if (!StudentService.isValidPhone(newPhone)) {
                    System.out.println("So dien thoai khong hop le! (10-11 chu so, bat dau bang 0)");
                    return;
                }
                phone = newPhone;
                break;
            case "4":
                return;
            default:
                System.out.println("Lua chon khong hop le!");
                return;
        }
        
        StudentService.updateStudent(id, name, dob, email, sex, phone);
    }
    
    private void deleteStudent() {
        System.out.println("\n========= XOA HOC VIEN =========");
        
        System.out.print("Nhap ID hoc vien: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID phai la so!");
            return;
        }
        
        Map<String, Object> student = StudentService.getStudentById(id);
        if (student == null) {
            System.out.println("Hoc vien khong ton tai!");
            return;
        }
        
        System.out.println("Hoc vien: " + student.get("name"));
        System.out.print("Xac nhan xoa? (Y/N): ");
        
        if (scanner.nextLine().trim().toUpperCase().equals("Y")) {
            StudentService.deleteStudent(id);
        }
    }
    
    private void searchStudent() {
        System.out.println("\n========= TIM KIEM HOC VIEN =========");
        
        System.out.println("1. Tim theo ten");
        System.out.println("2. Tim theo email");
        System.out.print("Chon: ");
        String choice = scanner.nextLine().trim();
        
        if (choice.equals("1")) {
            System.out.print("Ten: ");
            StudentService.searchStudentByName(scanner.nextLine().trim());
        } else {
            System.out.print("Email: ");
            StudentService.searchStudentByEmail(scanner.nextLine().trim());
        }
    }
    
    private void sortStudents() {
        System.out.println("\n========= SAP XEP HOC VIEN =========");
        
        System.out.println("Sap xep theo:");
        System.out.println("1. ID");
        System.out.println("2. Ten");
        System.out.print("Chon: ");
        int sortBy = scanner.nextLine().trim().equals("2") ? 2 : 1;
        
        System.out.println("Thu tu:");
        System.out.println("1. Tang dan");
        System.out.println("2. Giam dan");
        System.out.print("Chon: ");
        int order = scanner.nextLine().trim().equals("2") ? 2 : 1;
        
        StudentService.sortStudents(sortBy, order);
        StudentService.printAllStudents();
    }
}
