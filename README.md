# Employee Management System (EMS)

A Java Swing desktop application for managing employees and departments. Users sign in, then navigate a dashboard to manage employee records and departments. Data is stored locally in CSV files.

## Features

- Add, update, delete, search, and filter employee records.
- Manage full-time employees with a base salary and bonus.
- Manage part-time employees with an hourly rate and hours worked.
- Add and view departments.
- Calculate salary according to employee type.
- Validate required fields, positive IDs and ages, and non-negative pay values.
- Save and reload employee and department data from CSV files.

## Requirements

- Java JDK 17 or later. The source uses modern Java pattern matching syntax.
- A desktop environment that supports Swing.

## Build and Run

Run these commands from the project root (`EMS_Project`) in PowerShell:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -encoding UTF-8 -d out -sourcepath src src\Main.java
java -cp out Main
```

The application uses paths relative to the current working directory, so start it from the project root for data to be saved under the project’s `data` folder.

> **Current build issue:** `DepartmentManagementPanel` calls `EmployeeManager.removeDepartment(int)`, but that method is not currently defined. Implement the method or update the panel before compiling the complete application.

## Login

The default credentials are hard-coded in `logic.EmployeeManager`:

| Field | Value |
| --- | --- |
| Username | `admin` |
| Password | `admin123` |

Change these credentials and add appropriate credential protection before using the application beyond a local demonstration.

## Using the Application

1. Sign in with the credentials above.
2. Use the dashboard navigation to open employee or department management.
3. In employee management, choose full-time or part-time and enter the corresponding salary fields.
4. Add the departments employees will belong to. Employee records must refer to a registered department.
5. Select an employee row to load it into the form for editing or deletion; use the search and department filter to narrow the list.

## Data Storage

The application creates the `data` directory when it saves data if the directory does not exist.

- `data/departments.csv` stores `departmentId,departmentName`.
- `data/employees.csv` stores a type tag and employee fields. Full-time rows use `FULL` and contain base salary and bonus; part-time rows use `PART` and contain hourly rate and hours worked.

When no departments are loaded, `EmployeeManager` seeds these defaults: IT, Human Resources, Finance, Marketing, and Sales. Back up the `data` directory to preserve records.

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

- **`gui`** contains the Swing screens and user interactions.
- **`logic`** contains employee and department operations, input validation, and CSV persistence.
- **`model`** contains the department and employee domain classes.

## Object-Oriented Design

The project uses several object-oriented concepts:

- **Encapsulation:** model fields are private and accessed through methods that validate values.
- **Abstraction:** `Employee` defines shared employee data and the `calculateSalary()` contract.
- **Inheritance and polymorphism:** `FullTimeEmployee` and `PartTimeEmployee` extend `Employee` and calculate salaries differently.
- **Separation by responsibility:** GUI, business logic, persistence, validation, and domain models are organized in separate packages.

## SOLID Review and Improvement Opportunities

The lecture introduces SOLID as a guide for reducing coupling and making software easier to extend. This project has a starting separation of concerns, with opportunities to strengthen it:

- **Single Responsibility Principle (SRP):** `FileHandler` handles CSV I/O and `InputValidator` handles common checks. The Swing panels still combine layout, input handling, validation, and user messages; extracting some of these tasks could make them easier to maintain.
- **Open-Closed Principle (OCP):** employee subclasses let each type provide its own salary calculation. Adding a new employee type still requires updating GUI and file-loading branches, so a factory or type-specific creation abstraction could reduce those edits.
- **Liskov Substitution Principle (LSP):** both employee subclasses are used through the `Employee` type and implement its salary contract.
- **Interface Segregation Principle (ISP):** there are no broad custom interfaces at present. If the application grows, small interfaces for storage or employee creation can avoid exposing unrelated operations to callers.
- **Dependency Inversion Principle (DIP):** `EmployeeManager` currently receives a concrete `FileHandler`. Depending on a small storage interface would make persistence easier to replace and manager behavior easier to test.

These are observations about the current implementation and possible next steps; they are not claims that the project already implements every SOLID principle or a named GoF pattern.

## Design Pattern Context

Design patterns are reusable approaches to recurring design problems. They are commonly grouped into:

- **Creational:** object creation, such as Factory Method, Abstract Factory, Builder, Prototype, and Singleton.
- **Structural:** how objects and classes are composed.
- **Behavioral:** how responsibilities and communication are distributed.

The current code uses object-oriented techniques such as inheritance, polymorphism, and Swing event listeners. No specific Gang of Four pattern is documented as an intentional implementation. A small factory for constructing employee objects from form data or CSV rows is a possible future improvement because creation logic currently branches on employee type.
