package swt6.spring.basics.aop.advice;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

public class TraceAdviceConfigBased {

    private boolean isTracingEnabledInProxy(JoinPoint jp) {
        return ((TraceOptions) jp.getThis()).isTraceingEnabled();
    }

    public void traceBefore(JoinPoint jp) {
        if (isTracingEnabledInProxy(jp)) {
            // Get proxied type with called method signature
            final String methodName = jp.getTarget().getClass().getName() + "." + jp.getSignature();
            System.out.println("--> " + methodName);
        }
    }

    public void traceAfter(JoinPoint jp) {
        if (isTracingEnabledInProxy(jp)) {
            String methodName = jp.getTarget().getClass().getName() + "." + jp.getSignature().getName();
            System.out.println("<-- " + methodName);
        }
    }

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

    public void traceException(JoinPoint jp,
                               Throwable exception) {
        if (isTracingEnabledInProxy(jp)) {
            String methodName = jp.getTarget().getClass().getName() + "." + jp.getSignature().getName();
            System.out.printf("##> %s%n  threw exception <%s>%n", methodName, exception);
        }
    }
}
