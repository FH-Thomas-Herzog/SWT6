package swt6.spring.basics.aop.advice;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TraceAdviceAnnotationBased {

    @DeclareParents(value = "swt6.spring.basics.aop.logic..*", defaultImpl = TraceOptionsDefaultImpl.class)
    private static TraceOptions mixin;

    private boolean isTracingEnabledInProxy(JoinPoint jp) {
        return ((TraceOptions) jp.getThis()).isTraceingEnabled();
    }

    @Before("execution(public * swt6.spring.basics.aop.logic..*find*(..))")
    public void traceBefore(JoinPoint jp) {
        if (isTracingEnabledInProxy(jp)) {
            String methodName = jp.getTarget().getClass().getName() + "." + jp.getSignature().getName();
            System.out.println("--> " + methodName);
        }
    }

    @AfterReturning("execution(public * swt6.spring.basics.aop.logic..*find*(..))")
    public void traceAfter(JoinPoint jp) {
        if (isTracingEnabledInProxy(jp)) {
            String methodName = jp.getTarget().getClass().getName() + "." + jp.getSignature().getName();
            System.out.println("<-- " + methodName);
        }
    }

    @Around("execution(public * swt6.spring.basics.aop.logic..*find*ById*(..))")
    public Object traceAround(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = pjp.getTarget().getClass().getName() + "." + pjp.getSignature().getName();

        if (isTracingEnabledInProxy(pjp)) {
            System.out.println("==> " + methodName);
        }
        Object retVal = pjp.proceed(); // delegates to method of target class.
        if (isTracingEnabledInProxy(pjp)) {
            System.out.println("<== " + methodName);
        }

        return retVal;
    }

    @AfterThrowing(pointcut = "execution(public * swt6.spring.basics.aop.logic..*find*(..))", throwing = "exception")
    public void traceException(JoinPoint jp,
                               Throwable exception) {
        if (isTracingEnabledInProxy(jp)) {
            String methodName = jp.getTarget().getClass().getName() + "." + jp.getSignature().getName();
            System.out.printf("##> %s%n  threw exception <%s>%n", methodName, exception);
        }
    }
}
