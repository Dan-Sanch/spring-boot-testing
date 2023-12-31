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
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
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

    @Test
    @DisplayName("getAllEmployees test")
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeeList() {
        // Given
        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("Tony")
                .lastName("Stark")
                .email("tony@domain.com")
                .build();
        // Method stubbing:
        given(employeeRepository.findAll())
                .willReturn(List.of(employee, employee2))
        ;

        // When
        List<Employee> employeeList = employeeService.getAllEmployees();

        // Then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
        assertThat(employeeList).contains(employee, employee2);
    }

    @Test
    @DisplayName("getAllEmployees empty list test")
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeeList() {
        // Given
        // Method stubbing:
        given(employeeRepository.findAll())
                .willReturn(Collections.emptyList())
        ;

        // When
        List<Employee> employeeList = employeeService.getAllEmployees();

        // Then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isEmpty();
    }

    @Test
    @DisplayName("getEmployee by ID test")
    public void givenEmployee_whenGetEmployeeById_thenReturnEmployee() {
        // Given
        // Method stubbing:
        given(employeeRepository.findById(employee.getId()))
                .willReturn(Optional.of(employee));
        ;

        // When
        Optional<Employee> optionalEmployee = employeeService.getEmployeeById(employee.getId());

        // Then
        assertThat(optionalEmployee).isNotNull();
        assertThat(optionalEmployee).isPresent();
        assertThat(optionalEmployee.get().getId()).isEqualTo(employee.getId());
    }

    @Test
    @DisplayName("updateEmployee test")
    public void givenEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // Given
        // Method stubbing:
        given(employeeRepository.save(employee))
                .willReturn(employee)
        ;
        employee.setFirstName("DanUpdate");
        employee.setLastName("SanchezUpdate");


        // When
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        // Then
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getId()).isEqualTo(employee.getId());
        assertThat(updatedEmployee.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(updatedEmployee.getLastName()).isEqualTo(employee.getLastName());
    }

    @Test
    @DisplayName("deleteEmployee test")
    public void givenEmployee_whenDeleteEmployee_thenNothing() {
        // Given
        long employeeId = employee.getId();
        // Method stubbing:
        willDoNothing()
                .given(employeeRepository).deleteById(employeeId)
        ;
        // When
        employeeService.deleteEmployee(employeeId);
        // Then
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }
}
