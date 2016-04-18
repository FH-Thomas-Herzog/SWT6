package at.fh.ooe.swt6.worklog.manager.service.api;

/**
 * Created by Thomas on 4/18/2016.
 */
public class DataManagerFactory {

    private DataManagerFactory() {

    }

    /**
     * Creates a {@link DataManagerProvider} instance which provides the dataManager with backed persistence provider
     *
     * @return
     */
    public static DataManagerProvider createDataManagerProvider() {
        final String className = System.getenv("dataManagerProvider");
        try {
            Class<? extends DataManagerProvider> clazz = (Class<? extends DataManagerProvider>) Class.forName(
                    className);
            return clazz.newInstance();
        } catch (Throwable e) {
            throw new IllegalStateException("Could not create DataManager instance", e);
        }
    }
}
