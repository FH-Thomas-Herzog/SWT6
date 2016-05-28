package at.fh.ooe.swt6.em.web.mvc.app.configuration;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationScopeMetadataResolver;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.context.annotation.ScopedProxyMode;

import java.util.Set;

/**
 * Integrates the JSR-330 annotations into spring with proper spring equivalent.
 * <p>
 * Created by Thomas on 5/22/2016.
 */
public class CdiScopeMetaDataResolver
        extends AnnotationScopeMetadataResolver {

    @Override
    public ScopeMetadata resolveScopeMetadata(BeanDefinition definition) {
        ScopeMetadata metadata = new ScopeMetadata();
        if (definition instanceof AnnotatedBeanDefinition) {
            AnnotatedBeanDefinition annDef = (AnnotatedBeanDefinition) definition;
            Set<String> annotationTypes = annDef.getMetadata().getAnnotationTypes();

            if (annotationTypes.contains(
                    javax.enterprise.context.RequestScoped.class.getName())
                    ) {
                metadata.setScopeName("request");
                // Need to mark used with proxies, otherwise autowire will fail if
                // @Request scoped beans is tried to be injected outside a existing context
                metadata.setScopedProxyMode(ScopedProxyMode.TARGET_CLASS);
            } else if (annotationTypes.contains(
                    javax.enterprise.context.SessionScoped.class.getName())
                    ) {
                metadata.setScopeName("session");
                // Need to mark used with proxies, otherwise autowire will fail if
                // @Request scoped beans is tried to be injected outside a existing context
                metadata.setScopedProxyMode(ScopedProxyMode.TARGET_CLASS);
            } else if (annotationTypes.contains(
                    javax.enterprise.context.ApplicationScoped.class.getName())
                    ) {
                metadata.setScopeName("singleton");
            } else {
                // do the regular Spring stuff..
                return super.resolveScopeMetadata(definition);
            }
        }
        return metadata;
    }
}
