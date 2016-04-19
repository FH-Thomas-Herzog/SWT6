package at.fh.ooe.swt6.test.worklog.manager.service;

import at.fh.ooe.swt6.worklog.manager.model.Employee;
import at.fh.ooe.swt6.worklog.manager.model.PermanentEmployee;
import at.fh.ooe.swt6.worklog.manager.model.Project;
import at.fh.ooe.swt6.worklog.manager.model.TemporaryEmployee;
import at.fh.ooe.swt6.worklog.manager.service.api.*;
import at.fh.ooe.swt6.worklog.manager.testsuite.api.watcher.LoggingTestClassWatcher;
import at.fh.ooe.swt6.worklog.manager.testsuite.api.watcher.LoggingTestInvocationWatcher;
import org.apache.log4j.Level;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ModelGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Thomas on 4/17/2016.
 */
@RunWith(JUnit4.class)
public class WorklogManagerTest {

    //<editor-fold desc="Private Members">
    private WorklogManagerDataAccess dataAccess;
    private WorklogManagerService service;

    private static DataManagerProvider dataManagerProvider;
    private static Logger log = LoggerFactory.getLogger(WorklogManagerTest.class);
    private static Level level = Level.DEBUG;
    //</editor-fold>

    //<editor-fold desc="Test Rules">
    @ClassRule
    public static LoggingTestClassWatcher watcher = new LoggingTestClassWatcher(level);

    @Rule
    public LoggingTestInvocationWatcher methodWatcher = new LoggingTestInvocationWatcher(level);
    //</editor-fold>


    //<editor-fold desc="Test Lifecycle">
    @BeforeClass
    public static void beforeClass() {
        dataManagerProvider = DataManagerFactory.createDataManagerProvider();
    }

    @AfterClass
    public static void afterClass() {
        dataManagerProvider.close();
        dataManagerProvider = null;
    }

    @Before
    public void beforeTest() {
        // This will refresh the database
        dataManagerProvider.recreateContext();
        // Get a fresh entity manager
        final DataManager dm = dataManagerProvider.create(Boolean.FALSE);
        // create data access
        dataAccess = new WorklogManagerDataAccessImpl(dm);
        // create service
        service = new WorklogManagerServiceImpl(dm);
    }

    @After
    public void afterTest() {
        // close data access/service
        dataAccess.close();
        service.close();
        dataAccess = null;
        service = null;
    }
    //</editor-fold>

    /**
     * This test simulates a read of all projects and listing all of its information.
     */
    @Test
    public void loadAllProjectsAndListThem() {
        // -- given --
        long startMillis = 0;
        final int projectCount = 50;
        final int permantentCount = projectCount;
        final int temporaryCount = 10;

        final List<PermanentEmployee> permanentEmployees = invokeTx((dataManager) -> dataManager.batchPersist(
                ModelGenerator.createPermanatEmployees(
                        permantentCount)));
        log.debug("Created {} permanentEmployees", permantentCount);
        final List<TemporaryEmployee> temporaryEmployees = invokeTx((dataManager) -> dataManager.batchPersist(
                ModelGenerator.createTemporaryEmployees(
                        temporaryCount)));
        log.debug("Created {} temporaryEmployees", permantentCount);
        startMillis = System.currentTimeMillis();
        final List<Project> projects = new ArrayList<>(projectCount);
        for (int i = 0; i < 50; i++) {
            projects.add(service.createProject(
                    "Project_" + i,
                    permanentEmployees.get(i),
                    ModelGenerator.createModules(null),
                    temporaryEmployees));
        }
        log.debug("Created {} projects. duration: {} millis",
                  permantentCount,
                  (System.currentTimeMillis() - startMillis));

        // -- when --
        startMillis = System.currentTimeMillis();
        final List<Project> actualProjects = dataAccess.getAllProjects();
        log.debug("Loaded {} project. duration: {} millis", projectCount,
                  (System.currentTimeMillis() - startMillis));
        actualProjects.forEach(project -> {
            log.debug("----------------------------------------------------");
            log.debug("Project[id={}]: {} ", project.getId(), project.getName());
            log.debug("Leader [id={}]: {} ",
                      project.getLeader().getId(),
                      (project.getLeader().getLastName() + ", " + project.getLeader().getFirstName()));
            log.debug("Modules:        {} ", project.getModules().size());
            project.getModules().forEach(module -> {
                log.debug("        {}", module.getName());
            });
            log.debug("Employees:        {} ", project.getModules().size());
            project.getProjectEmployees().forEach(employee -> {
                log.debug("        [type={}] = {} ",
                          employee.getClass().getSimpleName(),
                          getEmployeeFullName(employee));
            });
            log.debug("----------------------------------------------------");
        });

        // -- then --
        Assert.assertEquals(projectCount, projects.size());
        Assert.assertEquals(projects.size(), actualProjects.size());
    }

    /**
     * Invoces the consumer within and data transaction.
     * This method will log the data creation elapsed time.
     *
     * @param function the consumer providing the DAtaManger which opened the connection.
     */
    private <T> T invokeTx(Function<DataManager, T> function) {
        final DataManager dataManager = dataManagerProvider.create(Boolean.FALSE);
        long startMillis = System.currentTimeMillis();
        T result = null;
        try {
            dataManager.startTx();
            result = function.apply(dataManager);
            dataManager.commit();
            dataManager.clear();
        } catch (Exception e) {
            e.printStackTrace();
            dataManager.rollback();
        } finally {
            dataManager.close();
        }
        log.debug("Ended transactional. duration: {} millis", (System.currentTimeMillis() - startMillis));

        return result;
    }

    private String getEmployeeFullName(Employee employee) {
        return new StringBuilder(employee.getLastName()).append(", ").append(employee.getFirstName()).toString();
    }

  /*  public void createTestData() {
        try {
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

                List<LogBookEntry> entries = ModelGenerator.createLogbookEntries(permanentEmployees,
                                                                                 phases,
                                                                                 modules,
                                                                                 10);
                entries.forEach(entry -> dataManager.persist(entry));

            });

            dataManager.commit();

        } catch (Exception e) {
            e.printStackTrace();
            dataManager.rollback();
        }
    }*/

}
