package gui;

import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import logic.EmployeeManager;
import model.Department;
import model.Employee;

/**
 * OWNER: Member D. Form + JTable + Add / Update / Delete / Search.
 * Validate with InputValidator, delegate everything else to EmployeeManager.
 * Delete needs a JOptionPane confirmation; update/delete with no selection => message.
 */
public class EmployeeManagementPanel extends JPanel {
    private EmployeeManager manager;
    private JTable tblEmployees;
    private DefaultTableModel tableModel;
    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtAge;
    private JTextField txtPosition;
    private JComboBox<Department> cmbDepartment;
    private JComboBox<String> cmbType;
    private JTextField txtBasicSalary;
    private JTextField txtBonus;
    private JTextField txtHourlyRate;
    private JTextField txtHoursWorked;
    private JTextField txtSearch;

    public EmployeeManagementPanel(EmployeeManager manager) {
        // TODO
    }
    public void handleAddEmployee() { /* TODO: "Employee added successfully!" */ }
    public void handleUpdateEmployee() { /* TODO */ }
    public void handleDeleteEmployee() { /* TODO */ }
    public void handleSearch() { /* TODO: by ID / name / department */ }
    public void toggleSalaryFields() { /* TODO: show basic+bonus OR rate+hours */ }
    public void refreshEmployeeTable(List<Employee> list) { /* TODO: salary column = calculateSalary() */ }
}
