package at.fh.ooe.swt6.worklog.manager.service.api;

import at.fh.ooe.swt6.worklog.manager.model.Employee;
import at.fh.ooe.swt6.worklog.manager.model.Module;
import at.fh.ooe.swt6.worklog.manager.model.Project;

import javax.persistence.PersistenceException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

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
            if ((leaderDB = dataManager.find(leader.getId(), Employee.class)) == null) {
                throw new IllegalArgumentException("Project leader does not exists");
            }

            // persist project
            project = dataManager.persist(new Project(name, leaderDB));
            // set project on modules
            modules.forEach(item -> item.setProject(project));
            // set modules on project
            project.getModules()
                   .addAll(dataManager.batchPersist(modules));
            // set employees if present
            if ((employees != null) && (!employees.isEmpty())) {
                project.getProjectEmployees().addAll(new HashSet<>(dataManager.batchMerge(employees)));
                project.getProjectEmployees().forEach(item -> item.getEmployeeProjects().add(project));
            }

            dataManager.commit();
        } catch (IllegalArgumentException e) {
            dataManager.rollback();
            throw new PersistenceException("Project creation failed");
        } finally {
            dataManager.clear();
        }

        return project;
    }

    @Override
    public void assignEmployeesToProjects(List<? extends Employee> employees,
                                          List<Project> projects) {

    }

    @Override
    public void removeEmployeesFromProject(List<Integer> employeeIds,
                                           int projectId) {

    }

    @Override
    public void close() {
        dataManager.close();
    }
}
