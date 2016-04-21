package at.fh.ooe.swt6.test.worklog.manager.service;

import at.fh.ooe.swt6.test.worklog.manager.service.api.AbstractWorklogManagerTest;
import at.fh.ooe.swt6.test.worklog.manager.service.util.TestDataPersister;
import at.fh.ooe.swt6.worklog.manager.model.*;
import at.fh.ooe.swt6.worklog.manager.service.api.DataManager;
import at.fh.ooe.swt6.worklog.manager.service.api.WorklogManagerService;
import at.fh.ooe.swt6.worklog.manager.service.api.WorklogManagerServiceImpl;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import utils.ModelGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * This class tests the implemented {@link WorklogManagerService} interface which demonstrates
 * service operations on the worklog data.
 * This test applies for JPA and Hibernate wince the abstraction realised via {@link DataManager} interface
 * prevents the code from being polluted with JPA or Hibernate specific references.
 * <p>
 * Created by Thomas on 4/21/2016.
 */
@RunWith(JUnit4.class)
public class WorklogManagerServiceTest extends AbstractWorklogManagerTest {

    //<editor-fold desc="Private Members">
    // Keep reference to test dataManager so taht we can manipulate persistence context
    private DataManager toTestDataManager;
    private WorklogManagerService service;
    private TestDataPersister testDataPersister;
    //</editor-fold>

    //<editor-fold desc="Test Lifecycle">

    /**
     * Performs prepations for each test method invocation
     */
    @Before
    public void beforeTest() {
        // This will refresh the database
        dataManagerProvider.recreateContext();
        // Get a fresh entity manager
        toTestDataManager = dataManagerProvider.create(Boolean.FALSE);
        // create service
        service = new WorklogManagerServiceImpl(toTestDataManager);
        // test data helper
        testDataPersister = new TestDataPersister(dataManagerProvider.create(Boolean.FALSE));
    }

    /**
     * Performs cleanup for each test method invocation.
     */
    @After
    public void afterTest() {
        // close data service
        service.close();
        // Should be closed already
        toTestDataManager.close();
        // close test data helper
        testDataPersister.close();

        // release references
        toTestDataManager = null;
        service = null;
        testDataPersister = null;
    }
    //</editor-fold>

    //<editor-fold desc="Test Methods">

    /**
     * This test methods create n projects in a loop an measure the time elapsed for each project generation.
     */
    @Test
    public void createProject() {
        // -- given --
        final int projectCount = 100;
        final int employeesPerProjectCount = 50;
        final int modulesPerProjectCount = 50;
        final List<TemporaryEmployee> projectEmployees = new ArrayList<>(employeesPerProjectCount);
        final List<PermanentEmployee> projectOwners = new ArrayList<>(projectCount);
        // create employees
        invokeTx((dataManager) -> {
            projectEmployees.addAll(dataManager.batchPersist(ModelGenerator.createTemporaryEmployees(
                    employeesPerProjectCount)));
            projectOwners.addAll(dataManager.batchPersist(ModelGenerator.createPermanatEmployees(projectCount)));
        });
        log.debug(LOG_SEPARATOR);
        log.debug("permanent employees: {}", projectCount);
        log.debug("temporary employees: {}", employeesPerProjectCount);
        log.debug(LOG_SEPARATOR);

        try {
            // create project for each permanent-employee
            for (PermanentEmployee employee : projectOwners) {
                // -- when --
                final StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                // create via service
                final Project project = service.createProject("Project_" + employee.getFirstName()
                        , employee, ModelGenerator.createModules(modulesPerProjectCount, null), projectEmployees);
                stopWatch.stop();
                toTestDataManager.clear();

                log.debug("Created the project. duration: {} ms",
                          nanotMillis.apply(stopWatch.getNanoTime()));

                // -- then --
                // perform assertion
                assertEquals(employee, project.getLeader());
                assertEquals(employeesPerProjectCount, project.getProjectEmployees().size());
            }
        } catch (Exception e) {
            if (toTestDataManager.isTxOpen()) {
                toTestDataManager.rollback();
            }
            log.error("Test method failed", e);
            fail();
        }
    }

    /**
     * This test assings n employees to m existing projects with k already assigned employees.
     */
    @Test
    public void assignEmployeesToProjects() {
        // -- given --
        final int projectCount = 100;
        final int employeesPerProjectCount = 50;
        final int modulesPerProjectCount = 50;
        final int newProjectEmployeesCount = 50;
        final List<Employee> newProjectEmployees = new ArrayList<>(newProjectEmployeesCount * 2);
        // create test data
        testDataPersister.createAndPersistProjects(projectCount,
                                                   employeesPerProjectCount,
                                                   modulesPerProjectCount);
        // create new employees
        invokeTx((dataManager) -> {
            newProjectEmployees.addAll(dataManager.batchPersist(ModelGenerator.createPermanatEmployees(
                    newProjectEmployeesCount)));
            newProjectEmployees.addAll(dataManager.batchPersist(ModelGenerator.createTemporaryEmployees(
                    newProjectEmployeesCount)));
        });

        try {
            // -- when --
            //load created projects
            List<Project> actualProjects = toTestDataManager.loadAllForClass(Project.class);
            final StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            // assign via service
            service.assignEmployeesToProjects(newProjectEmployees, actualProjects);
            stopWatch.stop();
            toTestDataManager.clear();
            log.debug("Assigned {} employees to {} projects. duration: {} ms",
                      (newProjectEmployeesCount * 2),
                      projectCount,
                      nanotMillis.apply(stopWatch.getNanoTime()));

            // -- then --
            // load modified projects
            actualProjects = toTestDataManager.loadAllForClass(Project.class);
            // perform assertions
            assertEquals(projectCount, actualProjects.size());
            actualProjects.forEach(item -> assertEquals((employeesPerProjectCount + (newProjectEmployeesCount * 2)),
                                                        item.getProjectEmployees().size()));
        } catch (Exception e) {
            if (toTestDataManager.isTxOpen()) {
                toTestDataManager.rollback();
            }
            log.error("Test method failed", e);
            fail();
        }
    }

