package gui;

import java.awt.*;
import javax.swing.*;
import logic.EmployeeManager;

/**
 * OWNER: Member C.
 * Welcome message, navigation buttons, CardLayout, Logout.
 */
public class MainDashboardFrame extends JFrame {
    private EmployeeManager manager;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private EmployeeManagementPanel empPanel;
    private DepartmentManagementPanel deptPanel;

    public MainDashboardFrame(EmployeeManager manager) {
        this.manager = manager;

        setTitle("Employee Management System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // 1. Header Panel (الشريط العلوي للترحيب وتسجيل الخروج)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel lblTitle = new JLabel("Employee Management Dashboard");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFocusable(false);
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogout.setBackground(new Color(231, 76, 60));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.addActionListener(e -> handleLogout());

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnLogout, BorderLayout.EAST);

        // 2. Sidebar Navigation Panel (القائمة الجانبية للأزرار)
        JPanel navPanel = new JPanel(new GridLayout(6, 1, 8, 8));
        navPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        navPanel.setBackground(new Color(236, 240, 241));
        navPanel.setPreferredSize(new Dimension(220, 0));

        JButton btnHome = createNavButton("Home / Overview");
        JButton btnEmpMgmt = createNavButton("Manage Employees");
        JButton btnDeptMgmt = createNavButton("Manage Departments");

        btnHome.addActionListener(e -> navigateTo("HOME"));
        btnEmpMgmt.addActionListener(e -> navigateTo("EMPLOYEES"));
        btnDeptMgmt.addActionListener(e -> navigateTo("DEPARTMENTS"));

        navPanel.add(btnHome);
        navPanel.add(btnEmpMgmt);
        navPanel.add(btnDeptMgmt);

        // 3. Content Area with CardLayout (منطقة المحتوى التي تتغير حسب الاختيار)
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Home / Welcome Panel
        JPanel homePanel = new JPanel(new GridBagLayout());
        homePanel.setBackground(Color.WHITE);

        JLabel lblWelcome = new JLabel("Welcome to Employee Management System");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblWelcome.setForeground(new Color(44, 62, 80));
        homePanel.add(lblWelcome);

        // Sub Panels (إنشاء لوحات الموظفين والأقسام وتمرير الـ Manager لها)
        empPanel = new EmployeeManagementPanel(manager);
        deptPanel = new DepartmentManagementPanel(manager);

        contentPanel.add(homePanel, "HOME");
        contentPanel.add(empPanel, "EMPLOYEES");
        contentPanel.add(deptPanel, "DEPARTMENTS");

        // إضافة اللوحات للـ Frame
        add(headerPanel, BorderLayout.NORTH);
        add(navPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusable(false);
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        return btn;
    }

    public void navigateTo(String panelName) {
        cardLayout.show(contentPanel, panelName);
    }

    public void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to log out?",
            "Logout Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        if (choice == JOptionPane.YES_OPTION) {
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                new LoginFrame(manager).setVisible(true);
            });
        }
    }
}