package at.fh.ooe.swt6.worklog.manager.service.api;

import at.fh.ooe.swt6.worklog.manager.model.*;

import java.util.List;

/**
 * Created by Thomas on 4/17/2016.
 */
public interface WorklogManagerDataAccess {

    /**
     * Gets all projects and related entities.
     *
     * @return the found projects
     */
    List<Project> getAllProjects();

    /**
     * Gets all permanent employees.
     *
     * @return the found permanent employees
     */
    List<PermanentEmployee> getAllPermanentEmployees();

    /**
     * Gets all temporary employees.
     *
     * @return teh found temporary employees
     */
    List<TemporaryEmployee> getAllTemporaryEmployees();

    /**
     * Gets the logbook entries for the employee with the given id
     *
     * @param id the employee id
     * @return the found logbook entries
     */
    List<LogBookEntry> getLogbookEntriesForEmployee(long id);

    /**
     * Gets the logbook entries for the project with the given id
     *
     * @param id the project id
     * @return the found logbook entries
     */
    List<LogBookEntry> getLogbookEntriesForProject(long id);
}
