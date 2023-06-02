package net.javaguides.springboot.service;

import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.impl.EmployeeServiceImpl;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.util.Optional;

public class EmployeeServiceTests {

    private EmployeeRepository employeeRepository;
    private EmployeeService employeeService;

    @BeforeEach
    public void setup() {
        employeeRepository = Mockito.mock(EmployeeRepository.class);
        employeeService = new EmployeeServiceImpl(employeeRepository);
    }

    private Employee buildBaseEmployee() {
        return Employee.builder()
                .firstName("Dan")
                .lastName("Sanchez")
                .email("dan@domain.com")
                .build();
    }

    @Test
    @DisplayName("saveEmployee test")
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployee() {
        // Given
        Employee employee = buildBaseEmployee();
        // Method stubbing:
        BDDMockito
                .given( // establish the method call to mock, with the given arguments
                    employeeRepository.findByEmail(employee.getEmail()))
                .willReturn( // what the mocked method will return
                        Optional.empty())
        ;
        BDDMockito
                .given(employeeRepository.save(employee))
                .willReturn(employee)
                // Note: the returned employee doesn't get assigned an ID
        ;

        // When
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // Then
        assertThat(savedEmployee).isNotNull();
    }
}
