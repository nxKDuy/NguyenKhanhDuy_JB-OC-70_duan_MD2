package views;

import services.CourseService;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AdminCourseManagement {
    private Scanner scanner;
    
    public AdminCourseManagement(Scanner scanner) {
        this.scanner = scanner;
    }
    
    public void start() {
        int choice = -1;
        
        while (true) {
            System.out.println("\n========= QUAN LY KHOA HOC =========");
            System.out.println("1. Xem danh sach khoa hoc");
            System.out.println("2. Them khoa hoc moi");
            System.out.println("3. Chinh sua khoa hoc");
            System.out.println("4. Xoa khoa hoc");
            System.out.println("5. Tim kiem khoa hoc");
            System.out.println("6. Sap xep khoa hoc");
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
                case 1: viewCourses(); break;
                case 2: addCourse(); break;
                case 3: editCourse(); break;
                case 4: deleteCourse(); break;
                case 5: searchCourse(); break;
                case 6: sortCourses(); break;
                case 7: return;
                default: System.out.println("Lua chon khong hop le!");
            }
        }
    }
    
    private void viewCourses() {
        System.out.println("\n========= DANH SACH KHOA HOC =========");
        List<Map<String, Object>> courses = CourseService.getAllCourses();
        printCourses(courses);
    }
    
    private void addCourse() {
        System.out.println("\n========= THEM KHOA HOC MOI =========");
        System.out.print("Ten khoa hoc: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Thoi luong (gio): ");
        int duration;
        try {
            duration = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Thoi luong phai la so!");
            return;
        }
        
        System.out.print("Giang vien: ");
        String instructor = scanner.nextLine().trim();
        
        CourseService.addCourse(name, duration, instructor);
    }
    
    private void editCourse() {
        System.out.println("\n========= CHINH SUA KHOA HOC =========");
        System.out.print("Nhap ID khoa hoc: ");
        int courseId;
        try {
            courseId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID phai la so!");
            return;
        }
        
        Map<String, Object> course = CourseService.getCourseById(courseId);
        if (course == null) {
            System.out.println("Khoa hoc khong ton tai!");
            return;
        }
        
        System.out.println("\nThong tin hien tai:");
        System.out.println("1. Ten: " + course.get("name"));
        System.out.println("2. Thoi luong: " + course.get("duration") + " gio");
        System.out.println("3. Giang vien: " + course.get("instructor"));
        System.out.println("4. Quay lai");
        System.out.print("Chon thuoc tinh can sua: ");
        
        String choice = scanner.nextLine().trim();
        
        String name = course.get("name").toString();
        int duration = (int) course.get("duration");
        String instructor = course.get("instructor").toString();
        
        switch (choice) {
            case "1":
                System.out.print("Ten moi: ");
                name = scanner.nextLine().trim();
                break;
            case "2":
                System.out.print("Thoi luong moi: ");
                try {
                    duration = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Thoi luong phai la so!");
                    return;
                }
                break;
            case "3":
                System.out.print("Giang vien moi: ");
                instructor = scanner.nextLine().trim();
                break;
            case "4":
                return;
            default:
                System.out.println("Lua chon khong hop le!");
                return;
        }
        
        CourseService.updateCourse(courseId, name, duration, instructor);
    }
    
    private void deleteCourse() {
        System.out.println("\n========= XOA KHOA HOC =========");
        System.out.print("Nhap ID khoa hoc can xoa: ");
        int courseId;
        try {
            courseId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID phai la so!");
            return;
        }
        
        Map<String, Object> course = CourseService.getCourseById(courseId);
        if (course == null) {
            System.out.println("Khoa hoc khong ton tai!");
            return;
        }
        
        System.out.println("Khoa hoc can xoa: " + course.get("name"));
        System.out.print("Xac nhan xoa? (Y/N): ");
        String confirm = scanner.nextLine().trim().toUpperCase();
        
        if (confirm.equals("Y")) {
            CourseService.deleteCourse(courseId);
        } else {
            System.out.println("Da huy xoa.");
        }
    }
    
    private void searchCourse() {
        System.out.println("\n========= TIM KIEM KHOA HOC =========");
        System.out.print("Nhap ten khoa hoc can tim: ");
        String searchName = scanner.nextLine().trim();
        
        List<Map<String, Object>> courses = CourseService.searchCourseByName(searchName);
        printCourses(courses);
    }
    
    private void sortCourses() {
        System.out.println("\n========= SAP XEP KHOA HOC =========");
        System.out.println("Sap xep theo:");
        System.out.println("1. ID");
        System.out.println("2. Ten");
        System.out.print("Chon: ");
        String sortByChoice = scanner.nextLine().trim();
        int sortBy = sortByChoice.equals("2") ? 2 : 1;
        
        System.out.println("Thu tu:");
        System.out.println("1. Tang dan");
        System.out.println("2. Giam dan");
        System.out.print("Chon: ");
        String orderChoice = scanner.nextLine().trim();
        int order = orderChoice.equals("2") ? 2 : 1;
        
        List<Map<String, Object>> courses = CourseService.sortCourses(sortBy, order);
        printCourses(courses);
    }
    
    private void printCourses(List<Map<String, Object>> courses) {
        if (courses.isEmpty()) {
            System.out.println("Khong co khoa hoc nao!");
            return;
        }
        
        System.out.println("\nID | Ten khoa hoc | Thoi luong (gio) | Giang vien");
        System.out.println("----|-------------|-----------------|----------");
        
        for (Map<String, Object> course : courses) {
            String name = course.get("name").toString();
            if (name.length() > 20) name = name.substring(0, 20);
            
            String instructor = course.get("instructor").toString();
            if (instructor.length() > 15) instructor = instructor.substring(0, 15);
            
            System.out.printf("%2d | %-20s | %10d | %s\n",
                course.get("id"),
                name,
                course.get("duration"),
                instructor);
        }
    }
}
