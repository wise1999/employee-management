package dev.wise.employeemanagement.journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import dev.wise.employeemanagement.employee.EmployeeDTO;
import dev.wise.employeemanagement.employee.EmployeeRegistrationRequest;
import dev.wise.employeemanagement.employee.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class EmployeeIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final Random RANDOM = new Random();
    private static final String EMPLOYEE_API_PATH = "api/v1/employee";

    @Test
    void canRegisterAnEmployee() {
        // create registration request

        Faker faker = new Faker();

        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@example.com";
        Integer randomNumber = RANDOM.nextInt(1,100);
        Gender gender = randomNumber % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        EmployeeRegistrationRequest request = new EmployeeRegistrationRequest(
                name, email, gender
        );

        // send a post request

        webTestClient.post()
                .uri(EMPLOYEE_API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), EmployeeRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // list all employees

        List<EmployeeDTO> allEmployees = webTestClient.get()
                .uri(EMPLOYEE_API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<EmployeeDTO>() {
                })
                .returnResult()
                .getResponseBody();

        var id = allEmployees.stream()
                .filter(c -> c.email().equals(email))
                .map(EmployeeDTO::id)
                .findFirst()
                .orElseThrow();

        // make sure that employee is present
        EmployeeDTO expectedEmployee = new EmployeeDTO(
                id, name, email, gender
        );

        assertThat(allEmployees).contains(expectedEmployee);

        // get employee by id
        webTestClient.get()
                .uri(EMPLOYEE_API_PATH + "/{id}", id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<EmployeeDTO>() {
                })
                .isEqualTo(expectedEmployee);
    }

    @Test
    void canDeleteEmployee() {

        // create registration request
        Faker faker = new Faker();

        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@example.com";
        Integer randomNumber = RANDOM.nextInt(1,100);
        Gender gender = randomNumber % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        EmployeeRegistrationRequest request = new EmployeeRegistrationRequest(
                name, email, gender
        );

        // send a post request to create an employee
        webTestClient.post()
                .uri(EMPLOYEE_API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), EmployeeRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // list all employees
        List<EmployeeDTO> allEmployees = webTestClient.get()
                .uri(EMPLOYEE_API_PATH)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<EmployeeDTO>() {
                })
                .returnResult()
                .getResponseBody();

        // make sure that employee is present
        var id = allEmployees.stream()
                .filter(c -> c.email().equals(email))
                .map(EmployeeDTO::id)
                .findFirst()
                .orElseThrow();

        webTestClient.delete()
                .uri(EMPLOYEE_API_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        // second employee gets first employee by id
        webTestClient.get()
                .uri(EMPLOYEE_API_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateEmployee() {

        // create registration request
        Faker faker = new Faker();

        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@example.com";
        Integer randomNumber = RANDOM.nextInt(1,100);
        Gender gender = randomNumber % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        EmployeeRegistrationRequest request = new EmployeeRegistrationRequest(
                name, email, gender
        );

        // send a post request
        webTestClient.post()
                .uri(EMPLOYEE_API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), EmployeeRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all employees
        List<EmployeeDTO> allEmployees = webTestClient.get()
                .uri(EMPLOYEE_API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<EmployeeDTO>() {
                })
                .returnResult()
                .getResponseBody();

        // make sure that employee is present
        var id = allEmployees.stream()
                .filter(c -> c.email().equals(email))
                .map(EmployeeDTO::id)
                .findFirst()
                .orElseThrow();

        // update employee
        String newName = "Alexander";

        EmployeeRegistrationRequest updateRequest = new EmployeeRegistrationRequest(
                newName, null, null
        );

        webTestClient.put()
                .uri(EMPLOYEE_API_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), EmployeeDTO.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get employee by id
        EmployeeDTO updatedEmployee = webTestClient.get()
                .uri(EMPLOYEE_API_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(EmployeeDTO.class)
                .returnResult()
                .getResponseBody();

        EmployeeDTO expected = new EmployeeDTO(
                id, newName, email, gender
        );

        assertThat(updatedEmployee).usingRecursiveComparison().isEqualTo(expected);
    }
}
