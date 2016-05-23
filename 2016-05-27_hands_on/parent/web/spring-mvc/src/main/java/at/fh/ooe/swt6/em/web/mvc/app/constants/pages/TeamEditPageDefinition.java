package at.fh.ooe.swt6.em.web.mvc.app.constants.pages;

import at.fh.ooe.swt6.em.web.mvc.api.PageDefinition;
import at.fh.ooe.swt6.em.web.mvc.controller.TeamController;
import lombok.Getter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.Objects;

/**
 * Created by Thomas on 5/22/2016.
 */
@Named("teamEditPageDefinition")
@ApplicationScoped
public class TeamEditPageDefinition implements PageDefinition {

    @Getter
    private String template = "teams/team_edit";
    @Getter
    private String actionNew = TeamController.TeamControllerActions.NEW;
    @Getter
    private String actionEdit = TeamController.TeamControllerActions.EDIT;
    @Getter
    private String actionBack = TeamController.TeamControllerActions.BACK;
    @Getter
    private String actionSave = TeamController.TeamControllerActions.SAVE;
    @Getter
    private String actionDelete = TeamController.TeamControllerActions.DELETE;
    @Getter
    private String teamEditContentFragment = getTemplate() + "::" + "content";
    @Getter
    private String titleKey = "teams.form";

    public TeamEditPageDefinition() {
    }

    @Override
    public String toActionUrl(String name) {
        return TeamController.TeamControllerActions.PREFIX + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamEditPageDefinition that = (TeamEditPageDefinition) o;
        return Objects.equals(template, that.template);
    }

    @Override
    public int hashCode() {
        return Objects.hash(template);
    }
}
