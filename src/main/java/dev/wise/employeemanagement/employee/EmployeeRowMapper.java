package dev.wise.employeemanagement.employee;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EmployeeRowMapper implements RowMapper<Employee> {
    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Employee(
                (int) rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                Gender.valueOf(rs.getString("gender"))
        );
    }
}
