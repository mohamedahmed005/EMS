package gui;

import java.awt.CardLayout;
import javax.swing.*;
import logic.EmployeeManager;

/** OWNER: Member C. Welcome message, navigation buttons, CardLayout, Logout. */
public class MainDashboardFrame extends JFrame {
    private EmployeeManager manager;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private EmployeeManagementPanel empPanel;
    private DepartmentManagementPanel deptPanel;

    public MainDashboardFrame(EmployeeManager manager) {
        // TODO
    }
    public void navigateTo(String panelName) { /* TODO */ }
    public void handleLogout() { /* TODO: back to LoginFrame */ }
}
