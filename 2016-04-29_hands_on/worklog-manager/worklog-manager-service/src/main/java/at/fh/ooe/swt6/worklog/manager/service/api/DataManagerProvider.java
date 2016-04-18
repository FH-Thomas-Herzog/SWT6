package at.fh.ooe.swt6.worklog.manager.service.api;

/**
 * Created by Thomas on 4/18/2016.
 */
public interface DataManagerProvider {
    /**
     * @return the data manager instance
     * @param threadLocal
     */
    DataManager create(boolean threadLocal);

    /**
     * Recreates the backed data manager provider context.
     */
    void recreateContext();
}
