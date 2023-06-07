package net.javaguides.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class EmployeeControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService employeeService;
    @Autowired
    private ObjectMapper objectMapper;

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
    @DisplayName("POST create employee test")
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // Given
        given(employeeService.saveEmployee(ArgumentMatchers.any()))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // When
        ResultActions response = mockMvc.perform( // <-- throws exception
            MockMvcRequestBuilders.post("/api/employees")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(employee)) // <-- throws exception
        );

        // Then
        response
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail())))
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("GET all employees test")
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnAllEmployees() throws Exception {
        // Given
        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("Dan2")
                .lastName("Sanchez2")
                .email("dan2@domain.com")
                .build();
        given(employeeService.getAllEmployees())
                .willReturn(List.of(employee, employee2));

        // When
        ResultActions response = mockMvc.perform( // <-- throws exception
                get("/api/employees")
        );

        // Then
        response
                .andExpect(status().isOk())   // Test response status
                .andExpect(jsonPath("$.size()", // Test response data size
                        CoreMatchers.is(2)))
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("GET employee by ID")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() throws Exception {
        // Given
        given(employeeService.getEmployeeById(employee.getId()))
                .willReturn(Optional.of(employee));

        // When
        ResultActions response = mockMvc.perform( // <-- throws exception
                get("/api/employees/{id}", employee.getId())
        );

        // Then
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail())))
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("GET invalid employee")
    public void givenBadEmployeeId_whenGetEmployeeById_thenReturnNotFound() throws Exception {
        // Given
        given(employeeService.getEmployeeById(2L))
                .willReturn(Optional.empty());

        // When
        ResultActions response = mockMvc.perform( // <-- throws exception
                get("/api/employees/{id}", 2L)
        );

        // Then
        response
                .andExpect(status().isNotFound())
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("Update employee")
    public void givenUpdatedEmployeeObject_whenUpdateEmployee_thenReturnEmployee() throws Exception {
        // Given
        Employee savedEmployee = employee;
        long employeeId = savedEmployee.getId();

        given(employeeService.getEmployeeById(employeeId))
                .willReturn(Optional.of(savedEmployee));

        Employee updatedEmployee = Employee.builder()
                .id(1L)
                .firstName("DanUpdate")
                .lastName("SanchezUpdate")
                .email("dan_update@domain.com")
                .build();

        given(employeeService.updateEmployee(ArgumentMatchers.any()))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // When
        ResultActions response = mockMvc.perform( // <-- throws exception
                put("/api/employees/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee))
        );

        // Then
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName",
                        CoreMatchers.is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        CoreMatchers.is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email",
                        CoreMatchers.is(updatedEmployee.getEmail())))
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("Update invalid employee")
    public void givenBadEmployeeId_whenUpdateEmployee_thenReturnNotFound() throws Exception {
        // Given
        long employeeId = 2L;
        Employee updatedEmployee = Employee.builder()
                .id(employeeId)
                .firstName("DanUpdate")
                .lastName("SanchezUpdate")
                .email("dan_update@domain.com")
                .build();

        given(employeeService.getEmployeeById(employeeId))
                .willReturn(Optional.empty());

        // When
        ResultActions response = mockMvc.perform( // <-- throws exception
                put("/api/employees/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee))
        );

        // Then
        response
                .andExpect(status().isNotFound())
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("Delete employee")
    public void givenEmployeeObject_whenDeleteEmployee_thenReturnOk() throws Exception {
        // Given
        long employeeId = employee.getId();
        willDoNothing()
                .given(employeeService).deleteEmployee(employeeId);

        // When
        ResultActions response = mockMvc.perform( // <-- throws exception
                delete("/api/employees/{id}", employeeId)
        );

        // Then
        response
                .andExpect(status().isOk())
                .andDo(print())
        ;
    }
}
