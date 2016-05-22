package at.fh.ooe.swt6.em.web.mvc.app.constants.pages;

import at.fh.ooe.swt6.em.web.mvc.api.PageDefinition;
import at.fh.ooe.swt6.em.web.mvc.controller.TeamController;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * Created by Thomas on 5/22/2016.
 */
@Named("teamPageDefinition")
@ApplicationScoped
public class TeamPageDefinition implements PageDefinition {

    private String template = "teams/teams";


    public TeamPageDefinition() {
    }

    @Override
    public String getTemplate() {
        return template;
    }

    @Override
    public String getTitleKey() {
        return "teams";
    }

    @Override
    public String toActionUrl(String name) {
        return TeamController.TeamControllerActions.PREFIX + name;
    }

    public String getActionIndex() {
        return TeamController.TeamControllerActions.INDEX;
    }

    public String getActionSave() {
        return TeamController.TeamControllerActions.SAVE;
    }
}
