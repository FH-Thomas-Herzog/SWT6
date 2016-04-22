package at.fh.ooe.swt6.worklog.manager.service.api;

import at.fh.ooe.swt6.worklog.manager.model.Employee;
import at.fh.ooe.swt6.worklog.manager.model.Module;
import at.fh.ooe.swt6.worklog.manager.model.Project;

import javax.persistence.PersistenceException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Thomas on 4/18/2016.
 */
public class WorklogManagerServiceImpl implements WorklogManagerService {

    private DataManager dataManager;

    public WorklogManagerServiceImpl(DataManager dataManager) {
        Objects.requireNonNull(dataManager, "DataManager must not be null");
        this.dataManager = dataManager;
    }

    @Override
    public Project createProject(String name,
                                 Employee leader,
                                 List<Module> modules,
                                 List<? extends Employee> employees) {
        Objects.requireNonNull(name, "A project must have a name");
        Objects.requireNonNull(leader, "A project needs a leader");
        Objects.requireNonNull(name, "A project needs at leas one module");
        if (modules.isEmpty()) {
            throw new IllegalArgumentException("project needs at leas one module");
        }

        final Project project;
        try {
            dataManager.startTx();

            final Employee leaderDB;
            // Check if leader exists
            if ((leaderDB = dataManager.loadForClassAndIds(leader.getId(), Employee.class)) == null) {
                throw new IllegalArgumentException("Project leader does not exists");
            }

            // persist project
            project = dataManager.persist(new Project(name, leaderDB));
            leaderDB.getLeadingProjects().add(project);
            // set project on modules
            modules.forEach(item -> item.setProject(project));
            // set modules on project
            project.getModules()
                   .addAll(dataManager.batchPersist(modules));
            // set employees if present
            if ((employees != null) && (!employees.isEmpty())) {
                project.getProjectEmployees().addAll(employees);
                employees.forEach(item -> item.getEmployeeProjects().add(project));
            }

            dataManager.commit();
        } catch (IllegalArgumentException e) {
            dataManager.rollback();
            throw new PersistenceException("Project creation failed");
        }

        return project;
    }

    @Override
    public void assignEmployeesToProjects(List<? extends Employee> employees,
                                          List<Project> projects) {
        try {
            dataManager.startTx();

            // load all projects from database by their id
            final List<Project> projectsDB = dataManager.loadForClassAndIds(projects.stream()
                                                                                    .map(item -> item.getId())
                                                                                    .filter(item -> item != null)
                                                                                    .collect(Collectors.toList()),
                                                                            Project.class);
            final List<? extends Employee> employeesDB = dataManager.loadForClassAndIds(projects.stream()
                                                                                                .map(item -> item.getId())
                                                                                                .filter(item -> item != null)
                                                                                                .collect(Collectors.toList()),
                                                                                        Employee.class);
            // assign to each project
            for (Project project : projectsDB) {
                // set on project
                project.getProjectEmployees().addAll(employees);
                // merge project
                dataManager.merge(project);
                // set on employee
                employeesDB.forEach(item -> item.getEmployeeProjects().add(project));
            }
            dataManager.commit();
        } catch (Exception e) {
            dataManager.rollback();
            throw new PersistenceException("Could not assign employees to projects", e);
        }
    }

    @Override
    public void removeEmployeesFromProject(List<? extends Employee> employees,
                                           List<Project> projects) {
        try {
            dataManager.startTx();

            // loading projects by their id
            final List<Project> projectsDB = dataManager.loadForClassAndIds(projects.stream()
                                                                                    .map(item -> item.getId())
                                                                                    .filter(item -> item != null)
                                                                                    .collect(Collectors.toList()),
                                                                            Project.class);
            final List<? extends Employee> employeesDB = dataManager.loadForClassAndIds(projects.stream()
                                                                                                .map(item -> item.getId())
                                                                                                .filter(item -> item != null)
                                                                                                .collect(Collectors.toList()),
                                                                                        Employee.class);
            // remove from each project
            for (Project project : projectsDB) {
                // remove from project
                project.getProjectEmployees().removeAll(employees);
                dataManager.merge(project);
                // remove from employee projects
                employeesDB.forEach(item -> item.getEmployeeProjects().remove(project));
            }
            dataManager.commit();
        } catch (Exception e) {
            dataManager.rollback();
            throw new PersistenceException("Could not remove the employees from the projects", e);
        }
    }

    @Override
    public int removeLogbookEntriesForEmployee(List<Long> ids) {
        Objects.requireNonNull(ids, "Cannot remove logbook-entries for null employee ids");
        int result = 0;

        // only if ids are present
        if (!ids.isEmpty()) {
            try {
                dataManager.startTx();

                // one shot delete of the logbook-entries
                result = dataManager.executeQuery(
                        "DELETE FROM LogBookEntry entry WHERE entry.employee.id in (:ids)",
                        new HashMap() {{
                            put("ids", ids);
                        }});
                dataManager.commit();
            } catch (Exception e) {
                dataManager.rollback();
                throw new PersistenceException("Could not delete logbook-entries for employees", e);
            }
        }

        return result;
    }

    @Override
    public int removeProjects(List<Long> ids) {
        Objects.requireNonNull(ids, "Cannot remove projects for null ids");

        int result = 0;

        // only if ids are present
        if (!ids.isEmpty()) {
            try {
                dataManager.startTx();

                // one shot delete related logbook-entries (related via module)
                dataManager.executeQuery(
                        "DELETE FROM LogBookEntry entry WHERE entry.id IN ("
                                + " SELECT entry.id FROM LogBookEntry entry "
                                + " WHERE entry.module.project.id IN (:ids) )",
                        new HashMap() {{
                            put("ids", ids);
                        }});

                // one shot delete related modules
                dataManager.executeQuery(
                        "DELETE FROM Module module WHERE module.project.id in (:ids)",
                        new HashMap() {{
                            put("ids", ids);
                        }});

                // one shot delete projects (projectEmployees done by persistence provider)
                result = dataManager.executeQuery(
                        "DELETE FROM Project project WHERE project.id in (:ids)",
                        new HashMap() {{
                            put("ids", ids);
                        }});
                dataManager.commit();
            } catch (Exception e) {
                dataManager.rollback();
                throw new PersistenceException("Could not remove projects", e);
            }
        }

        return result;
    }

    @Override
    public void close() {
        dataManager.close();
    }
}
