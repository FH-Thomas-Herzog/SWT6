package swt6.spring.worklog.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import swt6.spring.worklog.dao.EmployeeDao;
import swt6.spring.worklog.dao.LogbookEntryDao;
import swt6.spring.worklog.domain.Employee;
import swt6.spring.worklog.domain.LogbookEntry;

import java.util.List;

@Component("workLog")
@Transactional
public class WorkLogImpl2 implements WorkLogFacade {

    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private LogbookEntryDao logbookEntryDao;

    public WorkLogImpl2() {
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
    @Transactional(readOnly = true)
    public Employee findEmployeeById(Long id) {
        return employeeDao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public LogbookEntry findLogbookEntryById(Long id) {
        return logbookEntryDao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public void deleteLogbookEntryById(Long id) {
        logbookEntryDao.deleteById(id);
    }
}
