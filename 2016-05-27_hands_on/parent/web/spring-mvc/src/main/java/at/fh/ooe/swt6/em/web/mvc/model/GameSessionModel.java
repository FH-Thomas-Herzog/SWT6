package at.fh.ooe.swt6.em.web.mvc.model;

import at.fh.ooe.swt6.em.model.jpa.model.Game;
import at.fh.ooe.swt6.em.model.view.team.GameView;

import java.util.Comparator;

/**
 * Created by Thomas on 5/23/2016.
 */
public class GameSessionModel extends SessionModel<Long, Game, GameView> {

    private static final Comparator<GameView> GAME_COMPARATOR = (o1, o2) -> {
        int result = 0;
        // If one name set both are, if not bot are not
        if (o1.getTeam1Name() != null) {
            if ((result = o1.getTeam1Name().compareTo(o2.getTeam1Name())) == 0) {
                if ((result = o1.getTeam2Name().compareTo(o2.getTeam2Name())) == 0) {
                    result = o1.getId().compareTo(o2.getId());
                }
            }
        }
        return result;
    };

    @Override
    public GameView createViewFromEntity(Game entity) {
        final GameView view = new GameView();
        view.setId(entity.getId());
        view.setTeam1Name(entity.getTeam1().getName());
        view.setTeam2Name(entity.getTeam2().getName());
        view.setGameDate(entity.getGameDate());
        view.setGoalsTeam1(entity.getGoalsTeam1());
        view.setGoalsTeam2(entity.getGoalsTeam2());

        return view;
    }

    @Override
    public GameView createViewFromId(Long id) {
        return new GameView(id);
    }

    @Override
    public Comparator<GameView> createComparator() {
        return GAME_COMPARATOR;
    }

    public GameSessionModel(String errorAction,
                            String method) {
        super(errorAction, method);
    }
}
