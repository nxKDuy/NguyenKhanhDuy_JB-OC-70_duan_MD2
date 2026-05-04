import services.DatabaseConnection;
import views.MainMenu;

public class Main {
    public static void main(String[] args) {
        // Test kết nối database trước khi chạy ứng dụng
        if (DatabaseConnection.testConnection()) {
            System.out.println("Kết nối dtb thành công");
            MainMenu mainMenu = new MainMenu();
            mainMenu.start();
        } else {
            System.out.println("Không thể kết nối database");
            System.exit(1);
        }
    }
}
