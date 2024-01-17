package dev.wise.employeemanagement.employee;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeJDBCDataAccessService implements EmployeeDao {
    private final JdbcTemplate jdbcTemplate;
    private final EmployeeRowMapper employeRowMapper;

    public EmployeeJDBCDataAccessService(
            JdbcTemplate jdbcTemplate,
            EmployeeRowMapper employeRowMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.employeRowMapper = employeRowMapper;
    }
    @Override
    public List<Employee> selectAllEmployees() {
        var sql = """
                SELECT id, name, email, gender
                FROM employee
                """;

        return jdbcTemplate.query(sql, employeRowMapper);
    }

    @Override
    public Optional<Employee> selectEmployeeById(Long id) {
        var sql = """
                SELECT id, name, email, gender
                FROM employee
                WHERE id = ?
                """;

        return jdbcTemplate.query(sql, employeRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public void insertEmployee(Employee employee) {
        var sql = """
                INSERT INTO employee(name, email, gender)
                VALUES (?, ?, ?)
                """;
        jdbcTemplate.update(
                sql,
                employee.getName(),
                employee.getEmail(),
                employee.getGender().name()
        );
    }

    @Override
    public boolean existEmployeeWithEmail(String email) {
        var sql = """
                SELECT count(id)
                FROM employee
                WHERE email = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existEmployeeWithId(Long id) {
        var sql = """
                SELECT count(id)
                FROM employee
                WHERE id = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void updateEmployee(Employee update) {
        if(update.getName() != null) {
            String sql = "UPDATE employee SET name = ? WHERE id = ?";
            jdbcTemplate.update(
                    sql,
                    update.getName(),
                    update.getId()
            );
        }

        if(update.getEmail() != null) {
            String sql = "UPDATE employee SET email = ? WHERE id = ?";
            jdbcTemplate.update(
                    sql,
                    update.getEmail(),
                    update.getId()
            );
        }
    }

    @Override
    public void deleteEmployeeById(Long id) {
        var sql = """
                DELETE
                FROM employee
                WHERE id = ?
                """;

        int result = jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Employee> selectUserByEmail(String email) {
        var sql = """
                SELECT id, name, email, gender
                FROM customer
                WHERE email = ?
                """;

        return jdbcTemplate.query(sql, employeRowMapper, email)
                .stream()
                .findFirst();
    }
}
