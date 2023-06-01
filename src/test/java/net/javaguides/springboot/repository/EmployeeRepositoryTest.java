package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Employee;
import static  org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
}
