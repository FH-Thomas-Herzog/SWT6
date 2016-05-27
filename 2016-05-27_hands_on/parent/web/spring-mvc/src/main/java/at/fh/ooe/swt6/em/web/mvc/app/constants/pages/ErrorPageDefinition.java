package at.fh.ooe.swt6.em.web.mvc.app.constants.pages;

import at.fh.ooe.swt6.em.web.mvc.api.PageDefinition;
import at.fh.ooe.swt6.em.web.mvc.controller.ErrorController;
import at.fh.ooe.swt6.em.web.mvc.controller.TipController;
import lombok.Getter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.Objects;

/**
 * Created by Thomas on 5/22/2016.
 */
@Named("errorPageDefinition")
@ApplicationScoped
public class ErrorPageDefinition implements PageDefinition {

    @Getter
    private String template = "error";
    @Getter
    private String actionBackToFormer = ErrorController.ErrorControllerActions.BACK_FORMER;
    @Getter
    private String actionBackToHome = ErrorController.ErrorControllerActions.BACK_HOME;
    @Getter
    private String contentFragment = getTemplate() + "::" + "content";
    @Getter
    private String titleKey = "error";

    public ErrorPageDefinition() {
    }

    @Override
    public String toActionUrl(String name) {
        return TipController.TipControllerActions.PREFIX + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorPageDefinition that = (ErrorPageDefinition) o;
        return Objects.equals(template, that.template);
    }

    @Override
    public int hashCode() {
        return Objects.hash(template);
    }
}
