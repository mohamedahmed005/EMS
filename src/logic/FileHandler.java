package logic;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import model.*;

/**
 * OWNER: Member C. Reads/writes data/employees.csv and data/departments.csv.
 * Rebuilds the right subclass (FullTime / PartTime) when loading.
 * Missing file on first launch => returns an empty list. Never crashes on IO
 * errors or malformed lines (bad lines are skipped with a warning).
 *
 * Formats:
 * departments.csv : id,name
 * employees.csv : FULL,id,name,age,deptId,position,basic,bonus
 * PART,id,name,age,deptId,position,rate,hours
 */
public class FileHandler {
    private String employeeFilePath;
    private String departmentFilePath;

    public FileHandler(String empFile, String deptFile) {
        if (empFile == null || empFile.isEmpty() || deptFile == null || deptFile.isEmpty()) {
            throw new IllegalArgumentException("File paths cannot be null or empty.");
        }
        this.employeeFilePath = empFile;
        this.departmentFilePath = deptFile;
    }

    public void saveEmployees(List<Employee> employees) {
        if (employees == null)
            return;
        ensureDirectoryExists(employeeFilePath);

        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(employeeFilePath))) {
            for (Employee emp : employees) {
                writer.write(emp.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving employees: " + e.getMessage());
        }
    }

    public void saveDepartments(List<Department> departments) {
        if (departments == null)
            return;
        ensureDirectoryExists(departmentFilePath);
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(departmentFilePath))) {
            for (Department dept : departments) {
                writer.write(dept.getDepartmentId() + "," + dept.getDepartmentName());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving departments: " + e.getMessage());
        }
    }

    public List<Employee> loadEmployees(List<Department> departments) {
        List<Employee> list = new ArrayList<>();
        if (departments == null)
            return list;
        File file = new File(employeeFilePath);
        if (!file.exists()) {
            return list;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                String[] parts = line.split(",");
                if (parts.length < 8)
                    continue;
                try {
                    String type = parts[0].trim();
                    int id = Integer.parseInt(parts[1].trim());
                    String name = parts[2].trim();
                    int age = Integer.parseInt(parts[3].trim());
                    int deptId = Integer.parseInt(parts[4].trim());
                    String position = parts[5].trim();
                    Department dept = findDepartmentById(departments, deptId);
                    if ("FULL".equalsIgnoreCase(type)) {
                        double basic = Double.parseDouble(parts[6].trim());
                        double bonus = Double.parseDouble(parts[7].trim());
                        list.add(new FullTimeEmployee(id, name, age, dept, position, basic, bonus));
                    } else if ("PART".equalsIgnoreCase(type)) {
                        double rate = Double.parseDouble(parts[6].trim());
                        double hours = Double.parseDouble(parts[7].trim());
                        list.add(new PartTimeEmployee(id, name, age, dept, position, rate, hours));
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

    private void ensureDirectoryExists(String filePath) {
        File file = new File(filePath);
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
    }

    private Department findDepartmentById(List<Department> departments, int deptId) {
        if (departments != null) {
            for (Department dept : departments) {
                if (dept.getDepartmentId() == deptId) {
                    return dept;
                }
            }
        }
        return new Department(deptId, "Unknown");
    }

    public List<Department> loadDepartments() {
        List<Department> list = new ArrayList<>();
        File file = new File(departmentFilePath);
        if (!file.exists()) {
            return list;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank())
                    continue;
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        list.add(new Department(id, name));
                    } catch (IllegalArgumentException e) {
                        System.err.println("Skipping invalid department line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading departments: " + e.getMessage());
        }
        return list;
    }
}