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
@Named("teamPageDefinition")
@ApplicationScoped
public class TeamPageDefinition implements PageDefinition {

    @Getter
    private String template = "teams/teams";
    @Getter
    private String actionIndex = TeamController.TeamControllerActions.INDEX;
    @Getter
    private String actionNew = TeamController.TeamControllerActions.NEW;
    @Getter
    private String actionEdit = TeamController.TeamControllerActions.EDIT;
    @Getter
    private String actionDelete = TeamController.TeamControllerActions.DELETE;
    @Getter
    private String teamContentFragment = template + "::content";
    @Getter
    private String titleKey = "teams";

    public TeamPageDefinition() {
    }

    @Override
    public String toActionUrl(String name) {
        return TeamController.TeamControllerActions.PREFIX + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamPageDefinition that = (TeamPageDefinition) o;
        return Objects.equals(template, that.template);
    }

    @Override
    public int hashCode() {
        return Objects.hash(template);
    }
}
