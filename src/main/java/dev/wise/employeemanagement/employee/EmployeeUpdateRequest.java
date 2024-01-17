package dev.wise.employeemanagement.employee;

public record EmployeeUpdateRequest (
        String name,
        String email,
        Gender gender
) {
}
