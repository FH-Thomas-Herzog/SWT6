package at.fh.ooe.set6.em.configuration.spring.boot.app;

import at.fh.ooe.swt6.em.model.jpa.model.User;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by Thomas on 5/15/2016.
 */
// Marks this class as and configuration
@Configuration()
// Enables transaction management for this configuration
@EnableTransactionManagement
// Scans for entities
@EntityScan(basePackageClasses = User.class)
// This is our dev configuration
@Profile(SupportedProfile.DEV)
public class DevelopmentConfiguration {
}
