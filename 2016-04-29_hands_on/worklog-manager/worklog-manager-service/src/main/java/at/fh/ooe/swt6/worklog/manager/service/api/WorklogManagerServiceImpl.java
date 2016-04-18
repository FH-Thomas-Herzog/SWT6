package at.fh.ooe.swt6.worklog.manager.service.api;

import at.fh.ooe.swt6.worklog.manager.model.*;

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

        dataManager.startTx();

        final Employee leaderDB;
        if ((leaderDB=dataManager.find(leader.getId(), Employee.class)) == null) {
            throw new IllegalArgumentException("Project leader does not exists");
        }

        // persist project
        final Project project = dataManager.persist(new Project(name, leaderDB));
        // set project on modules
        modules.forEach(item -> item.setProject(project));
        // set modules on project
        project.getModules()
               .addAll(dataManager.batchPersist(modules));
        // set employees if present
        if ((employees != null) && (!employees.isEmpty())) {
            // create project employees
            final List<ProjectEmployee> projectEmployees = employees.stream()
                                                                    .map(item -> new ProjectEmployee(new ProjectEmployeeId(
                                                                            project.getId(),
                                                                            item.getId())))
                                                                    .collect(
                                                                            Collectors.toList());
            // set project employees on project
            project.getProjectEmployees()
                   .addAll(dataManager.batchPersist(projectEmployees));
        }

        dataManager.commit();

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
