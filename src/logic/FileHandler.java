package logic;

import java.util.List;
import model.Department;
import model.Employee;

/**
 * OWNER: Member C. Reads/writes data/employees.csv and data/departments.csv.
 * Must rebuild the right subclass (FullTime / PartTime) when loading.
 * Missing file on first launch => return an empty list. Never crash on IO errors.
 * Suggested formats:
 *   departments.csv : id,name
 *   employees.csv   : FULL,id,name,age,deptId,position,basic,bonus
 *                     PART,id,name,age,deptId,position,rate,hours
 */
public class FileHandler {
    private String employeeFilePath;
    private String departmentFilePath;

    public FileHandler(String empFile, String deptFile) {
        // TODO
    }
    public void saveEmployees(List<Employee> employees) { /* TODO */ }
    public List<Employee> loadEmployees(List<Department> departments) { return null; /* TODO */ }
    public void saveDepartments(List<Department> departments) { /* TODO */ }
    public List<Department> loadDepartments() { return null; /* TODO */ }
}
