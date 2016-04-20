package at.fh.ooe.swt6.test.worklog.manager.service;

import at.fh.ooe.swt6.test.worklog.manager.service.api.AbstractWorklogManagerTest;
import at.fh.ooe.swt6.worklog.manager.model.*;
import at.fh.ooe.swt6.worklog.manager.service.api.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import utils.ModelGenerator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

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
}
