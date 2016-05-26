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

    public String getJsJqueryValdiation() {
        return locationFor(Resource.JS_JQUERY_VALIDATION);
    }

    public String getCssMain() {
        return locationFor(Resource.CSS_MAIN);
    }

    public String getJsUtil() {
        return locationFor(Resource.JS_UTIL);
    }

    public String getJsDateTimePicker() {
        return locationFor(Resource.JS_BOOTSTRAP_DATETIME);
    }

    public String getCssDateTimePicker() {
        return locationFor(Resource.CSS_BOOTSTRAP_DATETIME);
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
        ROOT_JQUERY_VALIDATION(ROOT_WEB_JAR.location + "/jquery-validation"),
        JS_JQUERY(ROOT_WEB_JAR.location + "/jquery/jquery.min.js"),
        JS_BOOTSTRAP(ROOT_BOOTSTRAP.location + "/js/bootstrap.min.js"),
        CSS_BOOTSTRAP(ROOT_BOOTSTRAP.location + "/css/bootstrap.min.css"),
        CSS_MAIN(ROOT_CSS.location + "/main.css"),
        JS_UTIL(ROOT_JS.location + "/util.js"),
        JS_JQUERY_VALIDATION(ROOT_JQUERY_VALIDATION.location + "/jquery.validate.min.js"),
        JS_BOOTSTRAP_DATETIME("/static/datetime-picker/js/bootstrap-datetimepicker.min.js"),
        CSS_BOOTSTRAP_DATETIME("/static/datetime-picker/css/bootstrap-datetimepicker.min.css");

        public final String location;

        Resource(String location) {
            this.location = location;
        }

        public String getLocation() {
            return location;
        }
    }
}
