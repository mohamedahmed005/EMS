import gui.LoginFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import logic.EmployeeManager;
import logic.FileHandler;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Use Swing's default look and feel if the system one is unavailable.
            }

            FileHandler fileHandler = new FileHandler(
                    "data/employees.csv",
                    "data/departments.csv"
            );
            EmployeeManager manager = new EmployeeManager(fileHandler);

            new LoginFrame(manager).setVisible(true);
        });
    }
}
