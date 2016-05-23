package at.fh.ooe.swt6.em.web.mvc.model;

import at.fh.ooe.swt6.em.model.jpa.model.Team;
import at.fh.ooe.swt6.em.model.view.team.TeamView;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Thomas on 5/23/2016.
 */
public class TeamSessionModel implements Serializable {

    @Getter
    private SortedSet<TeamView> createdTeams;

    public TeamSessionModel() {
        clear();
    }

    public void addTeam(Team team) {
        Objects.requireNonNull(team, "Cannot null team team");
        final TeamView view = new TeamView();
        view.setId(team.getId());
        view.setName(team.getName());

        createdTeams.remove(view);
        createdTeams.add(view);
    }

    public void removeTeam(final long id) {
        createdTeams.remove(new TeamView(id));
    }

    public void clear() {
        createdTeams = new TreeSet<>((o1, o2) -> o1.getId().compareTo(o2.getId()));
    }
}
