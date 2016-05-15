package swt6.spring.worklog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import swt6.spring.worklog.domain.Employee;
import swt6.spring.worklog.logic.WorkLogFacade;

import java.util.List;

/**
 * Created by Thomas on 5/13/2016.
 */
@Controller
public class EmployeeController {

    private final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private WorkLogFacade worklogFacade;

    @RequestMapping("/employees")
    public String list(Model model) {

        final List<Employee> employees = worklogFacade.findAllEmployees();

        model.addAttribute("employees", employees);
        log.debug("Employee count {}", employees.size());

        return "employeeList";
    }

}
