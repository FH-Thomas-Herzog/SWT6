package at.fh.ooe.swt6.test.worklog.manager.service.jpa;

import at.fh.ooe.swt6.worklog.manager.model.*;
import at.fh.ooe.swt6.worklog.manager.service.jpa.JPADataManager;
import utils.ModelGenerator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Thomas on 4/17/2016.
 */
public class JPAWorklogManager {

    public static void main(String[] args) {
        JPADataManager dataManager = new JPADataManager();
        dataManager.startTx();

        List<Phase> phases = ModelGenerator.createPhases();
        phases.forEach(item -> dataManager.persist(item));

        List<PermanentEmployee> permanentEmployees = ModelGenerator.createPermanatEmployees(10);
        permanentEmployees.forEach(item -> dataManager.persist(item));

        List<TemporaryEmployee> temporaryEmployees = ModelGenerator.createTemporaryEmployees(10);
        temporaryEmployees.forEach(item -> dataManager.persist(item));

        permanentEmployees.forEach(item -> {
            Project project = ModelGenerator.createProject("project_" + item.getFirstName(),
                                                           item,
                                                           temporaryEmployees);
            Set<ProjectHasEmployee> hasEmployees = project.getProjectEmployees();
            project.setProjectEmployees(new HashSet<>());

            project = dataManager.persist(project);
            hasEmployees.forEach(entry -> {
                entry.setId(new ProjectHasEmployeeId(entry.getProject()
                                                          .getId(),
                                                     entry.getEmployee()
                                                          .getId()));
                entry = dataManager.persist(entry);
            });
        });
        dataManager.commit();
    }
}
