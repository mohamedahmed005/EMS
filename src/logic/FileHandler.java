
package logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import model.Department;
import model.Employee;
import model.FullTimeEmployee;
import model.PartTimeEmployee;

/**
 * Reads/writes data/employees.csv and data/departments.csv (UTF-8).
 * Rebuilds the right subclass (FullTime / PartTime) when loading.
 * Validates text and numeric inputs during loading to ensure data integrity.
 * A missing file on first launch returns an empty list. Never crashes on IO
 * errors or malformed lines (bad lines are skipped with a warning).
 * Commas and backslashes inside text fields are escaped with a backslash.
 */
public class FileHandler {
    private final String employeeFilePath;
    private final String departmentFilePath;

    public FileHandler(String empFile, String deptFile) {
        if (empFile == null || empFile.isEmpty() || deptFile == null || deptFile.isEmpty()) {
            throw new IllegalArgumentException("File paths cannot be null or empty.");
        }
        this.employeeFilePath = empFile;
        this.departmentFilePath = deptFile;
    }

    // ------------------------------------------------------------------ save

    /** Returns true only if the file was written successfully. */
    public boolean saveEmployees(List<Employee> employees) {
        if (employees == null) {
            return false;
        }
        ensureDirectoryExists(employeeFilePath);

        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(employeeFilePath))) {
            for (Employee emp : employees) {
                String line = toLine(emp);
                if (line != null) {
                    writer.write(line);
                    writer.newLine();
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving employees: " + e.getMessage());
            return false;
        }
    }

    /** Returns true only if the file was written successfully. */
    public boolean saveDepartments(List<Department> departments) {
        if (departments == null) {
            return false;
        }
        ensureDirectoryExists(departmentFilePath);

        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(departmentFilePath))) {
            for (Department dept : departments) {
                writer.write(dept.getDepartmentId() + "," + escape(dept.getDepartmentName()));
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving departments: " + e.getMessage());
            return false;
        }
    }

    // ------------------------------------------------------------------ load

    public List<Employee> loadEmployees(List<Department> departments) {
        List<Employee> list = new ArrayList<>();
        if (departments == null) {
            return list;
        }
        File file = new File(employeeFilePath);
        if (!file.exists()) {
            return list;
        }

        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = splitCsv(line);
                if (parts.length < 8) {
                    System.err.println("Skipping invalid employee line: " + line);
                    continue;
                }
                try {
                    String type = parts[0].trim();
                    int id = Integer.parseInt(parts[1].trim());
                    String name = parts[2].trim();
                    String ageStr = parts[3].trim();
                    int deptId = Integer.parseInt(parts[4].trim());
                    String position = parts[5].trim();

                    // 1. Validation: Name must contain letters and spaces only
                    if (!name.matches("^[a-zA-Z\\s]+$")) {
                        System.err.println("Skipping employee line (invalid name, letters only allowed): " + line);
                        continue;
                    }

                    // 2. Validation: Age must be a positive integer (> 0)
                    int age = Integer.parseInt(ageStr);
                    if (age <= 0) {
                        System.err.println("Skipping employee line (age must be a positive integer): " + line);
                        continue;
                    }

                    Department dept = findDepartmentById(departments, deptId);
                    if (dept == null) {
                        System.err.println("Skipping employee with unknown department: " + line);
                        continue;
                    }

                    if ("FULL".equalsIgnoreCase(type)) {
                        double basic = Double.parseDouble(parts[6].trim());
                        double bonus = Double.parseDouble(parts[7].trim());
                        list.add(new FullTimeEmployee(id, name, age, dept, position, basic, bonus));
                    } else if ("PART".equalsIgnoreCase(type)) {
                        double rate = Double.parseDouble(parts[6].trim());
                        double hours = Double.parseDouble(parts[7].trim());
                        list.add(new PartTimeEmployee(id, name, age, dept, position, rate, hours));
                    } else {
                        System.err.println("Skipping employee with unknown type: " + line);
                    }
                } catch (Exception e) {
                    System.err.println("Skipping invalid employee line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading employees: " + e.getMessage());
        }
        return list;
    }

    public List<Department> loadDepartments() {
        List<Department> list = new ArrayList<>();
        File file = new File(departmentFilePath);
        if (!file.exists()) {
            return list;
        }

        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = splitCsv(line);
                if (parts.length >= 2) {
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        list.add(new Department(id, name));
                    } catch (Exception e) {
                        System.err.println("Skipping invalid department line: " + line);
                    }
                } else {
                    System.err.println("Skipping invalid department line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading departments: " + e.getMessage());
        }
        return list;
    }

    // --------------------------------------------------------------- helpers

    /** Builds one CSV line for the employee, or null if it cannot be saved. */
    private String toLine(Employee emp) {
        if (emp == null || emp.getDepartment() == null) {
            return null;
        }
        String common = emp.getEmployeeId() + ","
                + escape(emp.getFullName()) + ","
                + emp.getAge() + ","
                + emp.getDepartment().getDepartmentId() + ","
                + escape(emp.getPosition());

        if (emp instanceof FullTimeEmployee ft) {
            return "FULL," + common + "," + ft.getBasicSalary() + "," + ft.getBonus();
        } else if (emp instanceof PartTimeEmployee pt) {
            return "PART," + common + "," + pt.getHourlyRate() + "," + pt.getHoursWorked();
        }
        return null;
    }

    private void ensureDirectoryExists(String filePath) {
        File parent = new File(filePath).getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }

    /** Returns the department with this ID, or null if it is not registered. */
    private Department findDepartmentById(List<Department> departments, int deptId) {
        for (Department dept : departments) {
            if (dept.getDepartmentId() == deptId) {
                return dept;
            }
        }
        return null;
    }

    /** Escapes backslashes and commas so a text field can never break the line. */
    private static String escape(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\", "\\\\").replace(",", "\\,");
    }

    /** Splits a line on commas that are not escaped, and removes the escapes. */
    private static String[] splitCsv(String line) {
        List<String> parts = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\\' && i + 1 < line.length()) {
                cur.append(line.charAt(++i));
            } else if (c == ',') {
                parts.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        parts.add(cur.toString());
        return parts.toArray(new String[0]);
    }
}