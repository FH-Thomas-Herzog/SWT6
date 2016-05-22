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

    public String getCssMain() {
        return locationFor(Resource.CSS_MAIN);
    }

    public String getJsUtil() {
        return locationFor(Resource.JS_UTIL);
    }

    private String locationFor(final Resource resource) {
        return resource.location;
    }

    public enum Resource {
        ROOT_WEB_JAR("/webjars"),
        ROOT_JS("/static/js"),
        ROOT_CSS("/static/css"),
        ROOT_TEMPLATE("/static/fragments"),
        ROOT_BOOTSTRAP(ROOT_WEB_JAR.location + "/bootstrap"),
        ROOT_CSS_BOOTSTRAP(ROOT_BOOTSTRAP.location + "/css"),
        ROOT_JS_BOOTSTRAP(ROOT_BOOTSTRAP.location + "/js"),
        ROOT_JQUERY(ROOT_WEB_JAR.location + "/jquery"),
        JS_JQUERY(ROOT_JQUERY.location + "/jquery.min.js"),
        JS_BOOTSTRAP(ROOT_JS_BOOTSTRAP.location + "/bootstrap.min.js"),
        CSS_BOOTSTRAP(ROOT_CSS_BOOTSTRAP.location + "/bootstrap.min.css"),
        CSS_MAIN(ROOT_CSS.location + "/main.css"),
        JS_UTIL(ROOT_JS.location + "/util.js");

        public final String location;

        Resource(String location) {
            this.location = location;
        }

        public String getLocation() {
            return location;
        }
    }
}
