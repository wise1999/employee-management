package dev.wise.employeemanagement.employee;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class EmployeeDTOMapper implements Function<Employee, EmployeeDTO> {

    @Override
    public EmployeeDTO apply(Employee employee) {
        return new EmployeeDTO(
                (int) employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getGender()
        );
    }
}
