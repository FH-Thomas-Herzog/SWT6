package at.fh.ooe.swt6.test.worklog.manager.service.api;

import at.fh.ooe.swt6.test.worklog.manager.service.WorklogManagerDataAccessTest;
import at.fh.ooe.swt6.worklog.manager.model.Employee;
import at.fh.ooe.swt6.worklog.manager.model.Project;
import at.fh.ooe.swt6.worklog.manager.service.api.DataManager;
import at.fh.ooe.swt6.worklog.manager.service.api.DataManagerFactory;
import at.fh.ooe.swt6.worklog.manager.service.api.DataManagerProvider;
import at.fh.ooe.swt6.worklog.manager.testsuite.api.watcher.LoggingTestClassWatcher;
import at.fh.ooe.swt6.worklog.manager.testsuite.api.watcher.LoggingTestInvocationWatcher;
import org.apache.log4j.Level;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by Thomas on 4/20/2016.
 */
public abstract class AbstractWorklogManagerTest {

    //<editor-fold desc="Private Members">
    protected final Random random = new Random();

    protected static DataManagerProvider dataManagerProvider;
    protected static Logger log = LoggerFactory.getLogger(WorklogManagerDataAccessTest.class);
    protected static Level level = Level.DEBUG;
    protected static final String LOG_SEPARATOR = "-----------------------------------------------------------------";
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
    //</editor-fold>

    //<editor-fold desc="Test Rules">
    @ClassRule
    public static LoggingTestClassWatcher watcher = new LoggingTestClassWatcher(level);

    @Rule
    public LoggingTestInvocationWatcher methodWatcher = new LoggingTestInvocationWatcher(level);
    //</editor-fold>

    //<editor-fold desc="Protected Test Helper">

    /**
     * @param consumer the data manager provided.
     * @see WorklogManagerDataAccessTest#invokeTx(Function)
     */
    protected void invokeTx(Consumer<DataManager> consumer) {
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
    protected <T> T invokeTx(Function<DataManager, T> function) {
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
    protected String getEmployeeFullName(Employee employee) {
        return new StringBuilder(employee.getLastName()).append(", ").append(employee.getFirstName()).toString();
    }

    /**
     * Logs the project information to the console
     *
     * @param projects the projects to log info from
     */
    protected void logProjectInfo(final List<Project> projects) {
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
