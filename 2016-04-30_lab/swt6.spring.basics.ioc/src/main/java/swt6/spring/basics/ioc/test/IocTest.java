package swt6.spring.basics.ioc.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import swt6.spring.basics.ioc.logic.WorkLogFacade;
import swt6.spring.basics.ioc.logic.WorkLogImplFactoryBased;

public class IocTest {

    private static void testSimple() {
        WorkLogImplFactoryBased workLog = new WorkLogImplFactoryBased();
        workLog.findAllEmployees();
        workLog.findEmployeeById(3L);
        workLog.findEmployeeById(99L);
    }

    private static void testConfigBasedIoC() {
        try (AbstractApplicationContext factory =
                     new ClassPathXmlApplicationContext(
                             "swt6/spring/basics/ioc/test/applicationContext-config-based-ioc.xml")) {

            // Injection performed via Setter method
            System.out.println("***> workLog-setter-injected:");
            WorkLogFacade worklog1 = factory.getBean("worklog-setter-injected", WorkLogFacade.class);
            worklog1.findAllEmployees();
            worklog1.findEmployeeById(1L);

            // Injection performed via constructor
            System.out.println("***> workLog-constructor-injected:");
            WorkLogFacade worklog2 = factory.getBean("worklog-constructor-injected", WorkLogFacade.class);
            worklog2.findAllEmployees();
            worklog2.findEmployeeById(1L);

            System.out.println("***> workLog-autowired:");
            WorkLogFacade worklog3 = factory.getBean("worklog-autowired", WorkLogFacade.class);
            worklog3.findAllEmployees();
            worklog3.findEmployeeById(3L);
        }
    }

    private static void testAnnoationBasedIoC() {
        try (AbstractApplicationContext factory = new ClassPathXmlApplicationContext(
                "swt6/spring/basics/ioc/test/applicationContext-annotation-based-ioc.xml")) {
            WorkLogFacade worklog1 = factory.getBean("worklog", WorkLogFacade.class);
            worklog1.findAllEmployees();
            worklog1.findEmployeeById(3L);
        }
    }

    private static void testJavaconfig() {
        try(AbstractApplicationContext factory = new AnnotationConfigApplicationContext(WorklogConfig.class)){
            WorkLogFacade worklog1 = factory.getBean(WorkLogFacade.class);
            worklog1.findAllEmployees();
            worklog1.findEmployeeById(3L);
        }
    }

    public static void main(String[] args) {
        System.out.println("==================== testSimple ======================");
        testSimple();

        System.out.println("================= testConfigBasedIoC =================");
        testConfigBasedIoC();

        System.out.println("================ testAnnoationBasedIoC ===============");
        testAnnoationBasedIoC();

        System.out.println("================ testJavaconfig =====================");
        testJavaconfig();
    }
}
