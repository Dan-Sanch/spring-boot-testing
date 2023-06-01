package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Employee;
import static  org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

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

    @DisplayName("Get employee by email")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {
        // given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("Dan")
                .lastName("Sanchez")
                .email("dan@domain.com")
                .build();
        employeeRepository.save(employee1);

        // when - action or the behavior that we are going to test
        Employee foundEmployee = employeeRepository.findByEmail(employee1.getEmail()).get();

        // then - verify the output
        assertThat(foundEmployee).isNotNull();
        assertThat(foundEmployee).isEqualTo(employee1);
    }

    @DisplayName("Update Employee Name")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("Dan")
                .lastName("Sanchez")
                .email("dan@domain.com")
                .build();
        employeeRepository.save(employee1);

        // when - action or the behavior that we are going to test
        Employee savedEmployee = employeeRepository.findById(employee1.getId()).get();
        savedEmployee.setFirstName("NewDan");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        // then - verify the output
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getId()).isEqualTo(employee1.getId());
        assertThat(updatedEmployee.getFirstName()).isEqualTo("NewDan");
    }

    @DisplayName("Delete Employee")
    @Test
    public void givenEmployeeObject_whenDeleteEmployee_thenRemoveEmployee() {
        // given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("Dan")
                .lastName("Sanchez")
                .email("dan@domain.com")
                .build();
        employeeRepository.save(employee1);

        // when - action or the behavior that we are going to test
        employeeRepository.delete(employee1);
        Optional<Employee> employeeOptional = employeeRepository.findById(employee1.getId());

        // then - verify the output
        // Note: these two statements are equivalent
        assertThat(employeeOptional.isPresent()).isFalse();
        assertThat(employeeOptional).isEmpty();
    }

    @DisplayName("Delete Employee by ID")
    @Test
    public void givenEmployeeObject_whenDeleteEmployeeById_thenRemoveEmployee() {
        // given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("Dan")
                .lastName("Sanchez")
                .email("dan@domain.com")
                .build();
        employeeRepository.save(employee1);

        // when - action or the behavior that we are going to test
        employeeRepository.deleteById(employee1.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee1.getId());

        // then - verify the output
        // Note: these two statements are equivalent
        assertThat(employeeOptional.isPresent()).isFalse();
        assertThat(employeeOptional).isEmpty();
    }
}
