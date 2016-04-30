package swt6.spring.basics.ioc.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swt6.spring.basics.ioc.domain.Employee;
import swt6.spring.basics.ioc.util.Log;
import swt6.spring.basics.ioc.util.Logger;
import swt6.spring.basics.ioc.util.LoggerType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Thomas on 4/30/2016.
 */
@Component("worklog")
public class WorkLogImplIocAnnotationBased implements WorkLogFacade {
    private Map<Long, Employee> employees = new HashMap<Long, Employee>();

    @Log
    private Logger logger;

    @Log(type = LoggerType.ERROR)
    private Logger errorLogger;

    private void init() {
        employees.put(1L, new Employee(1L, "Bill", "Gates"));
        employees.put(2L, new Employee(2L, "James", "Goslin"));
        employees.put(3L, new Employee(3L, "Bjarne", "Stroustrup"));
    }

    public WorkLogImplIocAnnotationBased() {
        init();
    }

    public WorkLogImplIocAnnotationBased(Logger logger) {
        init();
        this.logger = logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Employee findEmployeeById(Long id) {
        Employee empl = employees.get(id);
        logger.log("findEmployeeById(" + id + ") --> " + ((empl != null) ? empl.toString() : "<null>"));
        if (empl == null)
            errorLogger.log("invalid employee " + id);
        return empl;
    }

    public List<Employee> findAllEmployees() {
        logger.log("findAllEmployees()");
        logger.log("findAllEmployees()");

        return new ArrayList<>(employees.values());
    }
}
