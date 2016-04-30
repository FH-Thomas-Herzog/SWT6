package swt6.spring.basics.ioc.logic;

import java.util.List;

import swt6.spring.basics.ioc.domain.Employee;

public interface WorkLogFacade {

  Employee findEmployeeById(Long id);

  List<Employee> findAllEmployees();
}