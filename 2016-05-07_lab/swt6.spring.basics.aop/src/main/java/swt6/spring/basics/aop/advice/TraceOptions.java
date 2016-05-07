package swt6.spring.basics.aop.advice;

public interface TraceOptions {
  boolean isTraceingEnabled();

  void enableTracing();

  void disableTracing();
}
