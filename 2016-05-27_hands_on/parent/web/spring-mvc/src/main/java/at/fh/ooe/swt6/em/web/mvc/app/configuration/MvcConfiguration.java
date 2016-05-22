package at.fh.ooe.swt6.em.web.mvc.app.configuration;

import at.fh.ooe.swt6.em.model.view.team.TeamView;
import at.fh.ooe.swt6.em.web.mvc.app.constants.ResourceHelper;
import at.fh.ooe.swt6.em.web.mvc.controller.TeamController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * Created by Thomas on 5/22/2016.
 */
@Configuration
@ComponentScan(basePackageClasses = {
        TeamController.class,
        ResourceHelper.class,
        TeamView.class,
        DevDatabaseInitializer.class
}, scopeResolver = CdiScopeMetaDataResolver.class)
public class MvcConfiguration extends WebMvcConfigurerAdapter {

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/static/"};

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
}
