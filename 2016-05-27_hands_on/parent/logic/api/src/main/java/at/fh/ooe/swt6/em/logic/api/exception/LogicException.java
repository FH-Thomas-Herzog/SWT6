package at.fh.ooe.swt6.em.logic.api.exception;

/**
 * Created by Thomas on 5/23/2016.
 */
public class LogicException extends RuntimeException {

    public enum ServiceCode {
        ENTITY_EXISTS,
        ENTITY_NOT_FOUND,
        INVALID_DATA,
        INVALID_ACTION,
        UNKNOWN
    }

    private final ServiceCode code;

    public LogicException(ServiceCode code) {
        super();
        this.code = code;
    }

    public LogicException(String message,
                          ServiceCode code) {
        this(message, null, code);
    }

    public LogicException(Throwable cause,
                          ServiceCode code) {
        this(null, cause, code);
    }


    public LogicException(String message,
                          Throwable cause,
                          ServiceCode code) {
        super(message, cause);
        this.code = code;
    }

    public ServiceCode getCode() {
        return code;
    }
}
