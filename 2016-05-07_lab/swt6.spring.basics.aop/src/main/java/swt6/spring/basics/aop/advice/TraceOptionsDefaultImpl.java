package swt6.spring.basics.aop.advice;

public class TraceOptionsDefaultImpl implements TraceOptions {

    private boolean isTracingEnabled = false;

    @Override
    public boolean isTraceingEnabled() {
        return isTracingEnabled;
    }

    @Override
    public void enableTracing() {
        isTracingEnabled = true;
    }

    @Override
    public void disableTracing() {
        isTracingEnabled = false;
    }
}
