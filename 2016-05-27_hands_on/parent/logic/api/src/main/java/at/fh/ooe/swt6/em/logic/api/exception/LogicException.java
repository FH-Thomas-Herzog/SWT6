package at.fh.ooe.swt6.em.logic.api.exception;

/**
 * Created by Thomas on 5/23/2016.
 */
public class LogicException extends RuntimeException {
    public LogicException() {
        super();
    }

    public LogicException(String message) {
        super(message);
    }

    public LogicException(String message,
                          Throwable cause) {
        super(message, cause);
    }

    public LogicException(Throwable cause) {
        super(cause);
    }

    protected LogicException(String message,
                             Throwable cause,
                             boolean enableSuppression,
                             boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
