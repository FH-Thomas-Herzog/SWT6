package swt6.spring.basics.ioc.util;

import org.springframework.stereotype.Component;

@Component("consoleLogger")
@Log
public class ConsoleLogger implements Logger {

    @Override
    public void log(String msg) {
        System.out.format("%s: %s%n", this.getClass().getSimpleName(), msg);
    }
}
