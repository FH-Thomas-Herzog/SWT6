package at.fh.ooe.swt6.em.logic.impl;

import at.fh.ooe.swt6.em.data.dao.api.GameDao;
import at.fh.ooe.swt6.em.data.dao.api.TeamDao;
import at.fh.ooe.swt6.em.data.dao.api.UserDao;
import at.fh.ooe.swt6.em.logic.api.GameLogic;
import at.fh.ooe.swt6.em.model.jpa.model.Game;
import at.fh.ooe.swt6.em.model.jpa.model.Team;
import at.fh.ooe.swt6.em.model.jpa.model.Tip;
import at.fh.ooe.swt6.em.model.view.team.GameView;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Replace @Transactional and there is no spring dependency anymore.
 * <p>
 * Created by Thomas on 5/16/2016.
 */
@Named
public class GameLogicImpl implements GameLogic {

    @Inject
    private GameDao gameDao;
    @Inject
    private UserDao userDao;
    @Inject
    private TeamDao teamDao;


    @Override
    public List<GameView> findAllGames() {
        final List<Game> games = gameDao.findAll();
        if (games.isEmpty()) {
            return Collections.emptyList();
        }

        final Team eventTeam = new Team(0L);
        final List<GameView> views = new ArrayList<>(games.size());
        // Map tip count to team
        for (Game game : games) {
            // Group tips per team
            final Map<Team, List<Tip>> tipMap = game.getTips().stream()
                                                    .collect(Collectors.groupingBy(tip -> {
                                                        if (tip.getTipGoalsTeam1()
                                                               .compareTo(tip.getTipGoalsTeam2()) > 0) {
                                                            return tip.getGame().getTeam1();
                                                        } else if (tip.getTipGoalsTeam1()
                                                                      .compareTo(tip.getTipGoalsTeam2()) < 0) {
                                                            return tip.getGame().getTeam2();
                                                        } else {
                                                            return eventTeam;
                                                        }
                                                    }));
            // get total tip count
            Long total = tipMap.entrySet().stream().map(entry -> entry.getValue().size()).count();
            // build view model
            views.add(new GameView(game.getId(),
                                   game.getTeam1().getName(),
                                   game.getTeam2().getName(),
                                   game.getGoalsTeam1(),
                                   game.getGoalsTeam2(),
                                   (tipMap.containsKey(game.getTeam1()) ? tipMap.get(game.getTeam1())
                                                                                .size() : Integer.valueOf(0)),
                                   (tipMap.containsKey(game.getTeam2()) ? tipMap.get(game.getTeam2())
                                                                                .size() : Integer.valueOf(0)),
                                   (tipMap.containsKey(eventTeam) ? tipMap.get(eventTeam).size() : Integer.valueOf(0)),
                                   total.intValue(),
                                   game.gameDate));
        }

        return views;
    }

    @Override
    public Game saveGame(Game game) {
        Objects.requireNonNull(game, "Cannot save null game");

        // TODO: Update check if in the past
        // TODO: Check if teams still exist

        return gameDao.save(game);
    }

    @Override
    public List<Game> saveGames(List<Game> games) {
        Objects.requireNonNull(games, "Cannot save null games");
        if (games.isEmpty()) {
            return Collections.emptyList();
        }

        return games.stream().map(game -> saveGame(game)).collect(Collectors.toList());
    }
}
