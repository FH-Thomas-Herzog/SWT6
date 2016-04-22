package at.fh.ooe.swt6.test.worklog.manager.service.util;

import at.fh.ooe.swt6.worklog.manager.model.*;
import at.fh.ooe.swt6.worklog.manager.service.api.Closable;
import at.fh.ooe.swt6.worklog.manager.service.api.DataManager;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ModelGenerator;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by Thomas on 4/21/2016.
 */
public class TestDataPersister implements Closable {

    //<editor-fold desc="Inner Types">
    public static enum EntityType {
        PROJECT,
        EMPLOYEE;
    }
    //</editor-fold>

    //<editor-fold desc="Private Memebers">
    private DataManager dataManager;
    private final Random random = new Random();

    private final Function<Long, Long> nanotMillis = ((nano) -> nano / 1000000L);
    private final Function<List<?>, ?> getRandomItemFormList = ((items) -> items.get(random.nextInt(items.size())));
    private final Function<Set<?>, ?> getRandomItemFormSet = ((items) -> getRandomItemFormList.apply(new ArrayList<>(
            items)));

    private static final Logger log = LoggerFactory.getLogger(TestDataPersister.class);
    private static final String LOG_SEPARATOR = "-----------------------------------------------------------------";
    //</editor-fold>

    //<editor-fold desc="Constructors">

    /**
     * @param dataManager the data manager to use
     */
    public TestDataPersister(DataManager dataManager) {
        Objects.requireNonNull(dataManager, "DataManager instance must not be null");
        this.dataManager = dataManager;
    }
    //</editor-fold>

    //<editor-fold desc="Create and Persist Methods">

    /**
     * Persists the logbook entries for the given entity type
     *
     * @param permanentCount         the count of permanent employees (= project count)
     * @param temporaryCount         the count of temporary employees (= project employees)
     * @param phaseCount             the count of phases to generate (randomly assigned)
     * @param modulesPerProjectCount the count of modules to create per project
     * @param logbookEntryPerEntity  the count of logbook entries per entity type
     * @param type                   the set entity type
     * @throws NullPointerException if type is null
     * @see EntityType for available entity types to produce logbookentries for
     */
    public void createAndPersistLogbookEntries(final int permanentCount,
                                               final int temporaryCount,
                                               final int phaseCount,
                                               final int modulesPerProjectCount,
                                               final int logbookEntryPerEntity,
                                               final EntityType type) {
        Objects.requireNonNull(type, "EntityType must not be null");

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.debug(LOG_SEPARATOR);
        log.debug("createAndPersistLogbookEntries started");
        try {
            dataManager.startTx();
            final List<Phase> phases = dataManager.batchPersist(ModelGenerator.createPhases(phaseCount));
            final List<PermanentEmployee> permanentEmployees = dataManager.batchPersist(ModelGenerator.createPermanatEmployees(
                    permanentCount));
            final List<TemporaryEmployee> temporaryEmployees = dataManager.batchPersist(ModelGenerator.createTemporaryEmployees(
                    temporaryCount));
            final List<Project> projects = dataManager.batchPersist(ModelGenerator.createProjects(permanentEmployees,
                                                                                                  temporaryEmployees
            ));
            projects.forEach(item -> item.getModules()
                                         .addAll(dataManager.batchPersist(ModelGenerator.createModules(
                                                 modulesPerProjectCount,
                                                 item))));
            final BiConsumer<Employee, Project> logbookEntryAction = (employee, project) -> {
                final Module useModule = (project != null) ? (Module) getRandomItemFormSet.apply(project.getModules()) : (Module) getRandomItemFormSet
                        .apply(((Project) getRandomItemFormList.apply(projects)).getModules());
                dataManager.persist(new LogBookEntry(("Entry_employee_" + employee.getId()),
                                                     Calendar.getInstance()
                                                             .getTime(),
                                                     Calendar.getInstance()
                                                             .getTime(),
                                                     employee,
                                                     (Phase) getRandomItemFormList
                                                             .apply(phases),
                                                     useModule));
            };

            switch (type) {
                case EMPLOYEE:
                    for (int i = 0; i < logbookEntryPerEntity; i++) {
                        permanentEmployees.forEach(item -> logbookEntryAction.accept(item, null));
                    }
                    for (int i = 0; i < logbookEntryPerEntity; i++) {
                        temporaryEmployees.forEach(item -> logbookEntryAction.accept(item, null));
                    }
                    break;
                case PROJECT:
                    for (int i = 0; i < logbookEntryPerEntity; i++) {
                        projects.forEach(item -> logbookEntryAction.accept(permanentEmployees.get(projects.indexOf(item)),
                                                                           item));
                    }
                    break;
                default:
                    break;
            }
            dataManager.commit();

            log.debug("permanent employees: {}", permanentCount);
            log.debug("temporary employees: {}", temporaryCount);
            log.debug("projects:            {}", permanentCount);
            log.debug("phases:              {}", phaseCount);
            log.debug("modules/project:     {}", modulesPerProjectCount);
            log.debug("employees/project:   {}", temporaryCount);
            log.debug("logbookentries/{}:   {}", type, logbookEntryPerEntity);
        } catch (Exception e) {
            dataManager.rollback();
            log.error("createAndPersistLogbookEntries failed", e);
        } finally {
            stopWatch.stop();
            dataManager.clear();
        }

        log.debug("createAndPersistLogbookEntries ended. duration: {} ms", nanotMillis.apply(stopWatch.getNanoTime()));
        log.debug(LOG_SEPARATOR);
    }

