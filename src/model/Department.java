
package model;

/** OWNER: Member A (Model) */
public class Department {
    private int departmentId;
    private String departmentName;

    public Department(int departmentId, String departmentName){
        setDepartmentId(departmentId);
        setDepartmentName(departmentName);
    }

    public int getDepartmentId(){
        return departmentId;
    }
    public void setDepartmentId(int id){
        if (id <=0){
            throw new IllegalArgumentException("department ID must be greater than zero!");
        }
        this.departmentId =id;
    }


    public String getDepartmentName(){
        return departmentName;
    }
    public void setDepartmentName(String name){
        if (name == null || name.trim().isEmpty()){
            throw new IllegalArgumentException("department name cannot be empty!");
        }
  
        String trimmed = name.trim();
        boolean isAllDigits = true;
         for (int i = 0; i < trimmed.length(); i++) {
             if (!Character.isDigit(trimmed.charAt(i))) {
             isAllDigits = false;
             break;
             }
        }
    
    if (isAllDigits) {
        throw new IllegalArgumentException("department name cannot contain only numbers");
        }

        this.departmentName =name.trim();
    }
    @Override
    public String toString() {
        return departmentName;
    }
}
