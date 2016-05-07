package swt6.spring.basics.aop.test;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import swt6.spring.basics.aop.advice.TraceOptions;
import swt6.spring.basics.aop.logic.EmployeeIdNotFoundException;
import swt6.spring.basics.aop.logic.WorkLogFacade;

public class AopTest {

  public static void reflectClass(Class<?> clazz) {
    System.out.println("class=" + clazz.getName());
    Class<?>[] interfaces = clazz.getInterfaces();
    for (Class<?> itf : interfaces) {
      System.out.println("  implements " + itf.getName());
    }
  }

  private static void testAOP(String configFileName) {
    try (AbstractApplicationContext factory = new ClassPathXmlApplicationContext(configFileName)) {

      System.out.println("----------- TraceAdvice -----------");

      WorkLogFacade workLog = factory.getBean("workLog", WorkLogFacade.class);
      reflectClass(workLog.getClass());

      // ((TraceOptions)workLog).disableTracing();
      ((TraceOptions) workLog).enableTracing();

      try {
        workLog.findAllEmployees();
        for (int i = 1; i <= 4; i++) {
          workLog.findEmployeeById(Long.valueOf(i));
        }
      } catch (EmployeeIdNotFoundException e) {
      }
    }
  }

  public static void main(String[] args) {
    System.out.println("=============== testAOP (config based) ===============");
    testAOP("swt6/spring/basics/aop/test/applicationContext-config-based-aop.xml");

    System.out.println("============= testAOP (annotation based) =============");
     testAOP("swt6/spring/basics/aop/test/applicationContext-annotation-based-aop.xml");
  }
}
