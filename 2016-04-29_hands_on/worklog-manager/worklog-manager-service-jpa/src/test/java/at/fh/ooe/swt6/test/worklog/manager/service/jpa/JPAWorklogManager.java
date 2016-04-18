package at.fh.ooe.swt6.test.worklog.manager.service.jpa;

import at.fh.ooe.swt6.worklog.manager.model.*;
import at.fh.ooe.swt6.worklog.manager.service.api.*;
import at.fh.ooe.swt6.worklog.manager.service.jpa.JPADataManager;
import at.fh.ooe.swt6.worklog.manager.service.jpa.JPAUtils;
import at.fh.ooe.swt6.worklog.manager.testsuite.api.watcher.LoggingTestClassWatcher;
import at.fh.ooe.swt6.worklog.manager.testsuite.api.watcher.LoggingTestInvocationWatcher;
import org.apache.log4j.Level;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import utils.ModelGenerator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Thomas on 4/17/2016.
 */
@RunWith(JUnit4.class)
public class JPAWorklogManager {

    private DataManager dataManager;

    @ClassRule
    public static LoggingTestClassWatcher watcher = new LoggingTestClassWatcher(Level.WARN);

    @Rule
    public LoggingTestInvocationWatcher methodWatcher = new LoggingTestInvocationWatcher(Level.DEBUG);

    WorklogManagerDataAccess dataAccess;
    WorklogManagerService service;

    public JPAWorklogManager() {
        // This will refresh the database
        JPAUtils.createEntityManagerFactory();
    }

    @Before
    public void beforeTest() {
        // Get a fresh entity manager
        dataManager = new JPADataManager(JPAUtils.getEntityManager());
        // create data access
        dataAccess = new WorklogManagerDataAccessImpl(dataManager);
        // create service
        service = new WorklogManagerServiceImpl(dataManager);
        // create data
        creaData();
    }

    @After
    public void afterTest() {
        // close data access/service
        dataAccess.close();
        service.close();
        dataManager.close();
        dataAccess = null;
        service = null;
        dataManager = null;
        // This will refresh the database
        JPAUtils.createEntityManagerFactory();
    }

    @Test
    public void t1() {
        WorklogManagerDataAccess dataAccess = new WorklogManagerDataAccessImpl(dataManager);
        WorklogManagerService service = new WorklogManagerServiceImpl(dataManager);

        List<PermanentEmployee> permanentEmpl = dataAccess.getAllPermanentEmployees();
        List<TemporaryEmployee> temporaryEmployees = dataManager.batchPersist(ModelGenerator.createTemporaryEmployees(10));
        permanentEmpl.forEach(item -> service.createProject("project",
                                                            permanentEmpl.get(0),
                                                            ModelGenerator.createModules(null),
                                                            temporaryEmployees));
    }

    public void creaData() {
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
            Set<ProjectEmployee> hasEmployees = project.getProjectEmployees();
            project.setProjectEmployees(new HashSet<>());

            project = dataManager.persist(project);
            hasEmployees.forEach(entry -> {
                entry.setId(new ProjectEmployeeId(entry.getProject()
                                                       .getId(),
                                                  entry.getEmployee()
                                                       .getId()));
                entry = dataManager.persist(entry);
            });

            List<Module> modules = ModelGenerator.createModules(project);
            modules.forEach(module -> dataManager.persist(module));

            List<LogBookEntry> entries = ModelGenerator.createLogbookEntries(permanentEmployees, phases, modules, 10);
            entries.forEach(entry -> dataManager.persist(entry));

        });


        dataManager.commit();
    }

}