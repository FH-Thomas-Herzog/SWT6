package at.fh.ooe.swt6.em.logic.api;

import at.fh.ooe.swt6.em.model.jpa.model.Game;
import at.fh.ooe.swt6.em.model.view.team.GameView;

import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;


/**
 * Created by Thomas on 5/16/2016.
 */
public interface GameLogic extends Serializable {

    List<GameView> findAllGames(Predicate<Game> filter);

    Game saveGame(Game games);

    List<Game> saveGames(List<Game> games);

    void delete(long  id);
}
