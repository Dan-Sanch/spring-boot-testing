package net.javaguides.springboot.service;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.impl.EmployeeServiceImpl;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

//@SpringBootTest // NOTE: this annotation would also work, as it also has the @ExtendWith annotation
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    private void buildBaseEmployee() {
        employee = Employee.builder()
                .id(1L)
                .firstName("Dan")
                .lastName("Sanchez")
                .email("dan@domain.com")
                .build();
    }

    @Test
    @DisplayName("saveEmployee test")
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployee() {
        // Given
        // Method stubbing:
        given( // establish the method call to mock, with the given arguments
            employeeRepository.findByEmail(employee.getEmail()))
        .willReturn( // what the mocked method will return
                Optional.empty())
        ;
        given(employeeRepository.save(employee))
        .willReturn(employee) // Note: mocked "save" method doesn't generate an ID
        ;

        // When
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // Then
        assertThat(savedEmployee).isNotNull();
    }

    @Test
    @DisplayName("saveEmployee Exception test")
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException() {
        // Given
        // Method stubbing:
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee))
        ;

        // When
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        // Then
        verify(employeeRepository, never()).save(any(Employee.class));
    }
}
