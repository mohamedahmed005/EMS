import gui.EmployeeManagementPanel;
import javax.swing.*;
import logic.EmployeeManager;
import logic.FileHandler;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            FileHandler fileHandler = new FileHandler(
                    "data/employees.csv",
                    "data/departments.csv"
            );

            EmployeeManager manager = new EmployeeManager(fileHandler);

            JFrame frame = new JFrame("Employee Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);

            EmployeeManagementPanel panel = new EmployeeManagementPanel(manager);

            frame.add(panel);

            frame.setVisible(true);
        });
    }
}