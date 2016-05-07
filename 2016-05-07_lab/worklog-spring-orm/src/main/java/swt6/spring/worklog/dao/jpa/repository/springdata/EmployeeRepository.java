package swt6.spring.worklog.dao.jpa.repository.springdata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import swt6.spring.worklog.domain.Employee;

import java.util.Date;
import java.util.List;

/**
 * Created by Thomas on 5/7/2016.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Add additional queries here
    Employee getByLastName(String lastName);

    @Query("SELECT e FROM Employee e WHERE e.lastName like %:substr%")
    List<Employee> findByLastNameContaining(@Param("substr") String lastName);

    @Query("SELECT e FROM Employee e WHERE e.dateOfBirth < :date")
    List<Employee> findOlderThen(@Param("date") Date dateOfBirth);

}
