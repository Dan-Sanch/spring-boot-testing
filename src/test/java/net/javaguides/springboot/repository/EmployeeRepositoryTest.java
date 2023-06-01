package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Employee;
import static  org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository employeeRepository;

    // Test for save employee annotation
    @DisplayName("Save employee test")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Dan")
                .lastName("Sanchez")
                .email("dan@domain.com")
                .build();

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
        Employee employee1 = Employee.builder()
                .firstName("Dan")
                .lastName("Sanchez")
                .email("dan@domain.com")
                .build();
        Employee employee2 = Employee.builder()
                .firstName("John")
                .lastName("Cena")
                .email("john@domain.com")
                .build();
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        // when - action or the behavior that we are going to test
        List<Employee> employees = employeeRepository.findAll();

        // then - verify the output
        assertThat(employees).isNotNull();
        assertThat(employees.size()).isEqualTo(2);
        assertThat(employees).contains(employee1, employee2);
    }

    @DisplayName("Get employee by ID")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
        // given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("Dan")
                .lastName("Sanchez")
                .email("dan@domain.com")
                .build();
        employeeRepository.save(employee1);

        // when - action or the behavior that we are going to test
        Employee foundEmployee = employeeRepository.findById(employee1.getId()).get();

        // then - verify the output
        assertThat(foundEmployee).isNotNull();
        assertThat(foundEmployee).isEqualTo(employee1);
    }
}
