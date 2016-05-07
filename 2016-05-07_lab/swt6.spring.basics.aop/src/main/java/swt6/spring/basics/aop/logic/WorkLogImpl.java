package swt6.spring.basics.aop.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component("workLog")
public class WorkLogImpl implements WorkLogFacade {

  private Map<Long, Employee> employees = new HashMap<Long, Employee>();

  private void init() {
    employees.put(1L, new Employee(1L, "Bill", "Gates"));
    employees.put(2L, new Employee(2L, "James", "Goslin"));
    employees.put(3L, new Employee(3L, "Bjarne", "Stroustrup"));
  }

  public WorkLogImpl() {
    init();
  }

  @Override
  public Employee findEmployeeById(Long id) throws EmployeeIdNotFoundException {
    if (employees.get(id) == null) {
      throw new EmployeeIdNotFoundException();
    }
    return employees.get(id);
  }

  @Override
  public List<Employee> findAllEmployees() {
    return new ArrayList<Employee>(employees.values());
  }
}
