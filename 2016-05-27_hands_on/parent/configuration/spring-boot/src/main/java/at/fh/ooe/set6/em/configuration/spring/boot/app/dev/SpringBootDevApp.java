package at.fh.ooe.set6.em.configuration.spring.boot.app.dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Thomas on 5/15/2016.
 */
@SpringBootApplication
public class SpringBootDevApp {

    /**
     * Starts the spring boot application with the given arguments
     *
     * @param args
     */
    public static void main(String args[]) {
        SpringApplication.run(SpringBootDevApp.class, args);
    }
}
