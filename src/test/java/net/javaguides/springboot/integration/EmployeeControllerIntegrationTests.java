package net.javaguides.springboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        employeeRepository.deleteAll();
    }

    @Test
    @DisplayName("POST create employee test")
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // Given
        Employee employee = Employee.builder()
                .firstName("Dan")
                .lastName("Sanchez")
                .email("dan@domain.com")
                .build();

        // When
        ResultActions response = postCreateEmployee(employee);

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
        Employee employee = Employee.builder()
                .firstName("Dan")
                .lastName("Sanchez")
                .email("dan@domain.com")
                .build();
        Employee employee2 = Employee.builder()
                .firstName("Dan2")
                .lastName("Sanchez2")
                .email("dan2@domain.com")
                .build();
        postCreateEmployee(employee);
        postCreateEmployee(employee2);

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
        Employee employee = Employee.builder()
                .firstName("Dan")
                .lastName("Sanchez")
                .email("dan@domain.com")
                .build();
        employeeRepository.save(employee);

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
        long invalidID = 0L;

        // When
        ResultActions response = mockMvc.perform(
                get("/api/employees/{id}", invalidID)
        );

        // Then
        response
                .andExpect(status().isNotFound())
                .andDo(print())
        ;
    }

    private ResultActions postCreateEmployee(Employee employee) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee))
        );
    }
}
