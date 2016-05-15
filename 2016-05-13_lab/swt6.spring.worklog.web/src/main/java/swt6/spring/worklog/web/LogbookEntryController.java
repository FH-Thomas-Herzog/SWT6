package swt6.spring.worklog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import swt6.spring.worklog.domain.Employee;
import swt6.spring.worklog.domain.LogbookEntry;
import swt6.spring.worklog.logic.WorkLogFacade;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class LogbookEntryController {

    private final Logger logger = LoggerFactory.getLogger(LogbookEntryController.class);

    @Autowired
    private WorkLogFacade workLog;

    private Date addHours(Date date, int hours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hours);
        return cal.getTime();
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    @RequestMapping("/employees/{employeeId}/entries")
    public String list(@PathVariable("employeeId") long employeeId, Model model) {

        Employee empl = this.workLog.findEmployeeById(employeeId);

        // This statement is not necessary if the filter OpenSessionInViewFilter
        // is active (see web.xml). This filter keeps the session open during
        // the whole request. This way lazy loading is always possible.

        // this.workLog.loadLogbookEntriesOfEmployee(empl);

        logger.debug("details view for employee {}", empl);

        List<LogbookEntry> entries =
                new ArrayList<LogbookEntry>(empl.getLogbookEntries());
        Collections.sort(entries,
                         (e1, e2) -> e1.getStartTime().compareTo(e2.getStartTime()));

        model.addAttribute("employee", empl);
        model.addAttribute("logbookEntries", entries);

        return "entryList";
    }

    private String internalProcessUpdate(Long employeeId, LogbookEntry entry, BindingResult result) {
        if (result.hasErrors())
            return "entry";
        else {
            entry.setEmployee(workLog.findEmployeeById(employeeId));
            entry = workLog.syncLogbookEntry(entry);

            logger.debug("added/updated entry {}", entry);
            return "redirect:/employees/{employeeId}/entries";
        }
    }

    @RequestMapping(value = "/employees/{employeeId}/entries/new", method = RequestMethod.GET)
    public String initNew(@PathVariable("employeeId") Long employeeId, Model model) {

        Date now = new Date();
        LogbookEntry entry = new LogbookEntry("", addHours(now, -1), now);

        Employee empl = this.workLog.findEmployeeById(employeeId);
        entry.setEmployee(empl);
        model.addAttribute("entry", entry);

        logger.debug("entry template {}", entry);
        return "entry";
    }

    @RequestMapping(value = "/employees/{employeeId}/entries/new",
            method = RequestMethod.POST)
    public String processNew(@PathVariable("employeeId") Long employeeId,
                             @ModelAttribute("entry") LogbookEntry entry, BindingResult result, Model model) {

        return internalProcessUpdate(employeeId, entry, result);
    }

    @RequestMapping(value = "/employees/{employeeId}/entries/{entryId}/update",
            method = RequestMethod.GET)
    public String initUpdate(@PathVariable("employeeId") Long employeeId,
                             @PathVariable(value = "entryId") Long entryId, Model model) {

        LogbookEntry entry = this.workLog.findLogbookEntryById(entryId);
        model.addAttribute("entry", entry);

        logger.debug("entry template {}", entry);
        return "entry";
    }

    @RequestMapping(value = "/employees/{employeeId}/entries/{entryId}/update",
            method = RequestMethod.POST)
    public String processUpdate(@PathVariable("employeeId") Long employeeId,
                                @ModelAttribute("entry") LogbookEntry entry, BindingResult result) {

        return internalProcessUpdate(employeeId, entry, result);
    }

    @RequestMapping("/employees/{employeeId}/entries/{entryId}/delete")
    public String delete(@PathVariable("employeeId") long employeeId,
                         @PathVariable("entryId") long entryId) {

        this.workLog.deleteLogbookEntryById(entryId);
        logger.debug("deleted entry {}", entryId);

        return "redirect:/employees/{employeeId}/entries";
    }

}
