package at.fh.ooe.swt6.em.logic.api;

import at.fh.ooe.swt6.em.model.jpa.model.Team;
import at.fh.ooe.swt6.em.model.view.team.TeamView;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Thomas on 5/19/2016.
 */
public interface TeamLogic extends Serializable {

    Team save(Team team);

    Team save(String name);

    List<Team> save(String... names);

    void delete(long id);

    List<TeamView> findAllWithGameStatistics();
}
