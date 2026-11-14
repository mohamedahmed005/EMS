package gui;

import javax.swing.*;
import logic.EmployeeManager;

/** OWNER: Member A. Username/password + Login + Exit. Wrong credentials => JOptionPane message. */
public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnExit;
    private EmployeeManager manager;

    public LoginFrame(EmployeeManager manager) {
        // TODO: build UI, add ActionListeners
    }
    private void handleLogin() { /* TODO: manager.authenticate -> open MainDashboardFrame, dispose this */ }
    private void handleExit() { /* TODO */ }
}
