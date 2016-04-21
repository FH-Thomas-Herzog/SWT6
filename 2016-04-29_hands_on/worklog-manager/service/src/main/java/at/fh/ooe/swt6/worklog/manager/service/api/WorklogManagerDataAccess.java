package at.fh.ooe.swt6.worklog.manager.service.api;

import at.fh.ooe.swt6.worklog.manager.model.LogBookEntry;
import at.fh.ooe.swt6.worklog.manager.model.PermanentEmployee;
import at.fh.ooe.swt6.worklog.manager.model.Project;
import at.fh.ooe.swt6.worklog.manager.model.TemporaryEmployee;

import java.util.List;

/**
 * Specifies the worklog manager data access.
 * Here all database query access is placed the caller is allowed to use.
 * <p>
 * Created by Thomas on 4/17/2016.
 */
public interface WorklogManagerDataAccess extends Closable {

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
