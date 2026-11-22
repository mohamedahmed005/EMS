package gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import logic.EmployeeManager;
import logic.InputValidator;
import model.Department;
import model.Employee;
import model.FullTimeEmployee;
import model.PartTimeEmployee;

public class EmployeeManagementPanel extends JPanel {
    private final EmployeeManager  manager;
    private int selectedEmployeeId = -1;
    // Track the currently selected employee ID for update/delete operations

    private JTable tblEmployees; // Table to display employee data
    private DefaultTableModel tableModel;
     // Table model for employee data(add/remove rows/update cells)

    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtAge;
    private JTextField txtPosition;
    private JTextField txtBasicSalary;
    private JTextField txtBonus;
    private JTextField txtHourlyRate;
    private JTextField txtHoursWorked;
    private JTextField txtSearch;

    private JComboBox<Department> cmbDepartment; //employee department
    private JComboBox<String> cmbType; //part-time or full-time
    private JComboBox<String> cmbFilterDept; //filter by department

    private JLabel lblBasicSalary;
    private JLabel lblBonus;
    private JLabel lblHourlyRate;
    private JLabel lblHoursWorked;


    public EmployeeManagementPanel(EmployeeManager manager) {
        this.manager = manager;
        initComponents();
        reloadDepartments();
        toggleSalaryFields();
        refreshEmployeeTable(manager.getAllEmployees());
    }

