package at.fh.ooe.swt6.test.worklog.manager.service;

import at.fh.ooe.swt6.test.worklog.manager.service.api.AbstractWorklogManagerTest;
import at.fh.ooe.swt6.test.worklog.manager.service.util.TestDataPersister;
import at.fh.ooe.swt6.worklog.manager.model.*;
import at.fh.ooe.swt6.worklog.manager.service.api.*;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import utils.ModelGenerator;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Thomas on 4/17/2016.
 */
@RunWith(JUnit4.class)
public class WorklogManagerDataAccessTest extends AbstractWorklogManagerTest {

    //<editor-fold desc="Private Members">
    // Keep reference to the used data manager to be able to manipulate persistence context.
    private DataManager toTestDataManager;
    private WorklogManagerDataAccess dataAccess;
    private WorklogManagerService service;

    // Helper for creating test data
    private TestDataPersister testDataPersister;
    //</editor-fold>

    //<editor-fold desc="Test Lifecycle">
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
        // create test data helper
        testDataPersister = new TestDataPersister(dataManagerProvider.create(Boolean.FALSE));
    }

    @After
    public void afterTest() {
        // close data access/service
        dataAccess.close();
        service.close();
        // Should be closed already
        toTestDataManager.close();
        // close test data helper
        testDataPersister.close();

        // release references
        toTestDataManager = null;
        dataAccess = null;
        service = null;
        testDataPersister = null;
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

        // create test data
        testDataPersister.createAndPersistProjects(permantentCount, temporaryCount, modulePerProjectCount);

        // -- when --
        List<Project> actualProjects = Collections.emptyList();
        try {
            final StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            toTestDataManager.startTx();
            actualProjects = dataAccess.getAllProjects();
            toTestDataManager.commit();

            stopWatch.stop();
            log.debug("Loaded {} project. duration: {} ms",
                      actualProjects.size(),
                      nanotMillis.apply(stopWatch.getNanoTime()));
//        log.debug(LOG_SEPARATOR);
//        logProjectInfo(actualProjects);
//        log.debug(LOG_SEPARATOR);
        } catch (Exception e) {
            toTestDataManager.rollback();
            throw new AssertionError("Test method failed", e);
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

        // -- when --
        List<PermanentEmployee> actualPermanentEmployees = Collections.emptyList();
        try {
            final StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            toTestDataManager.startTx();
            actualPermanentEmployees = dataAccess.getAllPermanentEmployees();
            toTestDataManager.commit();

            stopWatch.stop();
            log.debug("Loaded {} permanent-employees. duration: {} millis",
                      actualPermanentEmployees.size(),
                      nanotMillis.apply(stopWatch.getNanoTime()));
        } catch (Exception e) {
            toTestDataManager.rollback();
            throw new AssertionError("Test method failed", e);
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

        // -- when --
        List<TemporaryEmployee> actualTemporaryEmployees = Collections.emptyList();
        try {
            final StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            toTestDataManager.startTx();
            actualTemporaryEmployees = dataAccess.getAllTemporaryEmployees();
            toTestDataManager.commit();

            stopWatch.stop();
            log.debug("Loaded {} temporary-employees. duration: {} millis",
                      actualTemporaryEmployees.size(),
                      nanotMillis.apply(stopWatch.getNanoTime()));
        } catch (Exception e) {
            toTestDataManager.rollback();
            throw new AssertionError("Test method failed", e);
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

        // create test data
        testDataPersister.createAndPersistLogbookEntries(permanentCount,
                                                         temporaryCount,
                                                         phaseCount,
                                                         modulesPerProjectCount,
                                                         logbookEntryPerEmployee,
                                                         TestDataPersister.EntityType.EMPLOYEE);
        // load all employees
        final List<Employee> allEmployees = toTestDataManager.loadAllForClass(Employee.class);

        try {
            // check for each employee
            for (Employee employee : allEmployees) {
                // -- when --
                final StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                toTestDataManager.startTx();
                final List<LogBookEntry> actualLogbookEntries = dataAccess.getLogbookEntriesForEmployee(employee.getId());
                toTestDataManager.commit();
                stopWatch.stop();
                toTestDataManager.clear();

                log.debug("Loaded {} logbook-entries for emplyee {}. duration: {} ms",
                          actualLogbookEntries.size(),
                          getEmployeeFullName(employee),
                          nanotMillis.apply(stopWatch.getNanoTime()));
                // -- then --
                assertEquals(logbookEntryPerEmployee, actualLogbookEntries.size());
            }
        } catch (Exception e) {
            toTestDataManager.rollback();
            e.printStackTrace();
        }
    }

    @Test
    public void getLogbookEntriesForProject() {
        // -- given --
        final int permanentCount = 100;
        final int temporaryCount = 100;
        final int phaseCount = 100;
        final int modulesPerProjectCount = 10;
        final int logbookEntryPerProject = 10;

        // create test data
        testDataPersister.createAndPersistLogbookEntries(permanentCount,
                                                         temporaryCount,
                                                         phaseCount,
                                                         modulesPerProjectCount,
                                                         logbookEntryPerProject,
                                                         TestDataPersister.EntityType.PROJECT);
        // load all employees
        final List<Project> allProjects = toTestDataManager.loadAllForClass(Project.class);

        try {
            // check for each project
            for (Project project : allProjects) {
                // -- when --
                final StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                toTestDataManager.startTx();
                final List<LogBookEntry> actualLogbookEntries = dataAccess.getLogbookEntriesForProject(project.getId());
                toTestDataManager.commit();
                stopWatch.stop();
                toTestDataManager.clear();

                log.debug("Loaded {} logbook-entries for project {}. duration: {} ms",
                          actualLogbookEntries.size(),
                          project.getName(),
                          nanotMillis.apply(stopWatch.getNanoTime()));
                // -- then --
                assertEquals(logbookEntryPerProject, actualLogbookEntries.size());
            }
        } catch (Exception e) {
            toTestDataManager.rollback();
            e.printStackTrace();
        }
    }
    //</editor-fold>
}
