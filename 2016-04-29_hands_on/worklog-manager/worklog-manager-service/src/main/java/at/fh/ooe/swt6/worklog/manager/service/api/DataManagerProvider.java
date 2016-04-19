package at.fh.ooe.swt6.worklog.manager.service.api;

/**
 * Created by Thomas on 4/18/2016.
 */
public interface DataManagerProvider {
    /**
     * @param threadLocal
     * @return the data manager instance
     */
    DataManager create(boolean threadLocal);

    /**
     * Recreates the backed data manager provider context.
     */
    void recreateContext();

    /**
     * Closes this data manager provider.
     * From now on now DataManagers can be provided anymore
     */
    void close();
}
