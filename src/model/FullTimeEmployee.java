
package model;

/** OWNER: Member A (Model). Salary = basicSalary + bonus */
public class FullTimeEmployee extends Employee {
    private double basicSalary;
    private double bonus;

    public FullTimeEmployee(int id, String name, int age,
                            Department dept, String pos,
                            double basicSalary, double bonus){

        super(id, name, age, dept, pos);
        setBasicSalary(basicSalary);
        setBonus(bonus);
    }


    @Override
    public double calculateSalary(){
        return basicSalary + bonus;
    }
    @Override
    public String getEmployeeType(){
        return "full-Time";
    }


    public double getBasicSalary(){
        return basicSalary;
    }
    public void setBasicSalary(double salary){
        if (!Double.isFinite(salary) || salary < 0){
            throw new IllegalArgumentException("basic salary must be a valid non-negative number");
        }
        this.basicSalary =salary;
    }


    public double getBonus(){
        return bonus;
    }
    public void setBonus(double bonus){
        if (!Double.isFinite(bonus) || bonus < 0){
            throw new IllegalArgumentException("bonus must be a valid non-negative number!");
        }
        this.bonus=bonus;
    }


    @Override
    public String toFileString(){
        return "FULL," + super.toFileString() + "," + basicSalary + "," + bonus;
    }
}
