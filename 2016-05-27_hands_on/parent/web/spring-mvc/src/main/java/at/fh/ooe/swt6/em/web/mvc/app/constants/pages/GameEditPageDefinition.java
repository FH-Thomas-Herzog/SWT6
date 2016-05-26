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
@Named("gamesEditPageDefinition")
@ApplicationScoped
public class GameEditPageDefinition implements PageDefinition {

    @Getter
    private String template = "games/game_edit";
    @Getter
    private String actionNew = GameController.GameControllerActions.NEW;
    @Getter
    private String actionEdit = GameController.GameControllerActions.EDIT;
    @Getter
    private String actionBack = GameController.GameControllerActions.BACK;
    @Getter
    private String actionSave = GameController.GameControllerActions.SAVE;
    @Getter
    private String actionDelete = GameController.GameControllerActions.DELETE;
    @Getter
    private String contentFragment = getTemplate() + "::" + "content";
    @Getter
    private String titleKey = "games.form";

    public GameEditPageDefinition() {
    }

    @Override
    public String toActionUrl(String name) {
        return GameController.GameControllerActions.PREFIX + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameEditPageDefinition that = (GameEditPageDefinition) o;
        return Objects.equals(template, that.template);
    }

    @Override
    public int hashCode() {
        return Objects.hash(template);
    }
}
