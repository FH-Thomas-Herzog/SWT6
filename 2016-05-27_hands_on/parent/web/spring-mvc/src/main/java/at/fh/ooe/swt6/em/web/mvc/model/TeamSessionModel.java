package at.fh.ooe.swt6.em.web.mvc.model;

import at.fh.ooe.swt6.em.model.jpa.model.Team;
import at.fh.ooe.swt6.em.model.view.team.TeamView;
import lombok.NoArgsConstructor;

import java.util.Comparator;

/**
 * Created by Thomas on 5/23/2016.
 */
@NoArgsConstructor
public class TeamSessionModel extends SessionModel<Long, Team, TeamView> {

    private static final Comparator<TeamView> TEAM_COMPARATOR = (o1, o2) ->
    {
        int result = 0;
        if ((o1.getName() == null) || ((result = o1.getName().compareTo(o2.getName())) == 0)) {
            result = o1.getId().compareTo(o2.getId());
        }
        return result;
    };

    @Override
    public TeamView createViewFromEntity(Team entity) {
        final TeamView view = new TeamView();
        view.setId(entity.getId());
        view.setName(entity.getName());

        return view;
    }

    @Override
    public TeamView createViewFromId(Long id) {
        return new TeamView(id);
    }

    @Override
    public Comparator<TeamView> createComparator() {
        return TEAM_COMPARATOR;
    }

    public TeamSessionModel(String errorAction,
                            String method) {
        super(errorAction, method);
    }
}
