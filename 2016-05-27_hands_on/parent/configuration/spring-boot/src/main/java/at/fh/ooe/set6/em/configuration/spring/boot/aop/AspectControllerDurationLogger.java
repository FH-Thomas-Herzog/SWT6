package at.fh.ooe.set6.em.configuration.spring.boot.aop;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * This aspect is used for logging the Controller invocation duration.
 * <p>
 * Created by Thomas on 5/18/2016.
 */
@Aspect
@Component
public class AspectControllerDurationLogger {

    private static final Logger log = LoggerFactory.getLogger(AspectControllerDurationLogger.class);

    /**
     * Applies to all invoked methods within package *.mvc.controller on any bean which name ends with Controller.
     *
     * @param jp the intercepted join point
     * @return the result of hte invoked method
     */
    @Around("execution(* *..mvc.controller..*Controller..*(..))")
    public Object aroundInvoke(ProceedingJoinPoint jp) {
        Object result = null;
        final String fullName = jp.getTarget().getClass().getSimpleName() + "#" + jp.getSignature().getName();
        StopWatch watch = new StopWatch();
        watch.start();
        log.info("Invocation {} started: {}",
                 fullName,
                 DateFormatUtils.ISO_TIME_NO_T_FORMAT.format(Calendar.getInstance()));
        try {
            result = jp.proceed();
            watch.stop();
        } catch (Throwable throwable) {
            watch.stop();
            log.error("Invocation {} failed:", fullName);
            log.error("Exception: ", throwable);
        }

        log.info("Invocation {} took : {}",
                 fullName,
                 watch.toString());
        log.info("Invocation {} ended: {}",
                 fullName,
                 DateFormatUtils.ISO_TIME_NO_T_FORMAT.format(Calendar.getInstance()));

        return result;
    }
}
