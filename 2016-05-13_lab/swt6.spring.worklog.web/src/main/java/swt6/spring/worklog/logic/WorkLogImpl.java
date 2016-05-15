package swt6.spring.worklog.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import swt6.spring.worklog.dao.EmployeeRepository;
import swt6.spring.worklog.dao.LogbookEntryRepository;
import swt6.spring.worklog.domain.Employee;
import swt6.spring.worklog.domain.LogbookEntry;

@Component()
@Transactional
public class WorkLogImpl implements WorkLogFacade {
  
  @Autowired
  private EmployeeRepository      employeeRepo;
  
  @Autowired
  private LogbookEntryRepository logbookEntryRepo;
  
  public WorkLogImpl() {
  }

  //=======================  Setters =======================

  public void setEmployeeRepo(EmployeeRepository employeeRepo) {
    this.employeeRepo = employeeRepo;
  }

  public void setLogbookEntryRepo(LogbookEntryRepository logbookEntryRepo) {
    this.logbookEntryRepo = logbookEntryRepo;
  }
  
  //================ Business logic method for Employee ================

  @Transactional(readOnly = true)
  public Employee findEmployeeById(Long id) {
    return employeeRepo.findOne(id);
  }

  @Transactional(readOnly = true)
  public List<Employee> findAllEmployees() {
    return employeeRepo.findAll();
  }

  public Employee syncEmployee(Employee employee) {
    return employeeRepo.saveAndFlush(employee);
  }

  // ============== Business logic method for LogbookEntry ================
  public LogbookEntry syncLogbookEntry(LogbookEntry entry) {
    return logbookEntryRepo.saveAndFlush(entry);
  }

  @Transactional(readOnly = true)
  public LogbookEntry findLogbookEntryById(Long id) {
    return logbookEntryRepo.findOne(id);
  }

  public void deleteLogbookEntryById(Long id) {
    LogbookEntry entry = logbookEntryRepo.findOne(id);
    entry.detachEmployee();
    logbookEntryRepo.delete(entry);
  }
}
