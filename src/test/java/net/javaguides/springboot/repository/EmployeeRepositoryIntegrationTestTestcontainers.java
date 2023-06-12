package net.javaguides.springboot.repository;

import net.javaguides.springboot.integration.BaseTestAbstraction;
import net.javaguides.springboot.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
// NOTE: the annotation below disables the in-memory h2 DB, which makes way for the application's configured MySQL DB
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryIntegrationTestTestcontainers extends BaseTestAbstraction {
    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    private void buildEmployee() {
        employeeRepository.deleteAll();
        employee = Employee.builder()
                .firstName("Dan")
                .lastName("Sanchez")
                .email("dan@domain.com")
                .build();
    }

    // Test for save employee annotation
    @DisplayName("Save employee test")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        // given - precondition or setup

        // when - action or the behavior that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @DisplayName("Get all employees test")
    @Test
    public void givenEmployeesList_whenFindAll_thenReturnAllEmployees() {
        // given - precondition or setup
        Employee employee2 = Employee.builder()
                .firstName("John")
                .lastName("Cena")
                .email("john@domain.com")
                .build();
        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        // when - action or the behavior that we are going to test
        List<Employee> employees = employeeRepository.findAll();

        // then - verify the output
        assertThat(employees).isNotNull();
        assertThat(employees.size()).isEqualTo(2);
        assertThat(employees).contains(employee, employee2);
    }

    @DisplayName("Get employee by ID")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behavior that we are going to test
        Employee foundEmployee = employeeRepository.findById(employee.getId()).get();

        // then - verify the output
        assertThat(foundEmployee).isNotNull();
        assertThat(foundEmployee).isEqualTo(employee);
    }

    @DisplayName("Get employee by email")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behavior that we are going to test
        Employee foundEmployee = employeeRepository.findByEmail(employee.getEmail()).get();

        // then - verify the output
        assertThat(foundEmployee).isNotNull();
        assertThat(foundEmployee).isEqualTo(employee);
    }

    @DisplayName("Update Employee Name")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behavior that we are going to test
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setFirstName("NewDan");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        // then - verify the output
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getId()).isEqualTo(employee.getId());
        assertThat(updatedEmployee.getFirstName()).isEqualTo("NewDan");
    }

    @DisplayName("Delete Employee")
    @Test
    public void givenEmployeeObject_whenDeleteEmployee_thenRemoveEmployee() {
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behavior that we are going to test
        employeeRepository.delete(employee);
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        // then - verify the output
        // Note: these two statements are equivalent
        assertThat(employeeOptional.isPresent()).isFalse();
        assertThat(employeeOptional).isEmpty();
    }

    @DisplayName("Delete Employee by ID")
    @Test
    public void givenEmployeeObject_whenDeleteEmployeeById_thenRemoveEmployee() {
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behavior that we are going to test
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        // then - verify the output
        // Note: these two statements are equivalent
        assertThat(employeeOptional.isPresent()).isFalse();
        assertThat(employeeOptional).isEmpty();
    }

    @DisplayName("Get Employee by custom JPQL query")
    @Test
    public void givenEmployeeObject_whenFindByJpql_thenReturnEmployee() {
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behavior that we are going to test
        Employee returnEmployee = employeeRepository.findByJpql("Dan", "Sanchez");

        // then - verify the output
        // Note: these two statements are equivalent
        assertThat(returnEmployee).isNotNull();
        assertThat(returnEmployee).isEqualTo(employee);
    }

    @DisplayName("Get Employee by custom JPQL query with named parameters")
    @Test
    public void givenEmployeeObject_whenFindByJpqlNamedParams_thenReturnEmployee() {
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behavior that we are going to test
        Employee returnEmployee = employeeRepository.findByJpqlNamedParams("Dan", "Sanchez");

        // then - verify the output
        // Note: these two statements are equivalent
        assertThat(returnEmployee).isNotNull();
        assertThat(returnEmployee).isEqualTo(employee);
    }

    @DisplayName("Get Employee by custom SQL query with indexed parameters")
    @Test
    public void givenEmployeeObject_whenFindBySqlIndexedParams_thenReturnEmployee() {
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behavior that we are going to test
        Employee returnEmployee = employeeRepository.findByNativeSql("Dan", "Sanchez");

        // then - verify the output
        // Note: these two statements are equivalent
        assertThat(returnEmployee).isNotNull();
        assertThat(returnEmployee).isEqualTo(employee);
    }

    @DisplayName("Get Employee by custom SQL query with named parameters")
    @Test
    public void givenEmployeeObject_whenFindBySqlNamedParams_thenReturnEmployee() {
        // given - precondition or setup
        employeeRepository.save(employee);

        // when - action or the behavior that we are going to test
        Employee returnEmployee = employeeRepository.findByNativeSqlNamedParams("Dan", "Sanchez");

        // then - verify the output
        // Note: these two statements are equivalent
        assertThat(returnEmployee).isNotNull();
        assertThat(returnEmployee).isEqualTo(employee);
    }
}
