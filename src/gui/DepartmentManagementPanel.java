
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
        JButton btnRemove = new JButton("Remove Department");

        form.add(new JLabel("Department ID:"));
        form.add(txtDeptId);
        form.add(new JLabel("Department Name:"));
        form.add(txtDeptName);
        form.add(btnAdd);
        form.add(btnRemove);

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

        // button actions
        btnAdd.addActionListener(e -> handleAddDepartment());
        btnRemove.addActionListener(e -> handleRemoveDepartment());

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

    public void handleRemoveDepartment() {
        int selectedRow = tblDepartments.getSelectedRow();

        // 1. Make sure a row is selected
        if (selectedRow == -1) {
            showError("Please select a department from the table.");
            return;
        }

        // 2. Get department ID from the selected row
        int departmentId = (int) tableModel.getValueAt(selectedRow, 0);
        String departmentName = (String) tableModel.getValueAt(selectedRow, 1);

        // 3. Ask for confirmation
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to remove department \"" + departmentName + "\"?",
                "Remove Department",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        // 4. Remove if confirmed
        if (choice == JOptionPane.YES_OPTION) {
            if (manager.removeDepartment(departmentId)) {
                JOptionPane.showMessageDialog(this, "Department removed successfully!");
                refreshDepartmentTable();
            } else {
                showError("Could not remove the department.");
            }
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
