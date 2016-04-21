package at.fh.ooe.swt6.worklog.manager.service.api;

/**
 * This interface marks an instance as closable.
 * <p>
 * Created by Thomas on 4/18/2016.
 */
@FunctionalInterface
public interface Closable {
    /**
     * Performs close operation
     */
    void close();
}
