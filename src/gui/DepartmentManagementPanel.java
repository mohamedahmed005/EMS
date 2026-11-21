package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import logic.EmployeeManager;
import logic.InputValidator;
import model.Department;

/** OWNER: Member B. Add department + table of departments. */
public class DepartmentManagementPanel extends JPanel {
    private EmployeeManager manager;
    private JTextField txtDeptId;
    private JTextField txtDeptName;
    private JTable tblDepartments;
    private DefaultTableModel tableModel;

    public DepartmentManagementPanel(EmployeeManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout());

        // top: the form
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtDeptId = new JTextField(8);
        txtDeptName = new JTextField(15);
        JButton btnAdd = new JButton("Add Department");
        form.add(new JLabel("Department ID:"));
        form.add(txtDeptId);
        form.add(new JLabel("Department Name:"));
        form.add(txtDeptName);
        form.add(btnAdd);
        add(form, BorderLayout.NORTH);

        // center: the table (cells are read-only)
        tableModel = new DefaultTableModel(new String[]{"ID", "Name"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblDepartments = new JTable(tableModel);
        add(new JScrollPane(tblDepartments), BorderLayout.CENTER);

        // button action
        btnAdd.addActionListener(e -> handleAddDepartment());

        refreshDepartmentTable();
    }

    public void handleAddDepartment() {
        String idText = txtDeptId.getText();
        String nameText = txtDeptName.getText();

        // 1. required fields
        if (!InputValidator.isNotEmpty(idText) || !InputValidator.isNotEmpty(nameText)) {
            showError("Please fill in both the department ID and the department name.");
            return;
        }
        // 2. ID must be a positive whole number
        if (!InputValidator.isValidPositiveInt(idText)) {
            showError("Department ID must be a positive whole number.");
            return;
        }

        int id = Integer.parseInt(idText.trim());
        String name = nameText.trim();

        // 3. unique ID and unique name (the manager ignores letter case)
        if (!manager.isDepartmentUnique(id, name)) {
            showError("This department ID or name already exists (names are not case-sensitive).");
            return;
        }

        // 4. add it (the manager saves to the file)
        try {
            if (manager.addDepartment(new Department(id, name))) {
                JOptionPane.showMessageDialog(this, "Department added successfully!");
                txtDeptId.setText("");
                txtDeptName.setText("");
                refreshDepartmentTable();
            } else {
                showError("Could not add the department.");
            }
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    public void refreshDepartmentTable() {
        tableModel.setRowCount(0);
        for (Department d : manager.getAllDepartments()) {
            tableModel.addRow(new Object[]{d.getDepartmentId(), d.getDepartmentName()});
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}