package dev.wise.employeemanagement.employee;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<EmployeeDTO> getEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("{id}")
    public EmployeeDTO getEmployee(@PathVariable("id") Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping
    public void registerNewEmployee(@RequestBody EmployeeRegistrationRequest request) {
        employeeService.addEmployee(request);
    }

    @PutMapping("{id}")
    public void updateCustomer(@PathVariable("id") Long id, @RequestBody EmployeeUpdateRequest request) {
        employeeService.updateEmployee(id, request);
    }

    @DeleteMapping("{id}")
    public void deleteCustomer(@PathVariable("id") Long id) {
        employeeService.deleteEmployeeById(id);
    }
}
