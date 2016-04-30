package swt6.spring.basic.hello;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Thomas on 4/30/2016.
 */
public class Main {

    public static void main(String args[]) {
        try (AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(
                "swt6/spring/basics/hello/greetingService.xml")) {
            // gets by name and type
            GreetingService service1 = ctx.getBean("greetingService", GreetingService.class);
            service1.sayHello();

            // works if bean is named
            GreetingService service2 = (GreetingService) ctx.getBean("greetingService");
            service2.sayHello();

            // works only for single implementation of this interface
            GreetingService service3 = ctx.getBean(GreetingService.class);
            service3.sayHello();
        }
    }
}
