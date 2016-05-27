package at.fh.ooe.set6.em.configuration.spring.boot.app.dev.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * This is the duration logger aspect which can be applied on any Method of any bean.
 * <p>
 * Created by Thomas on 5/18/2016.
 */
@Aspect
@Component
@Slf4j
public class AspectDurationLogger {

    private static final String START_TEMPLATE = "Invocation started: {} ({})";
    private static final String DURAT_TEMPLATE = "Invocation took   : {} ({})";
    private static final String ENDED_TEMPLATE = "Invocation ended  : {} ({})";

    /**
     * Applies to all invoked methods within package *.mvc.controller on any bean which name ends with Controller.
     *
     * @param jp the intercepted join point
     * @return the result of hte invoked errorMethod
     */
    @Around("execution(* *..em.web.mvc..*Controller..*(..))")
    public Object logControllerDuration(ProceedingJoinPoint jp) throws Throwable {
        return aroundInvoke(jp);
    }

    @Around("execution(* *..em.logic..*LogicImpl..*(..))")
    public Object logLogicDuration(ProceedingJoinPoint jp) throws Throwable {
        return aroundInvoke(jp);
    }

    @Around("execution(* *..em.data.dao..*Dao..*(..))")
    public Object logDaoDuration(ProceedingJoinPoint jp) throws Throwable {
        return aroundInvoke(jp);
    }

    private Object aroundInvoke(ProceedingJoinPoint jp) throws Throwable {
        Throwable t = null;
        Object result = null;
        final String fullName = jp.getTarget().getClass().getSimpleName() + "#" + jp.getSignature().getName();
        StopWatch watch = new StopWatch();
        watch.start();
        log.info(START_TEMPLATE,
                 DateFormatUtils.ISO_TIME_NO_T_FORMAT.format(Calendar.getInstance()),
                 fullName);
        try {
            result = jp.proceed();
            watch.stop();
        } catch (Throwable throwable) {
            t = throwable;
            watch.stop();
            log.error("Invocation {} failed:", fullName);
            log.error("Exception: ", throwable);
        }

        log.info(DURAT_TEMPLATE,
                 watch.toString(),
                 fullName);
        log.info(ENDED_TEMPLATE,
                 DateFormatUtils.ISO_TIME_NO_T_FORMAT.format(Calendar.getInstance()),
                 fullName);

        // Need to rethrow original exception
        if (t != null) {
            throw t;
        }

        return result;
    }
}