    /**
     * This test removes all projects employee from al projects.
     */
    @Test
    public void removeEmployeesFromProject() {
        // -- given --
        final int projectCount = 100;
        final int employeesPerProjectCount = 50;
        final int modulesPerProjectCount = 50;
        // create test projects
        testDataPersister.createAndPersistProjects(projectCount,
                                                   employeesPerProjectCount,
                                                   modulesPerProjectCount);

        try {
            // -- when --
            // load created projects
            List<Project> actualProjects = toTestDataManager.loadAllForClass(Project.class);
            // extract their employees
            final List<? extends Employee> projectEmployees = actualProjects.stream()
                                                                            .flatMap(item -> item.getProjectEmployees()
                                                                                                 .stream())
                                                                            .distinct()
                                                                            .collect(
                                                                                    Collectors.toList());
            final StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            // remove via service
            service.removeEmployeesFromProject(projectEmployees, actualProjects);
            stopWatch.stop();
            toTestDataManager.clear();

            log.debug("Removed all employees of {} projects. duration: {} ms",
                      projectCount,
                      nanotMillis.apply(stopWatch.getNanoTime()));

            // -- then --
            // load modified projects
            actualProjects = toTestDataManager.loadAllForClass(Project.class);
            // perform assertions
            assertEquals(projectCount, actualProjects.size());
            actualProjects.forEach(item -> assertTrue(item.getProjectEmployees().isEmpty()));
        } catch (Exception e) {
            if (toTestDataManager.isTxOpen()) {
                toTestDataManager.rollback();
            }
            log.error("Test method failed", e);
            fail();
        }
    }

    /**
     * This test removes all projects employee from al projects.
     */
    @Test
    public void removeLogbookEntriesForEmployee() {
        // -- given --
        final int projectCount = 100;
        final int employeesPerProjectCount = 50;
        final int modulesPerProjectCount = 50;
        final int logbookEntriesperEmployeeCount = 100;
        final int logbookEntriesCount = ((projectCount + employeesPerProjectCount) * logbookEntriesperEmployeeCount);
        // create test logbook-entries
        testDataPersister.createAndPersistLogbookEntries(projectCount,
                                                         employeesPerProjectCount,
                                                         10,
                                                         modulesPerProjectCount,
                                                         logbookEntriesperEmployeeCount,
                                                         TestDataPersister.EntityType.EMPLOYEE);

        try {
            // -- when --
            // load created employee ids
            List<Long> actualEmployeeIds = toTestDataManager.loadAllForClass(LogBookEntry.class)
                                                            .stream()
                                                            .map(item -> item.getEmployee().getId())
                                                            .collect(Collectors.toList());
            final StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            // remove via service
            int result = service.removeLogbookEntriesForEmployee(actualEmployeeIds);
            stopWatch.stop();
            toTestDataManager.clear();

            log.debug("Removed all logbook-entries ({}) of all employee. duration: {} ms",
                      result,
                      nanotMillis.apply(stopWatch.getNanoTime()));

            // -- then --
            // try load deleted logbook-entries
            final long actualLogbookCount = toTestDataManager.loadAllForClass(LogBookEntry.class).stream().count();
            // perform assertions
            assertEquals(0, actualLogbookCount);
            assertEquals(logbookEntriesCount, result);
        } catch (Exception e) {
            if (toTestDataManager.isTxOpen()) {
                toTestDataManager.rollback();
            }
            log.error("Test method failed", e);
            fail();
        }
    }

    /**
     * This test removes all projects employee from al projects.
     */
    @Test
    public void removeProjects() {
        // -- given --
        final int projectCount = 10;
        final int employeesPerProjectCount = 10;
        final int modulesPerProjectCount = 10;
        final int logbookEntriesperEmployeeCount = 100;
        final int logbookEntriesCount = ((projectCount + employeesPerProjectCount) * logbookEntriesperEmployeeCount);
        // create test logbook-entries
        testDataPersister.createAndPersistLogbookEntries(projectCount,
                                                         employeesPerProjectCount,
                                                         10,
                                                         modulesPerProjectCount,
                                                         logbookEntriesperEmployeeCount,
                                                         TestDataPersister.EntityType.EMPLOYEE);

        try {
            // -- when --
            // load created projects
            List<Long> actualProjectIds = toTestDataManager.loadAllForClass(LogBookEntry.class)
                                                           .stream()
                                                           .map(item -> item.getModule().getProject().getId())
                                                           .collect(Collectors.toList());
            final StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            // remove via service
            int result = service.removeProjects(actualProjectIds);
            stopWatch.stop();
            toTestDataManager.clear();

            log.debug("Removed all projects ({}) and related entities. duration: {} ms",
                      result,
                      nanotMillis.apply(stopWatch.getNanoTime()));

            // -- then --
            // load modified projects
            final long actualLogbookCount = toTestDataManager.loadAllForClass(LogBookEntry.class).stream().count();
            final long actualProjectCount = toTestDataManager.loadAllForClass(Project.class).stream().count();
            // perform assertions
            assertEquals(0, actualProjectCount);
            assertEquals(0, actualLogbookCount);
            assertEquals(projectCount, result);
        } catch (Exception e) {
            if (toTestDataManager.isTxOpen()) {
                toTestDataManager.rollback();
            }
            log.error("Test method failed", e);
            fail();
        }
    }
    //</editor-fold>
}
