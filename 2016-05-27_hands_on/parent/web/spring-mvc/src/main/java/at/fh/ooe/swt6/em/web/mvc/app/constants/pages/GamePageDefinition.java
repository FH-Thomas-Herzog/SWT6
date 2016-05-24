package at.fh.ooe.swt6.em.web.mvc.app.constants.pages;

import at.fh.ooe.swt6.em.web.mvc.api.PageDefinition;
import at.fh.ooe.swt6.em.web.mvc.controller.GameController;
import lombok.Getter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.Objects;

/**
 * Created by Thomas on 5/22/2016.
 */
@Named("gamePageDefinition")
@ApplicationScoped
public class GamePageDefinition implements PageDefinition {

    @Getter
    private String template = "games/games";
    @Getter
    private String actionIndex = GameController.GameControllerActions.INDEX;
    @Getter
    private String actionNew = GameController.GameControllerActions.NEW;
    @Getter
    private String actionEdit = GameController.GameControllerActions.EDIT;
    @Getter
    private String actionDelete = GameController.GameControllerActions.DELETE;
    @Getter
    private String teamContentFragment = template + "::content";
    @Getter
    private String titleKey = "games";

    public GamePageDefinition() {
    }

    @Override
    public String toActionUrl(String name) {
        return GameController.GameControllerActions.PREFIX + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GamePageDefinition that = (GamePageDefinition) o;
        return Objects.equals(template, that.template);
    }

    @Override
    public int hashCode() {
        return Objects.hash(template);
    }
}
