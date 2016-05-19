package at.fh.ooe.set6.em.configuration.spring.boot.app;

import at.fh.ooe.swt6.em.web.mvc.controller.GameController;
import at.fh.ooe.swt6.em.web.mvc.model.MyModel;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;

/**
 * This configuration integrates JSF as view technology for spring mvc application.
 * <p>
 * http://docs.spring.io/spring-webflow/docs/current/reference/html/spring-faces.html
 * http://docs.spring.io/autorepo/docs/spring/3.2.x/spring-framework-reference/html/web-integration.html
 * http://www.mkyong.com/jsf2/jsf-2-0-spring-integration-example/
 * http://www.concretepage.com/spring-4/spring-4-jsf-2-integration-example-using-autowired-annotation
 * http://stackoverflow.com/questions/25479986/spring-boot-with-jsf-could-not-find-backup-for-factory-javax-faces-context-face
 * <p>
 * Created by Thomas on 5/16/2016.
 */
@Configuration
@Profile(SupportedProfile.DEV)
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = {
        GameController.class,
        MyModel.class
})
@AutoConfigureAfter(ConfigurationDevelopment.class)
public class ConfigurationJSFMvc implements ServletContextAware {

    /**
     * Register FacesServlet on xhtml suffix which are only accessible via internal controlles because
     * they reside in protected WEB-INF directory
     *
     * @return the customized servlet registration
     */
    @Bean
    public ServletRegistrationBean configureServletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(
                new FacesServlet(), "*.xhtml");
        registration.setLoadOnStartup(1);
        return registration;
    }

    /**
     * This custom setup resolver redirects to protected WEB-INF directory where our xhtml files reside.
     * The controllers only need to return proper path within this path relatively without the need to add the suffix.
     * E.g.: "/index" -> "/WEB-INF/jsf/index.xhtml"
     * So we are separated from the current used view technology
     *
     * @return the customized view resolver
     */
    @Bean
    public InternalResourceViewResolver internalResourceViewResolver() {
        final InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsf");
        resolver.setSuffix(".xhtml");

        return resolver;
    }

    /**
     * Setup ServletContext properly to integrate JSF as view technology
     *
     * @param ctx the servlet context to configure
     */
    @Override
    public void setServletContext(ServletContext ctx) {
        ctx.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());
        ctx.setInitParameter("javax.faces.DEFAULT_SUFFIX", ".xhtml");
        ctx.setInitParameter("javax.faces.PARTIAL_STATE_SAVING", "true");
        ctx.setInitParameter("javax.faces.STATE_SAVING_METHOD", "server");
        ctx.setInitParameter("javax.faces.PROJECT_STAGE", "Development");
        ctx.setInitParameter("javax.faces.FACELETS_REFRESH_PERIOD", "1");
        ctx.setInitParameter("facelets.DEVELOPMENT", "true");
    }
}
