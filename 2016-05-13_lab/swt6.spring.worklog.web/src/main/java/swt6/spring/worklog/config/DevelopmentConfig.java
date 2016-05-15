package swt6.spring.worklog.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by Thomas on 5/13/2016.
 */
@Configuration
// defines profile name, used for enabling configuration if this profile is set
@Profile("dev")
public class DevelopmentConfig {

    @Bean
    public CommandLineRunner getDatabaseInitializer() {
        return new DatabaseInitializer();
    }
}
