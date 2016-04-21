package at.fh.ooe.swt6.worklog.manager.service.api;

import at.fh.ooe.swt6.worklog.manager.model.Employee;
import at.fh.ooe.swt6.worklog.manager.model.Module;
import at.fh.ooe.swt6.worklog.manager.model.Project;

import java.util.List;

/**
 * This interface specifies the worklog manager service operations the caller is allowed to use.
 * <p>
 * Created by Thomas on 4/17/2016.
 */
public interface WorklogManagerService extends Closable {
    /**
     * Creates a project.
     *
     * @param name      the name of the project
     * @param leader    the leader of the project
     * @param modules   the modules of the project
     * @param employees the employees assigned to this project, may be null
     * @return the created project.
     * @throws NullPointerException if one argument except employees is null
     */
    Project createProject(String name,
                          Employee leader,
                          List<Module> modules,
                          List<? extends Employee> employees);

    /**
     * Assigns the employees to the projects.
     *
     * @param employees the employees meant to be assigned to the projects
     * @param projects  the projects meant to be assigned to the employees
     * @throws NullPointerException if one argument is null
     */
    void assignEmployeesToProjects(List<? extends Employee> employees,
                                   List<Project> projects);

    /**
     * Removes the given employees from the project.
     *
     * @param employees the employees meant to be assigned to the projects
     * @param projects  the projects meant to be assigned to the employees
     * @throws NullPointerException if a argument is null
     */
    void removeEmployeesFromProject(List<? extends Employee> employees,
                                    List<Project> projects);

    /**
     * Removes the logoobk entries from teh database.
     *
     * @param ids the employee ids to be removed.
     * @return the count of removed logbook-entries.
     */
    int removeLogbookEntriesForEmployee(List<Long> ids);
    /**
     * Removes the logbook-entries from a project from the database.
     *
     * @param ids the employee ids to be removed.
     * @return the count of removed logbook-entries.
     */
    int removeProjects(List<Long> ids);
}
