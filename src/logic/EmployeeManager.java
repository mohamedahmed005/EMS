package logic;

import java.util.List;
import model.Department;
import model.Employee;

/**
 * OWNER: Member B. All business rules live here (NOT in the GUI):
 * unique IDs, unique dept id + name (case-insensitive), CRUD, search/filter,
 * login check (admin / admin123), and calling FileHandler after every change.
 */
public class EmployeeManager {
    private List<Employee> employees;
    private List<Department> departments;
    private FileHandler fileHandler;

    public EmployeeManager(FileHandler fileHandler) {
        // TODO
    }
    public boolean authenticate(String username, String password) { return false; /* TODO */ }
    public boolean addEmployee(Employee emp) { return false; /* TODO: reject duplicate id, save */ }
    public boolean updateEmployee(int id, Employee updatedEmp) { return false; /* TODO: new id must not duplicate, save */ }
    public boolean deleteEmployee(int id) { return false; /* TODO: save */ }
    public Employee searchById(int id) { return null; /* TODO */ }
    public List<Employee> searchByName(String name) { return null; /* TODO */ }
    public List<Employee> filterByDepartment(String deptName) { return null; /* TODO: case-insensitive */ }
    public boolean isEmployeeIdUnique(int id) { return false; /* TODO */ }
    public boolean addDepartment(Department dept) { return false; /* TODO: unique id + name, save */ }
    public boolean isDepartmentUnique(int id, String name) { return false; /* TODO */ }
    public List<Employee> getAllEmployees() { return null; /* TODO */ }
    public List<Department> getAllDepartments() { return null; /* TODO */ }
    public void saveAll() { /* TODO */ }
    public void loadAll() { /* TODO */ }
}
