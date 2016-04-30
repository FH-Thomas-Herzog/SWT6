package swt6.spring.basics.ioc.util;

import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Thomas on 4/30/2016.
 */
@Autowired
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,
         ElementType.TYPE})
public @interface Log {
    LoggerType type() default LoggerType.STANDARD;
}
