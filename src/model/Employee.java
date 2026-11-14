package model;

/** OWNER: Member A (Model). Abstraction + Encapsulation. */
public abstract class Employee {
    private int employeeId;
    private String fullName;
    private int age;
    private Department department;
    private String position;

    public Employee(int employeeId, String fullName, int age, Department department, String position) {
        // TODO
    }

    public abstract double calculateSalary();   // Polymorphism: each subclass has its own formula
    public abstract String getEmployeeType();   // "Full-Time" / "Part-Time"

    public int getEmployeeId() { return 0; /* TODO */ }
    public void setEmployeeId(int id) { /* TODO */ }
    public String getFullName() { return null; /* TODO */ }
    public void setFullName(String name) { /* TODO */ }
    public int getAge() { return 0; /* TODO */ }
    public void setAge(int age) { /* TODO */ }
    public Department getDepartment() { return null; /* TODO */ }
    public void setDepartment(Department dept) { /* TODO */ }
    public String getPosition() { return null; /* TODO */ }
    public void setPosition(String pos) { /* TODO */ }

    /** One CSV line that can rebuild this object, e.g. FULL,id,name,age,deptId,position,basic,bonus */
    public String toFileString() { return null; /* TODO (subclasses may override/extend) */ }
}
