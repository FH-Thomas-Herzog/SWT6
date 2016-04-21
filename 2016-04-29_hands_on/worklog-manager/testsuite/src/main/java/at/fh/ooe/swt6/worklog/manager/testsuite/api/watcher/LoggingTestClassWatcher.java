package at.fh.ooe.swt6.worklog.manager.testsuite.api.watcher;

import at.fh.ooe.swt6.worklog.manager.testsuite.api.watcher.api.AbstractLoggerWatcher;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * This class is the test method invocation logger watcher.
 *
 * @author Thomas Herzog <thomas.herzog@students.fh-hagenberg.at>
 * @date Apr 26, 2015
 */
public class LoggingTestClassWatcher extends AbstractLoggerWatcher {

    //<editor-fold desc="Private Members">
    private String className;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public LoggingTestClassWatcher(final Level level) {
        super(level);
    }
    //</editor-fold>

    //<editor-fold desc="Overwritten TestWatcher Methods">
    @Override
    public Statement apply(Statement base,
                           Description description) {
        log = Logger.getLogger(description.getTestClass());
        return super.apply(base, description);
    }

    @Override
    protected void starting(Description description) {
        className = description.getTestClass()
                               .getName();
        log.log(level, StringUtils.repeat("#", SEPARATOR_REPEATIONS));
        log.log(level, new StringBuilder(String.format(LOG_FORMAT, "started:")).append(className)
                                                                               .toString());
        log.log(level, StringUtils.repeat("#", SEPARATOR_REPEATIONS));
    }

    @Override
    protected void succeeded(Description description) {
        log.log(level, StringUtils.repeat("-", SEPARATOR_REPEATIONS));
        log.log(level, StringUtils.repeat("#", SEPARATOR_REPEATIONS));
        log.log(level, new StringBuilder(String.format(LOG_FORMAT, "succeeded:")).append(className)
                                                                                 .toString());
        log.log(level, StringUtils.repeat("#", SEPARATOR_REPEATIONS));
    }

    @Override
    protected void failed(Throwable e,
                          Description description) {
        log.log(level, StringUtils.repeat("-", SEPARATOR_REPEATIONS));
        log.log(level, StringUtils.repeat("#", SEPARATOR_REPEATIONS));
        log.log(level, new StringBuilder(String.format(LOG_FORMAT, "failed:")).append(className)
                                                                              .toString(), e);
        log.log(level, StringUtils.repeat("#", SEPARATOR_REPEATIONS));
    }

    //</editor-fold>
}
