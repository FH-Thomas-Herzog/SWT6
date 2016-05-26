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
@Named("gameTipPageDefinition")
@ApplicationScoped
public class GameTipEditPageDefinition implements PageDefinition {

    @Getter
    private String template = "games/game_tip";
    @Getter
    private String actionNew = GameController.GameControllerActions.NEW_TIP;
    @Getter
    private String actionSave = GameController.GameControllerActions.SAVE_TIP;
    @Getter
    private String actionDelete = GameController.GameControllerActions.DELETE_TIP;
    @Getter
    private String actionBack = GameController.GameControllerActions.BACK;
    @Getter
    private String contentFragment = getTemplate() + "::" + "content";
    @Getter
    private String titleKey = "games.tip";

    public GameTipEditPageDefinition() {
    }

    @Override
    public String toActionUrl(String name) {
        return GameController.GameControllerActions.PREFIX + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameTipEditPageDefinition that = (GameTipEditPageDefinition) o;
        return Objects.equals(template, that.template);
    }

    @Override
    public int hashCode() {
        return Objects.hash(template);
    }
}
