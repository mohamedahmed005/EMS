# Employee Management System (EMS)

A Java Swing desktop application for managing employees and departments. Users sign in, then use a dashboard to manage employee records and departments. Data is stored locally in CSV files.

## Features

- Dashboard home page with summary cards: employee count, department count, full-time / part-time split, and total monthly payroll.
- Add, update, and delete employee records.
- Live search by employee ID or name (partial, case-insensitive), updating as you type.
- Combined filters: department, employee type (Full-Time / Part-Time), and search text.
- Full-time employees with a base salary and bonus; part-time employees with an hourly rate and hours worked.
- Add, view, and remove departments. A department cannot be removed while employees are assigned to it.
- Input validation for required fields, positive IDs and ages, and non-negative pay values.
- Persistent storage in CSV files (UTF-8). If saving fails, the change is rolled back and an error message is shown.

## Requirements

- Java JDK 17 or later (the source uses pattern matching for `instanceof`).
- A desktop environment that supports Swing.

## Build and Run

Run these commands from the project root (`EMS_Project`) in PowerShell:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -encoding UTF-8 -d out -sourcepath src src\Main.java
java -cp out Main
```

The application uses paths relative to the working directory, so start it from the project root to keep data in the project's `data` folder.

## Login

The default credentials are hard-coded in `logic.EmployeeManager`:

| Field    | Value      |
| -------- | ---------- |
| Username | `admin`    |
| Password | `admin123` |

Change these credentials and add proper credential protection before using the application beyond a local demonstration.

## Using the Application

1. Sign in with the credentials above.
2. Use the sidebar to open **Home / Overview**, **Manage Employees**, or **Manage Departments**.
3. In **Manage Departments**, add any departments you need. Employees must belong to a registered department.
4. In **Manage Employees**, choose Full-Time or Part-Time and fill in the matching salary fields.
5. Click a row in the table to load it into the form for editing or deletion.
6. Use the search box and the Department and Type filters to narrow the list. **Show All** resets them.

## Data Storage

The `data` directory is created automatically on the first save.

- `data/departments.csv`: `departmentId,departmentName`
- `data/employees.csv`:
  - Full-time: `FULL,id,name,age,deptId,position,basicSalary,bonus`
  - Part-time: `PART,id,name,age,deptId,position,hourlyRate,hoursWorked`

Details:

- Commas and backslashes inside text fields are escaped with a backslash, so names such as `Ahmed, Ali` are saved and loaded correctly.
- Files are read and written as UTF-8.
- Malformed lines, duplicate employee IDs, and employees that refer to an unknown department are skipped on load, with a warning in the console.
- On first launch, when no departments exist, the defaults IT, Human Resources, Finance, Marketing, and Sales are created.

Back up the `data` directory to preserve records. If it contains real employee data, add `data/` to `.gitignore` so it is not pushed to the repository.

## Project Structure

```text
src/
├── Main.java
├── gui/
│   ├── LoginFrame.java
│   ├── MainDashboardFrame.java
│   ├── EmployeeManagementPanel.java
│   └── DepartmentManagementPanel.java
├── logic/
│   ├── EmployeeManager.java
│   ├── FileHandler.java
│   └── InputValidator.java
└── model/
    ├── Department.java
    ├── Employee.java
    ├── FullTimeEmployee.java
    └── PartTimeEmployee.java
```

- **`gui`**: Swing screens and user interaction.
- **`logic`**: employee and department operations, validation, and CSV persistence.
- **`model`**: domain classes.

## Object-Oriented Design

- **Encapsulation:** model fields are private and accessed through methods that validate values.
- **Abstraction:** `Employee` defines the shared data and the `calculateSalary()` contract.
- **Inheritance and polymorphism:** `FullTimeEmployee` and `PartTimeEmployee` extend `Employee` and calculate salary differently.
- **Separation of concerns:** GUI, business logic, persistence, validation, and models live in separate packages.

## Possible Improvements (SOLID)

- **SRP:** the Swing panels combine layout, input handling, and validation; these could be separated.
- **OCP:** adding a new employee type currently means editing the GUI and file-loading branches. A factory for creating employees would reduce this.
- **DIP:** `EmployeeManager` depends on the concrete `FileHandler`. A small storage interface would make persistence replaceable and the manager easier to test.
- **Security:** credentials are hard-coded and should be replaced with a proper mechanism.

These are observations about the current code, not claims that every principle or a specific design pattern is already implemented.