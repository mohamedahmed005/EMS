package model;

/** OWNER: Member A (Model). Salary = basicSalary + bonus */
public class FullTimeEmployee extends Employee {
    private double basicSalary;
    private double bonus;

    public FullTimeEmployee(int id, String name, int age, Department dept, String pos, double basicSalary, double bonus) {
        super(id, name, age, dept, pos);
        // TODO
    }
    @Override public double calculateSalary() { return 0; /* TODO */ }
    @Override public String getEmployeeType() { return null; /* TODO */ }
    public double getBasicSalary() { return 0; /* TODO */ }
    public void setBasicSalary(double salary) { /* TODO */ }
    public double getBonus() { return 0; /* TODO */ }
    public void setBonus(double bonus) { /* TODO */ }
}
