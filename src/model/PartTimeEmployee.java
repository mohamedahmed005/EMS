
package model;

/** OWNER: Member A (Model). Salary = hourlyRate * hoursWorked */
public class PartTimeEmployee extends Employee {
    private double hourlyRate;
    private double hoursWorked;

    public PartTimeEmployee(int id, String name, int age,
                            Department dept, String pos,
                            double hourlyRate, double hoursWorked) {

        super(id, name, age, dept, pos);
        setHourlyRate(hourlyRate);
        setHoursWorked(hoursWorked);
    }

    @Override
    public double calculateSalary(){
        return hourlyRate *hoursWorked;
    }
    @Override
    public String getEmployeeType(){
        return "part-Time";
    }


    public double getHourlyRate(){
        return hourlyRate;
    }
    public void setHourlyRate(double rate){
        if (!Double.isFinite(rate) || rate<0){
            throw new IllegalArgumentException(
                "hourly rate must be a valid non-negative number");
        }
        this.hourlyRate=rate;
    }


    public double getHoursWorked(){
        return hoursWorked;
    }
    public void setHoursWorked(double hours){
        if (!Double.isFinite(hours) || hours <0){
            throw new IllegalArgumentException("hours worked must be a valid non-negative number");
        }
        this.hoursWorked =hours;
    }

    
    @Override
    public String toFileString(){
        return "PART," + super.toFileString() + "," + hourlyRate + "," + hoursWorked;
    }
}
