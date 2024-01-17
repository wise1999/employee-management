package dev.wise.employeemanagement.employee;

public record EmployeeDTO (
        Integer id,
        String name,
        String email,
        Gender gender
) {
}
