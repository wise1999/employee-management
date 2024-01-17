package dev.wise.employeemanagement.employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeDao {
    List<Employee> selectAllEmployees();

    Optional<Employee> selectEmployeeById(Long id);

    void insertEmployee(Employee customer);
    boolean existEmployeeWithEmail(String email);
    boolean existEmployeeWithId(Long id);
    void updateEmployee(Employee update);
    void deleteEmployeeById(Long id);
    Optional<Employee> selectUserByEmail(String email);
}