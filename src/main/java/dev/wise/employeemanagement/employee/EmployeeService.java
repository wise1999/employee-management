package dev.wise.employeemanagement.employee;

import dev.wise.employeemanagement.exception.DuplicateResourceException;
import dev.wise.employeemanagement.exception.RequestValidationException;
import dev.wise.employeemanagement.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeDao employeeDao;
    private final EmployeeDTOMapper employeeDTOMapper;

    public EmployeeService(
            EmployeeDao employeeDao,
            EmployeeDTOMapper employeeDTOMapper
    ) {
        this.employeeDao = employeeDao;
        this.employeeDTOMapper = employeeDTOMapper;
    }

    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeDao.selectAllEmployees();

        if (employees != null) {
            return employees.stream()
                    .map(employeeDTOMapper)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public EmployeeDTO getEmployeeById(Long id) {
        return employeeDao.selectEmployeeById(id)
                .map(employeeDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee with id [%s] not found".formatted(id)
                ));
    }


    public void addEmployee(EmployeeRegistrationRequest request) {
        String email = request.email();

        if(employeeDao.existEmployeeWithEmail(email)) {
            throw new DuplicateResourceException(
                    "email is already taken"
            );
        }

        Employee employee = new Employee(
                request.name(),
                request.email(),
                request.gender()
        );

        employeeDao.insertEmployee(employee);
    }

    public void updateEmployee(Long employeeId, EmployeeUpdateRequest request) {
        Employee employee = employeeDao.selectEmployeeById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee with id [%s] not found".formatted(employeeId)
                ));;

        boolean changed = false;

        if(request.name() != null && !request.name().equals(employee.getName())){
            employee.setName(request.name());
            changed = true;
        }

        if(request.email() != null && !request.email().equals(employee.getEmail())) {
            if( employeeDao.existEmployeeWithEmail(request.email()) ) {
                throw new DuplicateResourceException(
                        "email is already taken"
                );
            }

            employee.setEmail(request.email());
            changed = true;
        }

        if(!changed) {
            throw new RequestValidationException("No data changes found");
        }

        employeeDao.updateEmployee(employee);
    }

    public void deleteEmployeeById(Long id) {

        if(!employeeDao.existEmployeeWithId(id)) {
            throw new ResourceNotFoundException(
                    "Employee with id [%s] not found".formatted(id)
            );
        }

        employeeDao.deleteEmployeeById(id);
    }
}
