package model;

/** OWNER: Member A (Model). Salary = hourlyRate * hoursWorked */
public class PartTimeEmployee extends Employee {
    private double hourlyRate;
    private double hoursWorked;

    public PartTimeEmployee(int id, String name, int age, Department dept, String pos, double hourlyRate, double hoursWorked) {
        super(id, name, age, dept, pos);
        // TODO
    }
    @Override public double calculateSalary() { return 0; /* TODO */ }
    @Override public String getEmployeeType() { return null; /* TODO */ }
    public double getHourlyRate() { return 0; /* TODO */ }
    public void setHourlyRate(double rate) { /* TODO */ }
    public double getHoursWorked() { return 0; /* TODO */ }
    public void setHoursWorked(double hours) { /* TODO */ }
}
