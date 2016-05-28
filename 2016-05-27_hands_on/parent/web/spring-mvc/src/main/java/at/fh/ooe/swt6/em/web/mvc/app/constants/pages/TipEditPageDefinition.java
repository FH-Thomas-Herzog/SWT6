package at.fh.ooe.swt6.em.web.mvc.app.constants.pages;

import at.fh.ooe.swt6.em.web.mvc.api.PageDefinition;
import at.fh.ooe.swt6.em.web.mvc.controller.TipController;
import lombok.Getter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.Objects;

/**
 * Created by Thomas on 5/22/2016.
 */
@Named("tipEditPageDefinition")
@ApplicationScoped
public class TipEditPageDefinition implements PageDefinition {

    @Getter
    private String template = "tips/tip";
    @Getter
    private String actionIndex = TipController.TipControllerActions.INDEX;
    @Getter
    private String actionNew = TipController.TipControllerActions.NEW;
    @Getter
    private String actionSave = TipController.TipControllerActions.SAVE;
    @Getter
    private String actionDelete = TipController.TipControllerActions.DELETE;
    @Getter
    private String contentFragment = getTemplate() + "::" + "content";
    @Getter
    private String titleKey = "tips.tip";

    public TipEditPageDefinition() {
    }

    @Override
    public String toActionUrl(String name) {
        return TipController.TipControllerActions.PREFIX + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipEditPageDefinition that = (TipEditPageDefinition) o;
        return Objects.equals(template, that.template);
    }

    @Override
    public int hashCode() {
        return Objects.hash(template);
    }
}
