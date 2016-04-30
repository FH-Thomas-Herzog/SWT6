package swt6.spring.basics.ioc.util;

import org.springframework.stereotype.Component;

/**
 * Created by Thomas on 4/30/2016.
 */
@Component
public class LoggerFactory {

    public static Logger getLogger(String type) {
        switch (type.toUpperCase()) {
            case "FILE":
                return new FileLogger();
            case "CONSOLE":
                return new ConsoleLogger();
            default:
                throw new IllegalArgumentException("Type unknown");
        }
    }
}
