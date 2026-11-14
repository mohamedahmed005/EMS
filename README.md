# Employee Management System (EMS) – Lab 7

## Group
| Member | ID |
|--------|----|
| A | ____ |
| B | ____ |
| C | ____ |
| D | ____ |

Group number: ____

## Description
Java Swing desktop app to manage employees (Full-Time / Part-Time) and departments:
login, add / view / update / delete / search / filter, salary calculation, file persistence.

## Compile & run
```
cd src
javac -d ../out Main.java model/*.java logic/*.java gui/*.java
cd ..
java -cp out Main
```
(run from the project root so the `data/` folder is found)

## Login
Username: `admin`  Password: `admin123`

## Data storage
CSV files in `data/`: `employees.csv`, `departments.csv`
(created automatically on first save; a missing file = empty list)

## Project layout
```
src/model   Employee (abstract), FullTimeEmployee, PartTimeEmployee, Department
src/logic   EmployeeManager, FileHandler, InputValidator
src/gui     LoginFrame, MainDashboardFrame, EmployeeManagementPanel, DepartmentManagementPanel
src/Main.java
docs/       UML class diagram (.pdf/.png/.drawio)
screenshots/ main screens
```
