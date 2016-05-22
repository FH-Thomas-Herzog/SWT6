package at.fh.ooe.swt6.em.logic.impl;

import at.fh.ooe.swt6.em.data.dao.api.TeamDao;
import at.fh.ooe.swt6.em.logic.api.TeamLogic;
import at.fh.ooe.swt6.em.model.jpa.model.Game;
import at.fh.ooe.swt6.em.model.jpa.model.Team;
import at.fh.ooe.swt6.em.model.view.team.TeamView;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Thomas on 5/19/2016.
 */
@Named
public class TeamLogicImpl implements TeamLogic {

    @Inject
    private TeamDao teamDao;

    private static final BiFunction<Game, Boolean, Team> TEAM_FOR_GOALS_MAPPER = (game, winner) -> {
        if (game.getGoalsTeam2().equals(game.getGoalsTeam1())) {
            return null;
        }
        if (game.getGoalsTeam2()
                .compareTo(game.getGoalsTeam2()) > 0) {
            return (winner) ? game.getTeam2() : game.getTeam1();
        } else {
            return (winner) ? game.getTeam1() : game.getTeam2();
        }
    };

    @Override
    public Team save(String name) {
        Objects.requireNonNull(name, "A teams must have a name");
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("String name is empty");
        }

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
    public List<TeamView> findAllWithGameStatistics() {
        final List<Team> teams = teamDao.findAll();
        final List<Game> allGames = teams.stream()
                                         .flatMap(item -> Stream.concat(item.getGamesAsTeam1().stream(),
                                                                        item.getGamesAsTeam2().stream()))
                                         .collect(Collectors.toList());

        final Map<Team, List<Game>> winnerMap = allGames.stream()
                                                        .filter(item -> item.getGoalsTeam2()
                                                                            .equals(item.getGoalsTeam2()))
                                                        .collect(Collectors.groupingBy(item -> TEAM_FOR_GOALS_MAPPER.apply(
                                                                item,
                                                                Boolean.TRUE)));
        final Map<Team, List<Game>> loserMap = allGames.stream()
                                                       .filter(item -> item.getGoalsTeam2()
                                                                           .equals(item.getGoalsTeam2()))
                                                       .collect(Collectors.groupingBy(item -> TEAM_FOR_GOALS_MAPPER.apply(
                                                               item,
                                                               Boolean.FALSE)));
        final Map<Team, Long> equalGameMap = teams.stream()
                                                  .collect(Collectors.toMap(item -> item,
                                                                            item -> Stream.concat(item.getGamesAsTeam1()
                                                                                                      .stream(),
                                                                                                  item.getGamesAsTeam2()
                                                                                                      .stream())
                                                                                          .filter(game -> game.getGoalsTeam1()
                                                                                                              .equals(game.getGoalsTeam2()))
                                                                                          .count()));

        return teams.stream()
                    .map(team -> new TeamView(team.getName(),
                                              (winnerMap.containsKey(team) ? winnerMap.get(team).size() : 0L),
                                              (loserMap.containsKey(team) ? loserMap.get(team).size() : 0L),
                                              equalGameMap.get(team))).collect(Collectors.toList());
    }
}
