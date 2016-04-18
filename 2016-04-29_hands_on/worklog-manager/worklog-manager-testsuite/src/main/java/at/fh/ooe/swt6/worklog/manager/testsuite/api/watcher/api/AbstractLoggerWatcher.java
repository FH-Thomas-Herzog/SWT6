package at.fh.ooe.swt6.worklog.manager.testsuite.api.watcher.api;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.rules.TestWatcher;

/**
 * This is the base class for the loggin watchers.
 *
 * @author Thomas Herzog <thomas.herzog@students.fh-hagenberg.at>
 * @date May 2, 2015
 */
public abstract class AbstractLoggerWatcher extends TestWatcher {

    protected Logger log;
    protected final Level level;
    protected static final int SEPARATOR_REPEATIONS = 100;
    protected static final String LOG_FORMAT = "%1$-12s";

    public AbstractLoggerWatcher(Level level) {
        super();
        this.level = level;
    }

}