    private void initComponents(){
        setLayout(new BorderLayout(12, 12));
        // Set layout with horizontal and vertical gaps
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        // Set empty border around the panel for padding

        JLabel lblTitle = new JLabel("Employee Management");// Title label for the panel
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(lblTitle, BorderLayout.NORTH);// Add title label to the top of the panel

        JPanel formPanel = new JPanel(new GridBagLayout());// Panel for employee input form
        formPanel.setBorder(BorderFactory.createTitledBorder("Employee Information"));// Set titled border for the form panel
        GridBagConstraints gbc = new GridBagConstraints();// Constraints for layout management
        gbc.insets = new Insets(5, 8, 5, 8);// Set insets for spacing between components
        gbc.fill = GridBagConstraints.HORIZONTAL;// Set fill behavior for components to occupy horizontal space

        txtId = new JTextField(12);
        txtName = new JTextField(16);
        txtAge = new JTextField(12);
        txtPosition = new JTextField(16);
        
        cmbDepartment = new JComboBox<>();
        cmbType = new JComboBox<>(new String[]{"Full-Time", "Part-Time"});
        cmbType.addActionListener(e -> toggleSalaryFields()); // Add action listener to toggle salary fields based on employee type selection

        txtBasicSalary = new JTextField(12);
        txtBonus = new JTextField(12);
        txtHourlyRate = new JTextField(12);
        txtHoursWorked = new JTextField(12);

        lblBasicSalary = new JLabel("Basic Salary:");
        lblBonus = new JLabel("Bonus:");
        lblHourlyRate = new JLabel("Hourly Rate:");
        lblHoursWorked = new JLabel("Hours Worked:");

        addFormPair(formPanel, gbc, 0, 0, new JLabel("Employee ID:"), txtId);
        addFormPair(formPanel, gbc, 2, 0, new JLabel("Full Name:"), txtName);
        addFormPair(formPanel, gbc, 4, 0, new JLabel("Age:"), txtAge);

        addFormPair(formPanel, gbc, 0, 1, new JLabel("Department:"), cmbDepartment);
        addFormPair(formPanel, gbc, 2, 1, new JLabel("Position:"), txtPosition);
        addFormPair(formPanel, gbc, 4, 1, new JLabel("Employee Type:"), cmbType);

        addFormPair(formPanel, gbc, 0, 2, lblBasicSalary, txtBasicSalary);
        addFormPair(formPanel, gbc, 2, 2, lblBonus, txtBonus);

        addFormPair(formPanel, gbc, 0, 3, lblHourlyRate, txtHourlyRate);
        addFormPair(formPanel, gbc, 2, 3, lblHoursWorked, txtHoursWorked);

        JPanel actionButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnAdd = new JButton("Add Employee");
        JButton btnUpdate = new JButton("Update Employee");
        JButton btnDelete = new JButton("Delete Employee");
        JButton btnClear = new JButton("Clear Form");

        btnAdd.addActionListener(e -> handleAddEmployee());
        btnUpdate.addActionListener(e -> handleUpdateEmployee());
        btnDelete.addActionListener(e -> handleDeleteEmployee());
        btnClear.addActionListener(e -> clearForm());

        actionButtons.add(btnAdd);
        actionButtons.add(btnUpdate);
        actionButtons.add(btnDelete);
        actionButtons.add(btnClear);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search & Filter"));
        // Add search and filter components to the search panel

        txtSearch = new JTextField(18);
        cmbFilterDept = new JComboBox<>();
        JButton btnSearch = new JButton("Search / Filter");
        JButton btnReset = new JButton("Show All");

        btnSearch.addActionListener(e -> handleSearch());
        btnReset.addActionListener(e -> {
        txtSearch.setText("");
        cmbFilterDept.setSelectedIndex(0);
        refreshEmployeeTable(manager.getAllEmployees());
        });

        searchPanel.add(new JLabel("Search (ID or Name):"));
        searchPanel.add(txtSearch);
        searchPanel.add(new JLabel("Department:"));
        searchPanel.add(cmbFilterDept);
        searchPanel.add(btnSearch);
        searchPanel.add(btnReset);

        String[] columns = {"ID", "Full Name", "Age", "Department", "Position", "Type", "Monthly Salary (EGP)"};

        tableModel = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
            }
        };// Create a table model with specified columns and make cells non-editable

        tblEmployees = new JTable(tableModel);// Create a table to display employee data
        tblEmployees.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblEmployees.setRowHeight(24);

        tblEmployees.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedEmployeeToForm();
            }
        });// Add a selection listener to load selected employee data into the form when a row is selected

        JPanel topSection = new JPanel(new BorderLayout(8, 8));
        topSection.add(formPanel, BorderLayout.NORTH);
        topSection.add(actionButtons, BorderLayout.CENTER);
        topSection.add(searchPanel, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel(new BorderLayout(8, 8));
        centerPanel.add(topSection, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(tblEmployees), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);


    }

    private void addFormPair(JPanel panel, GridBagConstraints gbc, int col, int row,JLabel label, java.awt.Component field){
    gbc.gridwidth = 1;
    gbc.gridx = col;
    gbc.gridy = row;
    gbc.weightx = 0.0;
    panel.add(label, gbc);

    gbc.gridx = col + 1;
    gbc.weightx = 1.0;
    panel.add(field, gbc);
}


    private void reloadDepartments() {
    Department currentSelected = (Department) cmbDepartment.getSelectedItem();
    cmbDepartment.removeAllItems();
    cmbFilterDept.removeAllItems();
    cmbFilterDept.addItem("All");

    for (Department dept : manager.getAllDepartments()) {
        cmbDepartment.addItem(dept);
        cmbFilterDept.addItem(dept.getDepartmentName());
    }

    if (currentSelected != null) {
        cmbDepartment.setSelectedItem(currentSelected);
    }
}


    public void handleAddEmployee() {
    Employee emp = buildEmployeeFromForm(true);

    if (emp == null) {
        return;
    }

    if (manager.addEmployee(emp)) {
        refreshEmployeeTable(manager.getAllEmployees());
        clearForm();

        JOptionPane.showMessageDialog(
                this,
                "Employee added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    } else {
        JOptionPane.showMessageDialog(
                this,
                "Failed to add employee. Ensure Employee ID is unique.",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
    

    public void handleUpdateEmployee() {
    if (selectedEmployeeId == -1) {
        JOptionPane.showMessageDialog(
                this,
                "Please select an employee from the table to update.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    Employee updatedEmp = buildEmployeeFromForm(false);
    if (updatedEmp == null) {
        return;
    }

    if (updatedEmp.getEmployeeId() != selectedEmployeeId
            && !manager.isEmployeeIdUnique(updatedEmp.getEmployeeId())) {
        JOptionPane.showMessageDialog(
                this,
                "Employee ID " + updatedEmp.getEmployeeId() + " already exists.",
                "Duplicate ID",
                JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    if (manager.updateEmployee(selectedEmployeeId, updatedEmp)) {
        refreshEmployeeTable(manager.getAllEmployees());
        clearForm();
        JOptionPane.showMessageDialog(
                this,
                "Employee updated successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    } else {
        JOptionPane.showMessageDialog(
                this,
                "Failed to update employee record.",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
    

    public void handleDeleteEmployee() {
    if (selectedEmployeeId == -1) {
        JOptionPane.showMessageDialog(
                this,
                "Please select an employee from the table to delete.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete employee ID " + selectedEmployeeId + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {
        if (manager.deleteEmployee(selectedEmployeeId)) {
            refreshEmployeeTable(manager.getAllEmployees());
            clearForm();
            JOptionPane.showMessageDialog(
                    this,
                    "Employee deleted successfully.",
                    "Deleted",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}

    public void handleSearch() {
    String query = txtSearch.getText().trim();
    String selectedDept = (String) cmbFilterDept.getSelectedItem();

    List<Employee> baseList = manager.filterByDepartment(selectedDept);
    List<Employee> results = new ArrayList<>();

    if (!InputValidator.isNotEmpty(query)) {
        results = baseList;
    } else if (InputValidator.isValidPositiveInt(query)) {
        int searchId = Integer.parseInt(query);
        for (Employee emp : baseList) {
            if (emp.getEmployeeId() == searchId) {
                results.add(emp);
            }
        }
    } else {
        String lower = query.toLowerCase();
        for (Employee emp : baseList) {
            boolean matchesName = emp.getFullName().toLowerCase().contains(lower);
            boolean matchesDept = emp.getDepartment() != null
                    && emp.getDepartment().getDepartmentName().toLowerCase().contains(lower);
            if (matchesName || matchesDept) {
                results.add(emp);
            }
        }
    }

    refreshEmployeeTable(results);
    if (results.isEmpty()) {
        JOptionPane.showMessageDialog(
                this,
                "No matching employees found.",
                "Search Result",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}

    private  void toggleSalaryFields() {
    String selectedType = (String) cmbType.getSelectedItem();
    boolean isFullTime = "Full-Time".equals(selectedType);

    lblBasicSalary.setVisible(isFullTime);
    txtBasicSalary.setVisible(isFullTime);
    lblBonus.setVisible(isFullTime);
    txtBonus.setVisible(isFullTime);

    lblHourlyRate.setVisible(!isFullTime);
    txtHourlyRate.setVisible(!isFullTime);
    lblHoursWorked.setVisible(!isFullTime);
    txtHoursWorked.setVisible(!isFullTime);

    revalidate();
    repaint();
}

    private void refreshEmployeeTable(List<Employee> list) {
    tableModel.setRowCount(0);

    for (Employee emp : list) {
        String deptName = (emp.getDepartment() != null)
                ? emp.getDepartment().getDepartmentName()
                : "N/A";

        tableModel.addRow(new Object[]{
                emp.getEmployeeId(),
                emp.getFullName(),
                emp.getAge(),
                deptName,
                emp.getPosition(),
                emp.getEmployeeType(),
                String.format("%.2f", emp.calculateSalary())
        });
    }
}
    private Employee buildEmployeeFromForm(boolean checkUniqueId) {
    String idText = txtId.getText();
    String name = txtName.getText();
    String ageText = txtAge.getText();
    String position = txtPosition.getText();
    Department dept = (Department) cmbDepartment.getSelectedItem();
    String type = (String) cmbType.getSelectedItem();

    if (!InputValidator.isValidPositiveInt(idText)) {
        showValidationError("Employee ID must be a valid positive integer.");
        return null;
    }

    int id = Integer.parseInt(idText.trim());

    if (checkUniqueId && !manager.isEmployeeIdUnique(id)) {
        showValidationError("Employee ID " + id + " already exists.");
        return null;
    }

    if (!InputValidator.isNotEmpty(name)) {
        showValidationError("Full Name is required.");
        return null;
    }

    if (!InputValidator.isValidPositiveInt(ageText)) {
        showValidationError("Age must be a valid positive integer.");
        return null;
    }

    int age = Integer.parseInt(ageText.trim());

    if (dept == null) {
        showValidationError("Please select a valid department.");
        return null;
    }

    if (!InputValidator.isNotEmpty(position)) {
        showValidationError("Position is required.");
        return null;
    }

    if ("Full-Time".equals(type)) {
        if (!InputValidator.isValidNonNegativeDouble(txtBasicSalary.getText())) {
            showValidationError("Basic Salary must be a valid non-negative number.");
            return null;
        }

        if (!InputValidator.isValidNonNegativeDouble(txtBonus.getText())) {
            showValidationError("Bonus must be a valid non-negative number.");
            return null;
        }

        double basicSalary = Double.parseDouble(txtBasicSalary.getText().trim());
        double bonus = Double.parseDouble(txtBonus.getText().trim());

        return new FullTimeEmployee(
                id, name.trim(), age, dept, position.trim(), basicSalary, bonus
        );

    } else if ("Part-Time".equals(type)) {
        if (!InputValidator.isValidNonNegativeDouble(txtHourlyRate.getText())) {
            showValidationError("Hourly Rate must be a valid non-negative number.");
            return null;
        }

        if (!InputValidator.isValidNonNegativeDouble(txtHoursWorked.getText())) {
            showValidationError("Hours Worked must be a valid non-negative number.");
            return null;
        }

        double hourlyRate = Double.parseDouble(txtHourlyRate.getText().trim());
        double hoursWorked = Double.parseDouble(txtHoursWorked.getText().trim());

        return new PartTimeEmployee(
                id, name.trim(), age, dept, position.trim(), hourlyRate, hoursWorked
        );
    }

    showValidationError("Unsupported employee type.");
    return null;
}

    private void loadSelectedEmployeeToForm() {
    int row = tblEmployees.getSelectedRow();
    if (row == -1) {
        return;
    }
    int id = (int) tableModel.getValueAt(row, 0);
    Employee emp = manager.searchById(id);
    if (emp == null) {
        return;
    }
    selectedEmployeeId = emp.getEmployeeId();
    txtId.setText(String.valueOf(emp.getEmployeeId()));
    txtName.setText(emp.getFullName());
    txtAge.setText(String.valueOf(emp.getAge()));
    txtPosition.setText(emp.getPosition());
    cmbDepartment.setSelectedItem(emp.getDepartment());
    cmbType.setSelectedItem(emp.getEmployeeType());
    toggleSalaryFields();

    if (emp instanceof FullTimeEmployee ft) {
    txtBasicSalary.setText(String.valueOf(ft.getBasicSalary()));
    txtBonus.setText(String.valueOf(ft.getBonus()));
    txtHourlyRate.setText("");
    txtHoursWorked.setText("");
} else if (emp instanceof PartTimeEmployee pt) {
    txtHourlyRate.setText(String.valueOf(pt.getHourlyRate()));
    txtHoursWorked.setText(String.valueOf(pt.getHoursWorked()));
    txtBasicSalary.setText("");
    txtBonus.setText("");
}
}
    private void clearForm() {
    selectedEmployeeId = -1;
    txtId.setText("");
    txtName.setText("");
    txtAge.setText("");
    txtPosition.setText("");
    txtBasicSalary.setText("");
    txtBonus.setText("");
    txtHourlyRate.setText("");
    txtHoursWorked.setText("");

    if (cmbDepartment.getItemCount() > 0) {
        cmbDepartment.setSelectedIndex(0);
    }

    cmbType.setSelectedIndex(0);
    toggleSalaryFields();
    tblEmployees.clearSelection();
}

    private void showValidationError(String message) {
    JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.WARNING_MESSAGE);
}


}
