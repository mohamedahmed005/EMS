package gui;

import javax.swing.*;
import logic.EmployeeManager;

/** OWNER: Member B. Add department + table of departments. */
public class DepartmentManagementPanel extends JPanel {
    private EmployeeManager manager;
    private JTextField txtDeptId;
    private JTextField txtDeptName;
    private JTable tblDepartments;

    public DepartmentManagementPanel(EmployeeManager manager) {
        // TODO
    }
    public void handleAddDepartment() { /* TODO: validate, manager.addDepartment, refresh */ }
    public void refreshDepartmentTable() { /* TODO */ }
}
