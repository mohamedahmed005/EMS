```java
package model;

/** OWNER: Member A (Model). Abstraction + Encapsulation. */
public abstract class Employee {
    private int employeeId;
    private String fullName;
    private int age;
    private Department department;
    private String position;

    public Employee(int employeeId, String fullName, int age, Department department, String position){

        this.employeeId =employeeId;
        this.fullName =fullName;
        this.department =department;
        this.age =age;
        this.position =position;
    }

    public abstract double calculateSalary(); // Polymorphism: each subclass has its own formula
    public abstract String getEmployeeType(); // "Full-Time" / "Part-Time"

    public int getEmployeeId() { return employeeId;}
    public void setEmployeeId(int id) { this.employeeId=id;}

    public String getFullName() { return fullName;}
    public void setFullName(String name) {this.fullName=name;}

    public int getAge() { return age;}
    public void setAge(int age) { this.age=age; }

    public Department getDepartment() { return department;}
    public void setDepartment(Department dept) { this.department=dept;}

    public String getPosition() { return position;}
    public void setPosition(String pos) { this.position=pos;}

    /** One CSV line that can rebuild this object, e.g. FULL,id,name,age,deptId,position,basic,bonus */
    public String toFileString(){
        return employeeId + "," + fullName + "," + age + ","
                + department.getDepartmentId() + "," + position;
    }
 }