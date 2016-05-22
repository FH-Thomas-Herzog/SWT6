package at.fh.ooe.swt6.em.web.mvc.app.constants;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by Thomas on 5/22/2016.
 */
@Named("resourceHelper")
@ApplicationScoped
public class ResourceHelper implements Serializable {

    public String getJsJquery() {
        return locationFor(Resource.JS_JQUERY);
    }

    public String getJsBootstrap() {
        return locationFor(Resource.JS_BOOTSTRAP);
    }

    public String getCssBootstrap() {
        return locationFor(Resource.CSS_BOOTSTRAP);
    }

    private String locationFor(final Resource resource) {
        return resource.location;
    }

    public enum Resource {
        ROOT_WEB_JAR("/webjars"),
        ROOT_JS("/META-INF/resources/static/js"),
        ROOT_CSS("/META-INF/resources/static/css"),
        ROOT_TEMPLATE("/META-INF/resources/static/fragments"),
        ROOT_BOOTSTRAP(ROOT_WEB_JAR.location + "/bootstrap"),
        ROOT_CSS_BOOTSTRAP(ROOT_BOOTSTRAP.location + "/css"),
        ROOT_JS_BOOTSTRAP(ROOT_BOOTSTRAP.location + "/js"),
        ROOT_JQUERY(ROOT_WEB_JAR.location + "/jquery"),
        JS_JQUERY(ROOT_JQUERY.location + "/jquery.min.js"),
        JS_BOOTSTRAP(ROOT_JS_BOOTSTRAP.location + "/bootstrap.min.js"),
        CSS_BOOTSTRAP(ROOT_CSS_BOOTSTRAP.location + "/bootstrap.min.css");

        public final String location;

        Resource(String location) {
            this.location = location;
        }

        public String getLocation() {
            return location;
        }
    }
}
