package at.fh.ooe.set6.em.configuration.spring.boot.app.dev;

import at.fh.ooe.set6.em.configuration.spring.boot.app.api.SupportedProfile;
import at.fh.ooe.swt6.em.data.dao.api.TeamDao;
import at.fh.ooe.swt6.em.data.dao.api.UserDao;
import at.fh.ooe.swt6.em.logic.impl.TeamLogicImpl;
import at.fh.ooe.swt6.em.model.jpa.model.User;
import at.fh.ooe.swt6.em.web.mvc.app.configuration.MvcSpringBootApp;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by Thomas on 5/15/2016.
 */
// This is our dev configuration
@Configuration()
@EnableAutoConfiguration
@Profile(SupportedProfile.DEV)
// Do not name it like application-dev.xml because is considered to be properties defined via xml and not context xml
@ImportResource({"classpath:application-context-dev.xml"})
@PropertySource("application-dev.properties")
// Enables transaction management for this configuration
@EnableTransactionManagement
// Scans for entities
@EntityScan(basePackageClasses = User.class)
// JPA repositories
@EnableJpaRepositories(basePackageClasses = UserDao.class)
// Auto scan aspects
@EnableAspectJAutoProxy(proxyTargetClass = true)
// Scan for injectable components
@ComponentScan(basePackageClasses = {
        TeamDao.class,
        TeamLogicImpl.class,
        MvcSpringBootApp.class,
})
public class ConfigurationDev {

    // TODO: I think the fact that we use multiple maven modules causes the UnsatisfiedResolutionException to be thrown.
    // TODO: If we inject a bean defined in this artifact it works well, so bean scanning is the issue here
/*    @Bean
    public ApplicationRunner getDatabaseInitializer(TeamLogic l) {
        final DevDatabaseInitializer initializer = new DevDatabaseInitializer();
        initializer.setTeamLogic(l);
        return initializer;
    }*/
}
