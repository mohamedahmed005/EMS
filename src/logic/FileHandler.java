package logic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import model.Department;
import model.Employee;
import model.FullTimeEmployee;
import model.PartTimeEmployee;

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
        if (empFile == null || empFile.isEmpty() || deptFile == null || deptFile.isEmpty()) {
            throw new IllegalArgumentException("File paths cannot be null or empty");
        }
        this.employeeFilePath = empFile;
        this.departmentFilePath = deptFile;
    }

    // 1. حفظ الأقسام في ملف الـ CSV
    public void saveDepartments(List<Department> departments) {
        // إنشاء الفولدر أوتوماتيكياً لو مش موجود
        ensureDirectoryExists(departmentFilePath);

        try (PrintWriter writer = new PrintWriter(new FileWriter(departmentFilePath))) {
            for (Department dept : departments) {
                // الشراء والصيغة: id,name
                writer.println(dept.getDepartmentId() + "," + dept.getDepartmentName());
            }
        } catch (IOException e) {
            System.err.println("Error saving departments: " + e.getMessage());
        }
    }

    // 2. قراءة الأقسام من ملف الـ CSV
    public List<Department> loadDepartments() {
        List<Department> list = new ArrayList<>();
        File file = new File(departmentFilePath);

        // لو الملف مش موجود أصلها يرجع قائمة فاضية من غير ما يضرب Error
        if (!file.exists()) {
            return list;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    list.add(new Department(id, name));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading departments: " + e.getMessage());
        }
        return list;
    }

    // 3. حفظ الموظفين في ملف الـ CSV
    public void saveEmployees(List<Employee> employees) {
        ensureDirectoryExists(employeeFilePath);

        try (PrintWriter writer = new PrintWriter(new FileWriter(employeeFilePath))) {
            for (Employee emp : employees) {
                // بننادي toFileString() المكتوبة جوه FullTime و PartTime
                writer.println(emp.toFileString());
            }
        } catch (IOException e) {
            System.err.println("Error saving employees: " + e.getMessage());
        }
    }

    // 4. قراءة الموظفين وإعادة بناء الكائنات (FullTime / PartTime)
    public List<Employee> loadEmployees(List<Department> departments) {
        List<Employee> list = new ArrayList<>();
        File file = new File(employeeFilePath);

        if (!file.exists()) {
            return list;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                
                // التأكد من أن السطر يحتوي على البيانات المطلوبة
                if (parts.length < 8) continue;

                String type = parts[0].trim();
                int id = Integer.parseInt(parts[1].trim());
                String name = parts[2].trim();
                int age = Integer.parseInt(parts[3].trim());
                int deptId = Integer.parseInt(parts[4].trim());
                String position = parts[5].trim();

                // البحث عن القسم المناسب بناءً على deptId
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
            }
        } catch (Exception e) {
            System.err.println("Error loading employees: " + e.getMessage());
        }
        return list;
    }

    // ميثود مساعدة لإنشاء مجلد data تلقائياً لو مش موجود
    private void ensureDirectoryExists(String filePath) {
        File file = new File(filePath);
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
    }

    // ميثود مساعدة للبحث عن الكائن الخاص بالقسم بناءً على الـ ID
    private Department findDepartmentById(List<Department> departments, int deptId) {
        for (Department d : departments) {
            if (d.getDepartmentId() == deptId) {
                return d;
            }
        }
        return new Department(deptId, "Unknown");
    }
}