package at.fh.ooe.swt6.test.worklog.manager.service;

import at.fh.ooe.swt6.worklog.manager.model.*;
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

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

/**
 * Created by Thomas on 4/17/2016.
 */
@RunWith(JUnit4.class)
public class WorklogManagerTest {

    //<editor-fold desc="Private Members">
    // Keep reference to the used data manager to be able to manipulate persistence context.
    private DataManager toTestDataManager;
    private WorklogManagerDataAccess dataAccess;
    private WorklogManagerService service;
    private final Random random = new Random();

    private static DataManagerProvider dataManagerProvider;
    private static Logger log = LoggerFactory.getLogger(WorklogManagerTest.class);
    private static Level level = Level.DEBUG;
    private static final String LOG_SEPARATOR = "-----------------------------------------------------------------";
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
        // Finally close the backed persistence provider
        dataManagerProvider.close();
        dataManagerProvider = null;
    }

    @Before
    public void beforeTest() {
        // This will refresh the database
        dataManagerProvider.recreateContext();
        // Get a fresh entity manager
        toTestDataManager = dataManagerProvider.create(Boolean.FALSE);
        // create data access
        dataAccess = new WorklogManagerDataAccessImpl(toTestDataManager);
        // create service
        service = new WorklogManagerServiceImpl(toTestDataManager);
    }

    @After
    public void afterTest() {
        // close data access/service
        dataAccess.close();
        service.close();
        // Should be closed already
        toTestDataManager.close();

        // release references
        toTestDataManager = null;
        dataAccess = null;
        service = null;
    }
    //</editor-fold>

    //<editor-fold desc="Test Methods">
    @Test
    public void getAllProjects() {
        // -- given --
        long startMillis = 0;
        final int projectCount = 200;
        final int temporaryCount = 50;
        final int modulePerProjectCount = 10;
        final int permantentCount = projectCount;
        final int employeesPerProjectCount = temporaryCount;

        final List<PermanentEmployee> permanentEmployees = new ArrayList<>(permantentCount);
        final List<TemporaryEmployee> temporaryEmployees = new ArrayList<>(temporaryCount);

        invokeTx((dataManager) -> {
            permanentEmployees.addAll(dataManager.batchPersist(
                    ModelGenerator.createPermanatEmployees(
                            permantentCount)));
            temporaryEmployees.addAll(dataManager.batchPersist(
                    ModelGenerator.createTemporaryEmployees(
                            temporaryCount)));

            final List<Project> projects = dataManager.batchPersist(ModelGenerator.createProjects(permanentEmployees,
                                                                                                  temporaryEmployees,
                                                                                                  modulePerProjectCount));
            projects.forEach(item -> item.getModules()
                                         .addAll(dataManager.batchPersist(ModelGenerator.createModules(
                                                 modulePerProjectCount,
                                                 item))));
        });
        log.debug(LOG_SEPARATOR);
        log.debug("permanent employees: {}", permantentCount);
        log.debug("temporary employees: {}", temporaryCount);
        log.debug("projects:            {}", projectCount);
        log.debug("modules/project:     {}", modulePerProjectCount);
        log.debug("employees/project:   {}", employeesPerProjectCount);
        log.debug(LOG_SEPARATOR);

        // -- when --
        List<Project> actualProjects = Collections.emptyList();
        try {
            startMillis = System.currentTimeMillis();
            toTestDataManager.startTx();
            actualProjects = dataAccess.getAllProjects();
            log.debug("Loaded {} project. duration: {} millis", actualProjects.size(),
                      (System.currentTimeMillis() - startMillis));
//        log.debug(LOG_SEPARATOR);
//        logProjectInfo(actualProjects);
//        log.debug(LOG_SEPARATOR);
            toTestDataManager.commit();
        } catch (Exception e) {
            toTestDataManager.rollback();
            throw new AssertionError(e);
        } finally {
            toTestDataManager.clear();
        }


        // -- then --
        assertEquals(projectCount, actualProjects.size());
    }

    @Test
    public void getAllPermanentEmployees() {
        // -- given --
        final int permanentCount = 100;
        final int temporaryCount = 100;
        invokeTx((dataManager) -> {
            dataManager.batchPersist(ModelGenerator.createPermanatEmployees(permanentCount));
            dataManager.batchPersist(ModelGenerator.createTemporaryEmployees(temporaryCount));
        });
        log.debug(LOG_SEPARATOR);
        log.debug("permanent employees: {}", permanentCount);
        log.debug("temporary employees: {}", temporaryCount);
        log.debug(LOG_SEPARATOR);
        // clear persistence context here so that loaded works against database and not cache
        toTestDataManager.clear();

        // -- when --
        List<PermanentEmployee> actualPermanentEmployees = Collections.emptyList();
        try {
            toTestDataManager.startTx();
            final long startMillis = System.currentTimeMillis();
            actualPermanentEmployees = dataAccess.getAllPermanentEmployees();
            log.debug("Loaded {} permanent-employees. duration: {} millis", actualPermanentEmployees.size(),
                      (System.currentTimeMillis() - startMillis));
        } catch (Exception e) {
            toTestDataManager.rollback();
            throw new AssertionError(e);
        } finally {
            toTestDataManager.clear();
        }

        // -- then --
        assertEquals(permanentCount, actualPermanentEmployees.size());
    }


    @Test
    public void getAllTemporaryEmployees() {
        // -- given --
        final int permanentCount = 100;
        final int temporaryCount = 100;
        invokeTx((dataManager) -> {
            dataManager.batchPersist(ModelGenerator.createPermanatEmployees(permanentCount));
            dataManager.batchPersist(ModelGenerator.createTemporaryEmployees(temporaryCount));
        });
        log.debug(LOG_SEPARATOR);
        log.debug("permanent employees: {}", permanentCount);
        log.debug("temporary employees: {}", temporaryCount);
        log.debug(LOG_SEPARATOR);
        // clear persistence context here so that loaded works against database and not cache
        toTestDataManager.clear();

        // -- when --
        List<TemporaryEmployee> actualTemporaryEmployees = Collections.emptyList();
        try {
            toTestDataManager.startTx();
            actualTemporaryEmployees = dataAccess.getAllTemporaryEmployees();
            toTestDataManager.commit();
            final long startMillis = System.currentTimeMillis();
            log.debug("Loaded {} temporary-employees. duration: {} millis", actualTemporaryEmployees.size(),
                      (System.currentTimeMillis() - startMillis));
        } catch (Exception e) {
            toTestDataManager.rollback();
            e.printStackTrace();
        } finally {
            toTestDataManager.clear();
        }

        // -- then --
        assertEquals(temporaryCount, actualTemporaryEmployees.size());
    }

    @Test
    public void getLogbookEntriesForEmployee() {
        // -- given --
        final int permanentCount = 100;
        final int temporaryCount = 100;
        final int phaseCount = 100;
        final int modulesPerProjectCount = 10;
        final int logbookEntryPerEmployee = 10;

        final List<Employee> allEmployees = new ArrayList<>((permanentCount * logbookEntryPerEmployee) + (temporaryCount * logbookEntryPerEmployee));
        invokeTx((dataManager) -> {
            final List<Phase> phases = dataManager.batchPersist(ModelGenerator.createPhases(phaseCount));
            final List<PermanentEmployee> permanentEmployees = dataManager.batchPersist(ModelGenerator.createPermanatEmployees(
                    permanentCount));
            final List<TemporaryEmployee> temporaryEmployees = dataManager.batchPersist(ModelGenerator.createTemporaryEmployees(
                    temporaryCount));
            final List<Project> projects = dataManager.batchPersist(ModelGenerator.createProjects(permanentEmployees,
                                                                                                  temporaryEmployees,
                                                                                                  modulesPerProjectCount));
            projects.forEach(item -> item.getModules()
                                         .addAll(dataManager.batchPersist(ModelGenerator.createModules(
                                                 modulesPerProjectCount,
                                                 item))));
            final Consumer<Employee> logbookEntryAction = (item) -> dataManager.persist(new LogBookEntry(("Entry_employee_" + item
                    .getId()),
                                                                                                         Calendar.getInstance()
                                                                                                                 .getTime(),
                                                                                                         Calendar.getInstance()
                                                                                                                 .getTime(),
                                                                                                         item,
                                                                                                         phases.get(
                                                                                                                 random.nextInt(
                                                                                                                         phaseCount)),
                                                                                                         (new ArrayList<>(
                                                                                                                 projects.get(
                                                                                                                         random.nextInt(
                                                                                                                                 permanentCount))
                                                                                                                         .getModules())
                                                                                                                 .get(random.nextInt(
                                                                                                                         modulesPerProjectCount)))));
            for (int i = 0; i < logbookEntryPerEmployee; i++) {
                permanentEmployees.forEach(logbookEntryAction);
            }
            for (int i = 0; i < logbookEntryPerEmployee; i++) {
                temporaryEmployees.forEach(logbookEntryAction);
            }

            allEmployees.addAll(permanentEmployees);
            allEmployees.addAll(temporaryEmployees);
        });

        try {
            for (Employee employee : allEmployees) {
                // -- when --
                toTestDataManager.startTx();
                final List<LogBookEntry> actualLogbookEntries = dataAccess.getLogbookEntriesForEmployee(employee.getId());
                toTestDataManager.commit();
                toTestDataManager.clear();

                // -- then --
                assertEquals(logbookEntryPerEmployee, actualLogbookEntries.size());
            }
        } catch (Exception e) {
            toTestDataManager.rollback();
            e.printStackTrace();
        }
    }
    //</editor-fold>

    //<editor-fold desc="Private Helper">

    /**
     * @param consumer the data manager provided.
     * @see WorklogManagerTest#invokeTx(Function)
     */
    private void invokeTx(Consumer<DataManager> consumer) {
        invokeTx((dataManager) -> {
            consumer.accept(dataManager);
            return null;
        });
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

    /**
     * Concats the employee parameters to produce a full name.
     *
     * @param employee the employee to create full name for
     * @return the created full name
     */
    private String getEmployeeFullName(Employee employee) {
        return new StringBuilder(employee.getLastName()).append(", ").append(employee.getFirstName()).toString();
    }

    /**
     * Logs the project information to the console
     *
     * @param projects the projects to log info from
     */
    private void logProjectInfo(final List<Project> projects) {
        projects.forEach(project -> {
            log.debug(LOG_SEPARATOR);
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
            log.debug(LOG_SEPARATOR);
        });
    }
    //</editor-fold>
}
