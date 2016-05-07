package swt6.spring.worklog.logic;

import swt6.spring.worklog.dao.EmployeeDao;
import swt6.spring.worklog.dao.LogbookEntryDao;
import swt6.spring.worklog.domain.Employee;
import swt6.spring.worklog.domain.LogbookEntry;

import java.util.List;

public class WorkLogImpl1 implements WorkLogFacade {
  private EmployeeDao employeeDao;
  private LogbookEntryDao logbookEntryDao;

  public WorkLogImpl1() {
  }

  // ======================= DAO setters =======================
  public void setEmployeeDao(EmployeeDao employeeDao) {
    this.employeeDao = employeeDao;
  }

  public void setLogbookEntryDao(LogbookEntryDao logbookEntryDao) {
    this.logbookEntryDao = logbookEntryDao;
  }

  // ================ Business logic method for Employee ================

  @Override
  public Employee findEmployeeById(Long id) {
    return employeeDao.findById(id);
  }

  @Override
  public List<Employee> findAllEmployees() {
    return employeeDao.findAll();
  }

  @Override
  public Employee syncEmployee(Employee employee) {
    return employeeDao.merge(employee);
  }

  // ============== Business logic method for LogbookEntry ================
  @Override
  public LogbookEntry syncLogbookEntry(LogbookEntry entry) {
    return logbookEntryDao.merge(entry);
  }

  @Override
  public LogbookEntry findLogbookEntryById(Long id) {
    return logbookEntryDao.findById(id);
  }

  @Override
  public void deleteLogbookEntryById(Long id) {
    logbookEntryDao.deleteById(id);
  }
}
