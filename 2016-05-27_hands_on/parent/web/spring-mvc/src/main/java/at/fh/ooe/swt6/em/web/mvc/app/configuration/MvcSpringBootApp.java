package at.fh.ooe.swt6.em.web.mvc.app.configuration;

import at.fh.ooe.swt6.em.data.dao.api.TeamDao;
import at.fh.ooe.swt6.em.data.dao.api.UserDao;
import at.fh.ooe.swt6.em.logic.impl.TeamLogicImpl;
import at.fh.ooe.swt6.em.model.jpa.model.User;
import at.fh.ooe.swt6.em.model.view.team.TeamView;
import at.fh.ooe.swt6.em.web.mvc.aop.advice.AdviceController;
import at.fh.ooe.swt6.em.web.mvc.app.constants.ResourceHelper;
import at.fh.ooe.swt6.em.web.mvc.controller.TeamController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

import java.util.Locale;

/**
 * Created by Thomas on 5/22/2016.
 */
@SpringBootApplication
@Configuration
@EnableAutoConfiguration
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
        TeamController.class,
        ResourceHelper.class,
        TeamView.class,
        DevDatabaseInitializer.class,
        AdviceController.class
}, scopeResolver = CdiScopeMetaDataResolver.class)
public class MvcSpringBootApp extends WebMvcConfigurerAdapter {

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/static/"};

    @Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }

    @Bean
    @Scope("session")
    public LocaleResolver producerLocaleResolver() {
        final SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        registry.addResourceHandler("/static/**/*").addResourceLocations(
                CLASSPATH_RESOURCE_LOCATIONS);

    }

    /**
     * Starts the spring boot application with the given arguments
     *
     * @param args
     */
    public static void main(String args[]) {
        SpringApplication.run(MvcSpringBootApp.class, args);
    }
}
