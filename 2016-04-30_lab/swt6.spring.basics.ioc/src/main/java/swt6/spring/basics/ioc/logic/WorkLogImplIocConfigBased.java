package swt6.spring.basics.ioc.logic;

import org.springframework.beans.factory.annotation.Autowired;
import swt6.spring.basics.ioc.domain.Employee;
import swt6.spring.basics.ioc.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Thomas on 4/30/2016.
 */
public class WorkLogImplIocConfigBased implements WorkLogFacade {
    private Map<Long, Employee> employees = new HashMap<Long, Employee>();

    private Logger logger;
    private Logger errorLogger;

    private void init() {
        employees.put(1L, new Employee(1L, "Bill", "Gates"));
        employees.put(2L, new Employee(2L, "James", "Goslin"));
        employees.put(3L, new Employee(3L, "Bjarne", "Stroustrup"));
    }

    public WorkLogImplIocConfigBased() {
        init();
    }

    public WorkLogImplIocConfigBased(Logger logger,
                                     Logger errorLogger) {
        init();
        this.logger = logger;
        this.errorLogger = errorLogger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void setErrorLogger(Logger logger) {
        errorLogger = logger;
    }

    @Override
    public Employee findEmployeeById(Long id) {
        Employee empl = employees.get(id);
        logger.log("findEmployeeById(" + id + ") --> " + ((empl != null) ? empl.toString() : "<null>"));
        if (empl == null) {
            errorLogger.log("no employee with id " + id);
        }
        return empl;
    }

    @Override
    public List<Employee> findAllEmployees() {
        logger.log("findAllEmployees()");
        return new ArrayList<>(employees.values());
    }
}
