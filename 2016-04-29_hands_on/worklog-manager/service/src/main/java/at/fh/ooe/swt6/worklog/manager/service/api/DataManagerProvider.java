package at.fh.ooe.swt6.worklog.manager.service.api;

/**
 * This interface specifies an data manager provider which is capable of providing an caller with data manger instances.
 * <p>
 * Created by Thomas on 4/18/2016.
 */
public interface DataManagerProvider {
    /**
     * @param threadLocal true if data manager is managed in thread locale and will be retrieved from there if present.
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