    /**
     * Creates and persists projects.
     *
     * @param permantentCount       the count of permanent employees (= project count)
     * @param temporaryCount        the count of temporary employees (= project employees)
     * @param modulePerProjectCount the count of modules to create per project
     */
    public void createAndPersistProjects(final int permantentCount,
                                         final int temporaryCount,
                                         final int modulePerProjectCount) {
        final List<Project> projects = new ArrayList<>(permantentCount);
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.debug(LOG_SEPARATOR);
        log.debug("createAndPersistProjects started");
        final long startMillis = System.currentTimeMillis();
        try {
            dataManager.startTx();
            final List<PermanentEmployee> permanentEmployees = dataManager.batchPersist(
                    ModelGenerator.createPermanatEmployees(
                            permantentCount));
            final List<TemporaryEmployee> temporaryEmployees = dataManager.batchPersist(
                    ModelGenerator.createTemporaryEmployees(
                            temporaryCount));

            projects.addAll(dataManager.batchPersist(ModelGenerator.createProjects(permanentEmployees,
                                                                                   temporaryEmployees
            )));
            permanentEmployees.forEach(item -> item.getLeadingProjects().addAll(projects));
            temporaryEmployees.forEach(item -> item.getEmployeeProjects().addAll(projects));

            projects.forEach(item -> item.getModules()
                                         .addAll(dataManager.batchPersist(ModelGenerator.createModules(
                                                 modulePerProjectCount,
                                                 item))));

            dataManager.commit();

            log.debug("permanent employees: {}", permantentCount);
            log.debug("temporary employees: {}", temporaryCount);
            log.debug("projects:            {}", permantentCount);
            log.debug("modules/project:     {}", modulePerProjectCount);
            log.debug("employees/project:   {}", temporaryCount);
        } catch (Exception e) {
            dataManager.rollback();
            log.error("createAndPersistLogbookEntries failed", e);
        } finally {
            stopWatch.stop();
            dataManager.clear();
        }

        log.debug("createAndPersistProjects ended. duration: {} ms", nanotMillis.apply(stopWatch.getNanoTime()));
        log.debug(LOG_SEPARATOR);
    }
    //</editor-fold>

    //<editor-fold desc="Lifecycle Methods">

    /**
     * Closes this test data persister by closing the used data manager.
     */
    @Override
    public void close() {
        if (dataManager != null) {
            dataManager.close();
        }
    }
    //</editor-fold>
}
