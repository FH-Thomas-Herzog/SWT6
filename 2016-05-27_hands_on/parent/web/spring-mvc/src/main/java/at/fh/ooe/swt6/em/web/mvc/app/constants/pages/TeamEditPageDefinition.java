package at.fh.ooe.swt6.em.web.mvc.app.constants.pages;

import at.fh.ooe.swt6.em.web.mvc.api.PageDefinition;
import at.fh.ooe.swt6.em.web.mvc.controller.TeamController;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * Created by Thomas on 5/22/2016.
 */
@Named("teamEditPageDefinition")
@ApplicationScoped
public class TeamEditPageDefinition implements PageDefinition {

    private String template = "teams/team_edit";


    public TeamEditPageDefinition() {
    }

    @Override
    public String getTemplate() {
        return template;
    }

    @Override
    public String getTitleKey() {
        return "teams.form";
    }

    @Override
    public String toActionUrl(String name) {
        return TeamController.TeamControllerActions.PREFIX + name;
    }

    public String getActionBack() {
        return TeamController.TeamControllerActions.BACK;
    }

    public String getActionNew() {
        return TeamController.TeamControllerActions.NEW;
    }

    public String getActionSave() {
        return TeamController.TeamControllerActions.SAVE;
    }

    public String getActionDelete() {
        return TeamController.TeamControllerActions.DELETE;
    }

    public String getTeamEditorFragment() {
        return getTemplate() + "::" + "team-editor";
    }
}
