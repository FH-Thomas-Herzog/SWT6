package at.fh.ooe.swt6.worklog.manager.testsuite.api.watcher;

import at.fh.ooe.swt6.worklog.manager.testsuite.api.watcher.api.AbstractLoggerWatcher;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.Calendar;

/**
 * This class is the test method invocation logger watcher.
 *
 * @author Thomas Herzog <thomas.herzog@students.fh-hagenberg.at>
 * @date Apr 26, 2015
 */
public class LoggingTestInvocationWatcher extends AbstractLoggerWatcher {

    public LoggingTestInvocationWatcher(final Level level) {
        super(level);
    }

    private long startMilis = 0;

    @Override
    public Statement apply(Statement base,
                           Description description) {
        log = Logger.getLogger(description.getTestClass());
        return super.apply(base, description);
    }

    @Override
    protected void starting(Description description) {
        log.log(level, StringUtils.repeat("-", SEPARATOR_REPEATIONS));
        log.log(level,
                String.format(LOG_FORMAT,
                              "started:") + description.getMethodName() + " | " + DateFormatUtils.ISO_DATETIME_FORMAT.format(
                        Calendar.getInstance()));
        startMilis = System.currentTimeMillis();
    }

    @Override
    protected void succeeded(Description description) {
        long millis = System.currentTimeMillis() - startMilis;
        startMilis = 0;
        log.log(level,
                String.format(LOG_FORMAT,
                              "succeeded:") + description.getMethodName() + " | " + DateFormatUtils.ISO_DATETIME_FORMAT.format(
                        Calendar.getInstance()) + " | " + millis + " milis");
    }

    @Override
    protected void failed(Throwable e,
                          Description description) {
        long millis = System.currentTimeMillis() - startMilis;
        startMilis = 0;
        log.log(level,
                String.format(LOG_FORMAT, "failed:") + description.getMethodName() + " | " + millis + " milis",
                e);
    }

}
