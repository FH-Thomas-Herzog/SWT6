package at.fh.ooe.set6.em.configuration.spring.boot.app;

import at.fh.ooe.swt6.em.data.dao.api.UserDao;
import at.fh.ooe.swt6.em.logic.api.GameLogic;
import at.fh.ooe.swt6.em.model.jpa.model.User;
import at.fh.ooe.swt6.logic.impl.GameLogicImpl;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by Thomas on 5/15/2016.
 */
// This is our dev configuration
@Configuration()
@Profile(SupportedProfile.DEV)
// Enables transaction management for this configuration
@EnableTransactionManagement
// Scans for entities
@EntityScan(basePackageClasses = User.class)
// JPA repositories
@EnableJpaRepositories(basePackageClasses = UserDao.class)
// Scan for injectable components
@ComponentScan(basePackageClasses = {
        UserDao.class,
        GameLogic.class,
        GameLogicImpl.class
})
public class ConfigurationDevelopment {
}
