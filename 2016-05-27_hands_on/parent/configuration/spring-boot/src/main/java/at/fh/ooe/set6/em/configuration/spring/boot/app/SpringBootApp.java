package at.fh.ooe.set6.em.configuration.spring.boot.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Thomas on 5/15/2016.
 */
@SpringBootApplication
public class SpringBootApp {

    public static final String PROFILE_DEV = "DEV";
    public static final String PROFILE_PROD = "PROD";
    /**
     * Starts the spring boot application with the given arguments
     *
     * @param args
     */
    public static void main(String args[]) {
        SpringApplication.run(SpringBootApp.class, args);
    }
}
