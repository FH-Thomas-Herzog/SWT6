package at.fh.ooe.swt6.em.web.mvc.app.configuration;

import at.fh.ooe.swt6.em.data.dao.api.GameDao;
import at.fh.ooe.swt6.em.data.dao.api.TeamDao;
import at.fh.ooe.swt6.em.data.dao.api.UserDao;
import at.fh.ooe.swt6.em.logic.api.GameLogic;
import at.fh.ooe.swt6.em.logic.api.TeamLogic;
import at.fh.ooe.swt6.em.logic.api.TipLogic;
import at.fh.ooe.swt6.em.logic.api.UserLogic;
import at.fh.ooe.swt6.em.model.jpa.model.Game;
import at.fh.ooe.swt6.em.model.jpa.model.Team;
import at.fh.ooe.swt6.em.model.jpa.model.Tip;
import at.fh.ooe.swt6.em.model.jpa.model.User;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private TipLogic tipLogic;
    @Inject
    private UserDao userDao;
    @Inject
    private GameLogic gameLogic;
    @Inject
    private UserLogic userLogic;

    private final Random random = new Random();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        teamLogic.save("Bayern München",
                       "Austria Wien",
                       "FC Wolfsberg",
                       "FC Madrid",
                       "Schalke",
                       "FC Klagenfurt");
        userLogic.create("herzog.thomas81@gmail.com",
                         "t.herzog@curecompü.com",
                         "t_herzog@gmx.net");

        final List<Team> teams = teamDao.findAll();

        final List<Game> games = new ArrayList<>();
        final LocalDateTime gameDate = LocalDateTime.now();
        int day = 0;
        games.add(new Game(2, 4, LocalDateTime.of(2016, Month.JUNE, ++day, 12, 30), teams.get(0), teams.get(1)));
        games.add(new Game(5, 3, LocalDateTime.of(2016, Month.JUNE, ++day, 12, 30), teams.get(0), teams.get(2)));
        games.add(new Game(1, 6, LocalDateTime.of(2016, Month.JUNE, ++day, 12, 30), teams.get(0), teams.get(3)));
        games.add(new Game(0, 2, LocalDateTime.of(2016, Month.JUNE, ++day, 12, 30), teams.get(0), teams.get(4)));
        games.add(new Game(6, 0, LocalDateTime.of(2016, Month.JUNE, ++day, 12, 30), teams.get(0), teams.get(5)));
        games.add(new Game(4, 4, LocalDateTime.of(2016, Month.JUNE, ++day, 12, 30), teams.get(1), teams.get(2)));
        games.add(new Game(5, 2, LocalDateTime.of(2016, Month.JUNE, ++day, 12, 30), teams.get(1), teams.get(3)));
        games.add(new Game(3, 3, LocalDateTime.of(2016, Month.JUNE, ++day, 12, 30), teams.get(1), teams.get(4)));
        games.add(new Game(5, 1, LocalDateTime.of(2016, Month.JUNE, ++day, 12, 30), teams.get(1), teams.get(5)));
        games.add(new Game(5, 1, LocalDateTime.of(2016, Month.JUNE, ++day, 12, 30), teams.get(1), teams.get(0)));
        games.add(new Game(null, null, LocalDateTime.of(2016, Month.JUNE, ++day, 12, 30), teams.get(1), teams.get(2)));
        games.add(new Game(null, null, LocalDateTime.of(2016, Month.JUNE, ++day, 12, 30), teams.get(1), teams.get(3)));
        games.add(new Game(null, null, LocalDateTime.of(2016, Month.JUNE, ++day, 12, 30), teams.get(1), teams.get(4)));
        games.add(new Game(null, null, LocalDateTime.of(2016, Month.JUNE, ++day, 12, 30), teams.get(1), teams.get(5)));
        games.add(new Game(null, null, LocalDateTime.of(2016, Month.JUNE, ++day, 12, 30), teams.get(1), teams.get(0)));

        gameLogic.saveGames(games);

        final List<User> users = userDao.findAll();
        for (User user : users) {
            for (int i = 0; i < 10; i++) {
                final Game game = games.get(i);
                final Tip tip = new Tip();
                tip.setGame(game);
                tip.setUser(user);
                // won tips
                if (i < 5) {
                    tip.setTipGoalsTeam1(game.getGoalsTeam1());
                    tip.setTipGoalsTeam2(game.getGoalsTeam2());
                }
                // lost tips
                else if (i < 10) {
                    tip.setTipGoalsTeam1(game.getGoalsTeam1() + 1);
                    tip.setTipGoalsTeam2(game.getGoalsTeam2() + 1);
                }
                tipLogic.save(tip);
            }
        }
    }
}
