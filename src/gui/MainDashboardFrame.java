package gui;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import logic.EmployeeManager;
import model.Employee;
import model.FullTimeEmployee;

/** Welcome message + summary cards, navigation buttons, CardLayout, Logout. */
public class MainDashboardFrame extends JFrame {
    private final EmployeeManager manager;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);
    private final EmployeeManagementPanel empPanel;
    private final JPanel navPanel = new JPanel(new GridLayout(10, 1, 0, 6));

    private final JLabel lblEmployees = new JLabel("0");
    private final JLabel lblDepartments = new JLabel("0");
    private final JLabel lblTypes = new JLabel("0 / 0");
    private final JLabel lblPayroll = new JLabel("0 EGP");

    public MainDashboardFrame(EmployeeManager manager) {
        this.manager = manager;
        setTitle("Employee Management System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        // Header
        JLabel lblTitle = new JLabel("Employee Management Dashboard");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        JButton btnLogout = createButton("Logout", new Color(231, 76, 60));
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.addChangeListener(e -> btnLogout.setBackground(btnLogout.getModel().isRollover() ? new Color(192, 57, 43) : new Color(231, 76, 60)));
        btnLogout.addActionListener(e -> handleLogout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnLogout, BorderLayout.EAST);

        // Sidebar
        navPanel.setBorder(BorderFactory.createEmptyBorder(15, 12, 15, 12));
        navPanel.setBackground(new Color(33, 47, 61));
        navPanel.setPreferredSize(new Dimension(220, 0));
        for (String[] n : new String[][]{{"Home / Overview", "HOME"}, {"Manage Employees", "EMPLOYEES"}, {"Manage Departments", "DEPARTMENTS"}}) addNavButton(navPanel, n[0], n[1]);

        // Content
        empPanel = new EmployeeManagementPanel(manager);
        contentPanel.add(buildHomePanel(), "HOME");
        contentPanel.add(empPanel, "EMPLOYEES");
        contentPanel.add(new DepartmentManagementPanel(manager), "DEPARTMENTS");

        add(headerPanel, BorderLayout.NORTH);
        add(navPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        navigateTo("HOME");
    }

    private JPanel buildHomePanel() {
        JLabel lblWelcome = new JLabel("Welcome to Employee Management System", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblWelcome.setForeground(new Color(44, 62, 80));

        JPanel cards = new JPanel(new GridLayout(1, 4, 16, 0));
        cards.setOpaque(false);
        cards.add(createCard("Employees", lblEmployees, new Color(41, 128, 185)));
        cards.add(createCard("Departments", lblDepartments, new Color(39, 174, 96)));
        cards.add(createCard("Full-Time / Part-Time", lblTypes, new Color(243, 156, 18)));
        cards.add(createCard("Monthly Payroll", lblPayroll, new Color(142, 68, 173)));

        JPanel home = new JPanel(new GridBagLayout());
        home.setBackground(Color.WHITE);
        home.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 30, 0);
        home.add(lblWelcome, gbc);
        gbc.insets = new Insets(0, 0, 0, 0);
        home.add(cards, gbc);
        return home;
    }

    /** One summary card: small title on top, big value below, colored top stripe. */
    private JPanel createCard(String title, JLabel value, Color accent) {
        JLabel lblCardTitle = new JLabel(title, SwingConstants.CENTER);
        lblCardTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCardTitle.setForeground(new Color(127, 140, 141));

        value.setHorizontalAlignment(SwingConstants.CENTER);
        value.setFont(new Font("Segoe UI", Font.BOLD, 26));
        value.setForeground(new Color(44, 62, 80));

        JPanel card = new JPanel(new GridLayout(2, 1, 0, 6));
        card.setBackground(new Color(236, 240, 241));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(4, 0, 0, 0, accent),
                BorderFactory.createEmptyBorder(18, 10, 18, 10)));
        card.add(lblCardTitle);
        card.add(value);
        return card;
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setUI(new BasicButtonUI()); // needed so colors show under the system look and feel
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusable(false);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createLineBorder(bg.darker(), 1));
        return btn;
    }

    private void addNavButton(JPanel panel, String text, String card) {
        JButton btn = createButton(text, new Color(0x212F3D));
        btn.setName(card);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 10));
        btn.addChangeListener(e -> btn.setBackground(new Color(btn.isSelected() ? 0x2980B9 : btn.getModel().isRollover() ? 0x34495E : 0x212F3D)));
        btn.addActionListener(e -> navigateTo(card));
        panel.add(btn);
    }

    private void refreshHome() {
        List<Employee> employees = manager.getAllEmployees();
        int fullTime = 0;
        double payroll = 0;
        for (Employee e : employees) {
            if (e instanceof FullTimeEmployee) {
                fullTime++;
            }
            payroll += e.calculateSalary();
        }
        lblEmployees.setText(String.valueOf(employees.size()));
        lblDepartments.setText(String.valueOf(manager.getAllDepartments().size()));
        lblTypes.setText(fullTime + " / " + (employees.size() - fullTime));
        lblPayroll.setText(String.format("%,.0f EGP", payroll));
    }

    public void navigateTo(String panelName) {
        if ("EMPLOYEES".equals(panelName)) empPanel.reloadDepartments(); // pick up departments added/removed in the other tab
        else if ("HOME".equals(panelName)) refreshHome(); // pick up changes made in the other tabs
        for (Component c : navPanel.getComponents()) ((JButton) c).setSelected(panelName.equals(c.getName()));
        cardLayout.show(contentPanel, panelName);
    }

    public void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?",
                "Logout Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame(manager).setVisible(true);
        }
    }
}
