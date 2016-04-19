package at.fh.ooe.swt6.worklog.manager.service.api;

import at.fh.ooe.swt6.worklog.manager.model.LogBookEntry;
import at.fh.ooe.swt6.worklog.manager.model.PermanentEmployee;
import at.fh.ooe.swt6.worklog.manager.model.Project;
import at.fh.ooe.swt6.worklog.manager.model.TemporaryEmployee;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * This is the implementation of the {@link WorklogManagerDataAccess} which can be used
 * with any implementation of {@link DataManager} interface.
 * <p>
 * Created by Thomas on 4/17/2016.
 */
public class WorklogManagerDataAccessImpl implements WorklogManagerDataAccess {

    private final DataManager dataManager;

    /**
     * @param dataManager the data manager to use
     */
    public WorklogManagerDataAccessImpl(final DataManager dataManager) {
        Objects.requireNonNull(dataManager, "DataManager must not be null");
        this.dataManager = dataManager;
    }

    @Override
    public List<Project> getAllProjects() {
        return dataManager.queryMultipleResult("SELECT DISTINCT p FROM Project p",
                                               Project.class,
                                               null);
    }

    @Override
    public List<PermanentEmployee> getAllPermanentEmployees() {
        return dataManager.queryMultipleResult("FROM PermanentEmployee e ORDER BY UPPER(e.firstName)",
                                               PermanentEmployee.class,
                                               null);
    }

    @Override
    public List<TemporaryEmployee> getAllTemporaryEmployees() {
        return dataManager.queryMultipleResult("FROM TemporaryEmployee e ORDER BY UPPER(e.firstName)",
                                               TemporaryEmployee.class,
                                               null);
    }

    @Override
    public List<LogBookEntry> getLogbookEntriesForEmployee(long id) {
        return dataManager.queryMultipleResult("SELECT entry FROM LogBookEntry entry WHERE entry.employee.id = :id",
                                               LogBookEntry.class,
                                               new HashMap<String, Object>() {{
                                                   put("id", id);
                                               }});
    }

    @Override
    public List<LogBookEntry> getLogbookEntriesForProject(long id) {
        return dataManager.queryMultipleResult(
                "SELECT entry FROM LogBookEntry entry WHERE entry.module.project.id = :id",
                LogBookEntry.class,
                new HashMap<String, Object>() {{
                    put("id", id);
                }});
    }

    @Override
    public void close() {
        dataManager.close();
    }
}
