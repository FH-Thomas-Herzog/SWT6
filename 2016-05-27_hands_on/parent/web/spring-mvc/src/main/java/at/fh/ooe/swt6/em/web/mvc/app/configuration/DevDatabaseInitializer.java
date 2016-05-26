package at.fh.ooe.swt6.em.web.mvc.app.configuration;

import at.fh.ooe.swt6.em.data.dao.api.GameDao;
import at.fh.ooe.swt6.em.data.dao.api.TeamDao;
import at.fh.ooe.swt6.em.logic.api.GameLogic;
import at.fh.ooe.swt6.em.logic.api.TeamLogic;
import at.fh.ooe.swt6.em.model.jpa.model.Game;
import at.fh.ooe.swt6.em.model.jpa.model.Team;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 5/19/2016.
 */
@Named
public class DevDatabaseInitializer implements ApplicationRunner {

    @Inject
    private TeamDao teamDao;
    @Inject
    private GameDao gameDao;
    @Inject
    private TeamLogic teamLogic;
    @Inject
    private GameLogic gameLogic;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        teamLogic.save("Bayern München",
                       "Austria Wien",
                       "FC Wolfsberg",
                       "FC Madrid",
                       "Schalke",
                       "FC Klagenfurt");
        List<Team> teams = teamDao.findAll();

        final List<Game> games = new ArrayList<>();
        games.add(new Game(2, 4, LocalDateTime.now(), teams.get(0), teams.get(1)));
        games.add(new Game(5, 3, LocalDateTime.now(), teams.get(0), teams.get(2)));
        games.add(new Game(1, 6, LocalDateTime.now(), teams.get(0), teams.get(3)));
        games.add(new Game(0, 2, LocalDateTime.now(), teams.get(0), teams.get(4)));
        games.add(new Game(6, 0, LocalDateTime.now(), teams.get(0), teams.get(5)));

        games.add(new Game(4, 4, LocalDateTime.now(), teams.get(1), teams.get(2)));
        games.add(new Game(5, 2, LocalDateTime.now(), teams.get(1), teams.get(3)));
        games.add(new Game(3, 3, LocalDateTime.now(), teams.get(1), teams.get(4)));
        games.add(new Game(5, 1, LocalDateTime.now(), teams.get(1), teams.get(5)));

        gameLogic.saveGames(games);
    }
}
