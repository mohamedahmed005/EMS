
package model;

/** OWNER: Member A (Model). Abstraction + Encapsulation. */
public abstract class Employee {
    private int employeeId;
    private String fullName;
    private int age;
    private Department department;
    private String position;

    
    public Employee(int employeeId, String fullName, int age,Department department, String position){
        setEmployeeId(employeeId);
        setFullName(fullName);
        setAge(age);
        setDepartment(department);
        setPosition(position);
    }

    public abstract double calculateSalary();
    public abstract String getEmployeeType();

    public int getEmployeeId(){
        return employeeId;
    }
    public void setEmployeeId(int id){
        if (id <= 0) {
            throw new IllegalArgumentException(
                "employee ID must be greater than zero!"
            );
        }
        this.employeeId =id;
    }


    public String getFullName(){
        return fullName;
    }
    public void setFullName(String name){
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("employee name cannot be empty!");
        }
         this.fullName =name.trim();
    }


    public int getAge(){
        return age;
    }
    public void setAge(int age){
        if (age <= 0){
            throw new IllegalArgumentException("age must be greater than zero!");
        }
         this.age =age;
    }


    public Department getDepartment(){
        return department;
    }
    public void setDepartment(Department dept){
        if (dept == null){
            throw new IllegalArgumentException("department cannot be null !");
        }
        this.department =dept;
    }


    public String getPosition(){
        return position;
    }
    public void setPosition(String pos){
        if (pos == null || pos.trim().isEmpty()){
            throw new IllegalArgumentException("Position cannot be empty.");
        }
        this.position =pos.trim();
    }

    /** Returns the common employee data as a CSV line. */
    public String toFileString(){
        return employeeId + "," + fullName + "," + age + ","
                + department.getDepartmentId() + "," + position;
    }
}
