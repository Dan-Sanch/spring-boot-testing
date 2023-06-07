package net.javaguides.springboot.controller;

import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.saveEmployee(employee);
    }

    @GetMapping
    public List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @GetMapping("{id}")
    // NOTE: return type must be ResponseEntity so that we can control the response status
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") long id){
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        return employee
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build()) // NOTE: orElseGet gets passed a callback that runs only of Optional is empty
//                .orElse(ResponseEntity.notFound().build())        // method orElse() gets passed a value, which is evaluated every time
                ;
    }

    @PutMapping("{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") long employeeId,
                                                   @RequestBody Employee employeeObject) {
        return employeeService.getEmployeeById(employeeId)
                .map(savedEmployee -> {
                    // NOTE: the update below would overwrite every provided field to the DB record, including fields
                    // we wouldn't want to overwrite (like the ID). Ideally, we should check for that.
                    Employee updatedEmployee = employeeService.updateEmployee(employeeObject);
                    return ResponseEntity.ok(updatedEmployee);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
