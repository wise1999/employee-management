package dev.wise.employeemanagement.employee;

public record EmployeeRegistrationRequest(
        String name,
        String email,
        Gender gender
) {
}
