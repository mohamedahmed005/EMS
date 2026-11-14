# Work split – 4 members, equal effort (~8 points each)

Effort points: 1 = small class, 2-3 = medium, 4-5 = logic-heavy, 7 = big GUI.

| Member | Classes | Points |
|--------|---------|--------|
| **A** | Department, Employee, FullTimeEmployee, PartTimeEmployee (4) + LoginFrame (2) + README, screenshots & ZIP packaging (2) | **8** |
| **B** | EmployeeManager (5) + DepartmentManagementPanel (3) | **8** |
| **C** | FileHandler (4) + MainDashboardFrame (3) + Main.java (1) | **8** |
| **D** | EmployeeManagementPanel (7) + InputValidator (1) | **8** |

## Order of work (so nobody is blocked)
1. **Day 1:** everyone clones this skeleton. It already has every class, field and method signature
   from the UML, so each person codes against the others' signatures from the start.
   A finishes the model first (it is quick) and pushes it.
2. **Day 2-3:** everyone implements their own classes + tests them alone (a tiny `main` is enough).
3. **Day 4:** integrate: C runs Main, D tests the form against B's manager, A tests login.
4. **Day 5:** everyone tests the full workflow from the lab (section 9), A takes screenshots and zips.

## Agreed contracts (do not change without telling the group)
- Employee CSV: `FULL,id,name,age,deptId,position,basic,bonus` / `PART,id,name,age,deptId,position,rate,hours`
- Department CSV: `id,name`
- Every add/update/delete in `EmployeeManager` calls `saveAll()`.
- GUI never touches files or business rules; it validates input with `InputValidator`, then calls the manager.

## Everyone must understand the whole system
The lab discussion asks each member about ALL classes, so swap-review each other's code before submission:
A reviews B, B reviews C, C reviews D, D reviews A.
