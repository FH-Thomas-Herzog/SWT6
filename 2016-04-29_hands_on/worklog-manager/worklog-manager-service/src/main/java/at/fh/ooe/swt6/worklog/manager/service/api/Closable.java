package at.fh.ooe.swt6.worklog.manager.service.api;

/**
 * Created by Thomas on 4/18/2016.
 */
@FunctionalInterface
public interface Closable {
    /**
     * Performs close operation
     */
    void close();
}
