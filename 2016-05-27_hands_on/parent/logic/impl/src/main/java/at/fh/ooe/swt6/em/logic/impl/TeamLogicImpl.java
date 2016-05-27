package at.fh.ooe.swt6.em.logic.impl;

import at.fh.ooe.swt6.em.data.dao.api.TeamDao;
import at.fh.ooe.swt6.em.logic.api.TeamLogic;
import at.fh.ooe.swt6.em.logic.api.exception.LogicException;
import at.fh.ooe.swt6.em.model.jpa.model.Game;
import at.fh.ooe.swt6.em.model.jpa.model.Team;
import at.fh.ooe.swt6.em.model.view.team.TeamView;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Thomas on 5/19/2016.
 */
@Named
@Transactional(propagation = Propagation.REQUIRED)
public class TeamLogicImpl implements TeamLogic {

    @Inject
    private TeamDao teamDao;

    private static final BiFunction<Game, Boolean, Team> TEAM_FOR_GOALS_MAPPER = (game, winner) -> {
        if (game.getGoalsTeam1()
                .compareTo(game.getGoalsTeam2()) > 0) {
            return (winner) ? game.getTeam1() : game.getTeam2();
        } else {
            return new Team(0L);
        }
    };

    @Override
    public Team save(Team _team) {
        Objects.requireNonNull(_team, "Cannot create null team");
        Objects.requireNonNull(_team.getName(), "Cannot create team with null name");

        return teamDao.save(_team);
    }

    @Override
    public Team save(String name) {
        Objects.requireNonNull(name, "A teams must have a name");
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("String name is empty");
        }

        // TODO: Check for equal name on database

        return teamDao.save(new Team(name));
    }

    @Override
    public List<Team> save(String... names) {
        Objects.requireNonNull(names, "No names are given");
        if (names.length == 0) {
            throw new IllegalArgumentException("No names are given");
        }

        final List<Team> teams = new ArrayList<>(names.length);
        for (String name : names) {
            teams.add(save(name));
        }

        return teams;
    }

    @Override
    public void delete(long id) {
        final Team team = teamDao.findOne(id);
        if (team == null) {
            throw new LogicException("Team do delete not found", LogicException.ServiceCode.ENTITY_NOT_FOUND);
        }
        teamDao.delete(team);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<TeamView> findAllWithGameStatistics() {
        final List<Team> teams = teamDao.findAllByOrderByNameAsc();
        if (teams.isEmpty()) {
            Collections.emptyList();
        }

        final List<TeamView> views = new ArrayList<>(teams.size());
        // get all games of all teams
        final List<Game> games = teams.stream()
                                      .flatMap(item -> Stream.concat(item.getGamesAsTeam1().stream(),
                                                                     item.getGamesAsTeam2().stream()))
                                      .filter(game -> game.getGoalsTeam1() != null && game.getGoalsTeam2() != null)
                                      .collect(Collectors.toList());
        // map winner to games
        final Map<Team, List<Game>> teamWinnerMap = games.stream()
                                                         .filter(game -> !game.getGoalsTeam1()
                                                                              .equals(game.getGoalsTeam2()))
                                                         .collect(Collectors.groupingBy(game -> TEAM_FOR_GOALS_MAPPER.apply(
                                                                 game,
                                                                 Boolean.TRUE)));
        // map loser to games
        final Map<Team, List<Game>> teamLoserMap = games.stream()
                                                        .filter(game -> !game.getGoalsTeam1()
                                                                             .equals(game.getGoalsTeam2()))
                                                        .collect(Collectors.groupingBy(game -> TEAM_FOR_GOALS_MAPPER.apply(
                                                                game,
                                                                Boolean.FALSE)));
        // get even games
        final List<Game> evenGames = games.stream()
                                          .filter(game -> game.getGoalsTeam1().equals(game.getGoalsTeam2()))
                                          .collect(Collectors.toList());
        for (Team team : teams) {
            // get total even count for team
            final Long eventCount = evenGames.stream()
                                             .filter(game -> game.getTeam1().equals(team) || game.getTeam2()
                                                                                                 .equals(team))
                                             .count();
            // build view model
            final TeamView view = new TeamView(team.getName(),
                                               (teamWinnerMap.containsKey(team) ? teamWinnerMap.get(team).size() : 0),
                                               (teamLoserMap.containsKey(team) ? teamLoserMap.get(team).size() : 0),
                                               eventCount.intValue());
            view.setId(team.getId());
            view.setVersion(team.getVersion());

            views.add(view);
        }

        return views;
    }
}
