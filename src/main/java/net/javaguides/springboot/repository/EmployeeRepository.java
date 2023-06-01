package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);

    @Query("select e from Employee e where e.firstName = ?1 and e.lastName = ?2")
    Employee findByJpql(String firstName, String lastName);

    @Query("select e from Employee e where e.firstName = :firstNameParam and e.lastName = :lastNameParam")
    Employee findByJpqlNamedParams(@Param("firstNameParam") String firstName, @Param("lastNameParam") String lastName);
}
