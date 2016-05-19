package at.fh.ooe.set6.em.configuration.spring.boot.app;

import at.fh.ooe.set6.em.configuration.spring.boot.aop.AspectDurationLogger;
import at.fh.ooe.swt6.em.data.dao.api.UserDao;
import at.fh.ooe.swt6.em.logic.api.GameLogic;
import at.fh.ooe.swt6.em.logic.impl.GameLogicImpl;
import at.fh.ooe.swt6.em.model.jpa.model.User;
import at.fh.ooe.swt6.em.web.mvc.aop.advice.AdviceController;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by Thomas on 5/15/2016.
 */
// This is our dev configuration
@Configuration()
@Profile(SupportedProfile.DEV)
// Do not name it like application-dev.xml because is considered to be properties defined via xml and not context xml
@ImportResource("classpath:application-context-dev.xml")
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
        UserDao.class,
        GameLogic.class,
        GameLogicImpl.class,
        AspectDurationLogger.class,
        AdviceController.class
})
public class ConfigurationDevelopment {
}
