package logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import model.Department;
import model.Employee;

public class EmployeeManager {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    private List<Employee> employees;
    private List<Department> departments;
    private FileHandler fileHandler;

    public EmployeeManager(FileHandler fileHandler) {
        if (fileHandler == null) {
            throw new IllegalArgumentException("FileHandler cannot be null");
        }
        this.fileHandler = fileHandler;
        this.employees = new ArrayList<>();
        this.departments = new ArrayList<>();
        loadAll();
    }

    // ---------------------------------------------------------------- login

    public boolean authenticate(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        return ADMIN_USERNAME.equals(username.trim()) && ADMIN_PASSWORD.equals(password);
    }

    // ------------------------------------------------------------ employees

    /** Adds the employee. Returns false (nothing changes) if it is null, the ID is taken, or the department is not registered. */
    public boolean addEmployee(Employee emp) {
        if (emp == null) {
            return false;
        }
        if (!isEmployeeIdUnique(emp.getEmployeeId())) {
            return false;
        }
        if (!isDepartmentRegistered(emp.getDepartment())) {
            return false;
        }
        employees.add(emp);
        saveAll();
        return true;
    }

    /**
     * Replaces the employee that currently has ID 'id' with updatedEmp.
     * Returns false if no such employee exists, if the new ID would duplicate
     * ANOTHER employee, or if the department is not registered.
     */
    public boolean updateEmployee(int id, Employee updatedEmp) {
        if (updatedEmp == null) {
            return false;
        }
        int index = indexOfEmployee(id);
        if (index == -1) {
            return false;
        }
        int newId = updatedEmp.getEmployeeId();
        // keeping the same ID is fine; changing it must not collide with someone else
        if (newId != id && !isEmployeeIdUnique(newId)) {
            return false;
        }
        if (!isDepartmentRegistered(updatedEmp.getDepartment())) {
            return false;
        }
        employees.set(index, updatedEmp);
        saveAll();
        return true;
    }

    /** Removes the employee with this ID. Returns false if there is no such employee. */
    public boolean deleteEmployee(int id) {
        int index = indexOfEmployee(id);
        if (index == -1) {
            return false;
        }
        employees.remove(index);
        saveAll();
        return true;
    }

    /** Returns the employee with this ID, or null if not found. */
    public Employee searchById(int id) {
        int index = indexOfEmployee(id);
        return index == -1 ? null : employees.get(index);
    }

    /** Case-insensitive "contains" search on the full name. Never returns null. */
    public List<Employee> searchByName(String name) {
        List<Employee> result = new ArrayList<>();
        if (name == null || name.trim().isEmpty()) {
            return result;
        }
        String query = name.trim().toLowerCase(Locale.ROOT);
        for (Employee e : employees) {
            if (e.getFullName().toLowerCase(Locale.ROOT).contains(query)) {
                result.add(e);
            }
        }
        return result;
    }

    /** Case-insensitive match on the department name. Never returns null. */
    public List<Employee> filterByDepartment(String deptName) {
        List<Employee> result = new ArrayList<>();
        if (deptName == null || deptName.trim().isEmpty()) {
            return result;
        }
        String query = deptName.trim();
        for (Employee e : employees) {
            if (e.getDepartment().getDepartmentName().equalsIgnoreCase(query)) {
                result.add(e);
            }
        }
        return result;
    }

    /** True only if the ID is positive and no employee uses it yet. */
    public boolean isEmployeeIdUnique(int id) {
        return id > 0 && indexOfEmployee(id) == -1;
    }

    // ---------------------------------------------------------- departments

    /** Adds the department. Returns false if it is null or its ID / name (ignoring case) already exists. */
    public boolean addDepartment(Department dept) {
        if (dept == null) {
            return false;
        }
        if (!isDepartmentUnique(dept.getDepartmentId(), dept.getDepartmentName())) {
            return false;
        }
        departments.add(dept);
        saveAll();
        return true;
    }

    /** Removes the department with this ID.
 * Returns false if it doesn't exist or if employees are assigned to it.
 */
public boolean removeDepartment(int departmentId) {
    Department departmentToRemove = null;

    // 1. Find the department
    for (Department d : departments) {
        if (d.getDepartmentId() == departmentId) {
            departmentToRemove = d;
            break;
        }
    }

    // 2. Department doesn't exist
    if (departmentToRemove == null) {
        return false;
    }

    // 3. Prevent removing a department that has employees
    for (Employee e : employees) {
        if (e.getDepartment().getDepartmentId() == departmentId) {
            return false;
        }
    }

    // 4. Remove the department
    departments.remove(departmentToRemove);

    // 5. Save changes to the files
    saveAll();

    return true;
}

    /** True only if neither the ID nor the name (ignoring case and outer spaces) is used by another department. */
    public boolean isDepartmentUnique(int id, String name) {
        if (id <= 0 || name == null || name.trim().isEmpty()) {
            return false;
        }
        String cleanName = name.trim();
        for (Department d : departments) {
            if (d.getDepartmentId() == id || d.getDepartmentName().equalsIgnoreCase(cleanName)) {
                return false;
            }
        }
        return true;
    }

    // -------------------------------------------------------------- getters

    /** Returns a copy, so the GUI cannot change the internal list by accident. */
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    /** Returns a copy, so the GUI cannot change the internal list by accident. */
    public List<Department> getAllDepartments() {
        return new ArrayList<>(departments);
    }

    // ----------------------------------------------------------- file i/o

    public void saveAll() {
        try {
            fileHandler.saveDepartments(departments);
            fileHandler.saveEmployees(employees);
        } catch (Exception e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    public void loadAll() {
        try {
            List<Department> loadedDepartments = fileHandler.loadDepartments();
            loadedDepartments = (loadedDepartments == null)
                    ? new ArrayList<>() : new ArrayList<>(loadedDepartments);

            // first launch (no departments file yet): start with the company's default departments
            boolean seeded = false;
            if (loadedDepartments.isEmpty()) {
                loadedDepartments.add(new Department(1, "IT"));
                loadedDepartments.add(new Department(2, "Human Resources"));
                loadedDepartments.add(new Department(3, "Finance"));
                loadedDepartments.add(new Department(4, "Marketing"));
                loadedDepartments.add(new Department(5, "Sales"));
                seeded = true;
            }

            List<Employee> loadedEmployees = fileHandler.loadEmployees(loadedDepartments);
            List<Employee> cleanEmployees = new ArrayList<>();
            Set<Integer> seenIds = new HashSet<>();
            if (loadedEmployees != null) {
                for (Employee e : loadedEmployees) {
                    // a hand-edited file could contain a duplicate ID: keep the first one only
                    if (e != null && seenIds.add(e.getEmployeeId())) {
                        cleanEmployees.add(e);
                    }
                }
            }

            this.departments = loadedDepartments;
            this.employees = cleanEmployees;

            if (seeded) {
                saveAll();
            }
        } catch (Exception e) {
            // keep whatever is already in memory; never crash the application
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------- helpers

    private int indexOfEmployee(int id) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getEmployeeId() == id) {
                return i;
            }
        }
        return -1;
    }

    /** A department is valid only if one with the same ID is registered. */
    private boolean isDepartmentRegistered(Department dept) {
        if (dept == null) {
            return false;
        }
        for (Department d : departments) {
            if (d.getDepartmentId() == dept.getDepartmentId()) {
                return true;
            }
        }
        return false;
    }
}