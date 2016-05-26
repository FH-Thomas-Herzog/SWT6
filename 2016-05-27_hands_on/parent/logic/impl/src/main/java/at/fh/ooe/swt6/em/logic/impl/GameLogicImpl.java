package at.fh.ooe.swt6.em.logic.impl;

import at.fh.ooe.swt6.em.data.dao.api.GameDao;
import at.fh.ooe.swt6.em.data.dao.api.TeamDao;
import at.fh.ooe.swt6.em.data.dao.api.UserDao;
import at.fh.ooe.swt6.em.logic.api.GameLogic;
import at.fh.ooe.swt6.em.model.jpa.model.Game;
import at.fh.ooe.swt6.em.model.jpa.model.Team;
import at.fh.ooe.swt6.em.model.jpa.model.Tip;
import at.fh.ooe.swt6.em.model.view.team.GameView;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional(propagation = Propagation.REQUIRED)
public class GameLogicImpl implements GameLogic {

    @Inject
    private GameDao gameDao;
    @Inject
    private UserDao userDao;
    @Inject
    private TeamDao teamDao;


    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
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
            final GameView view = new GameView(game.getTeam1().getName(),
                                               game.getTeam2().getName(),
                                               game.getGoalsTeam1(),
                                               game.getGoalsTeam2(),
                                               (tipMap.containsKey(game.getTeam1()) ? tipMap.get(game.getTeam1())
                                                                                            .size() : Integer.valueOf(0)),
                                               (tipMap.containsKey(game.getTeam2()) ? tipMap.get(game.getTeam2())
                                                                                            .size() : Integer.valueOf(0)),
                                               (tipMap.containsKey(eventTeam) ? tipMap.get(eventTeam)
                                                                                      .size() : Integer.valueOf(0)),
                                               total.intValue(),
                                               game.gameDate);
            view.setId(game.getId());
            view.setVersion(game.getVersion());
            views.add(view);
        }

        views.sort((o1, o2) -> {
            int result = 0;
            if ((result = o2.getGameDate().compareTo(o1.getGameDate())) == 0) {
                Integer highGoalsO1 = (o1.getGoalsTeam1() > o1.getGoalsTeam2()) ? o1.getGoalsTeam1() : o1.getGoalsTeam2();
                Integer highGoalsO2 = (o2.getGoalsTeam1() > o2.getGoalsTeam2()) ? o2.getGoalsTeam1() : o2.getGoalsTeam2();
                if ((highGoalsO1.compareTo(highGoalsO2)) == 0) {
                    if ((result = o1.getTeam1Name().compareTo(o2.getTeam1Name())) == 0) {
                        if ((result = o1.getTeam2Name().compareTo(o2.getTeam2Name())) == 0) {
                            result = o1.getId().compareTo(o2.getId());
                        }
                    }
                }
            }
            return result;
        });

        return views;
    }

    @Override
    public Game saveGame(final Game _game) {
        Objects.requireNonNull(_game, "Cannot save null game");
        Objects.requireNonNull(_game.getTeam1(), "Team1 must be set");
        Objects.requireNonNull(_game.getTeam2(), "Team2 must be set");
        Objects.requireNonNull(_game.getTeam1().getId(), "Team1#id must be set");
        Objects.requireNonNull(_game.getTeam2().getId(), "Team2#id must be set");

        final Game gameDB;
        // Persist game
        if (_game.getId() == null) {
            final Team team1 = teamDao.findOne(_game.getTeam1().getId());
            final Team team2 = teamDao.findOne(_game.getTeam2().getId());
            if ((team1 == null) || (team2 == null)) {
                throw new RuntimeException("One or both teams are not set");
            }
            _game.setTeam1(team1);
            _game.setTeam2(team2);
            gameDB = gameDao.save(_game);
            team1.getGamesAsTeam1().add(gameDB);
            team2.getGamesAsTeam2().add(gameDB);
        }
        // Update game
        else {
            gameDB = gameDao.findOne(_game.getId());
            gameDB.setGoalsTeam1(_game.getGoalsTeam1());
            gameDB.setGoalsTeam2(_game.getGoalsTeam2());
            gameDB.setGameDate(_game.getGameDate());
        }

        return gameDao.save(gameDB);
    }

    @Override
    public List<Game> saveGames(List<Game> games) {
        Objects.requireNonNull(games, "Cannot save null games");
        if (games.isEmpty()) {
            return Collections.emptyList();
        }

        return games.stream().map(game -> saveGame(game)).collect(Collectors.toList());
    }

    @Override
    public void delete(long id) {
        final Game game = gameDao.findOne(id);
        if (game == null) {
            throw new RuntimeException("Game not found for id");
        }
        gameDao.delete(id);
    }
}
