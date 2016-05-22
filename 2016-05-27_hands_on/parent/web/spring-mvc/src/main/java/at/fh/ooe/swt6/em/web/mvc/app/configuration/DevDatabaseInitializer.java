package at.fh.ooe.swt6.em.web.mvc.app.configuration;

import at.fh.ooe.swt6.em.data.dao.api.GameDao;
import at.fh.ooe.swt6.em.data.dao.api.TeamDao;
import at.fh.ooe.swt6.em.logic.api.GameLogic;
import at.fh.ooe.swt6.em.logic.api.TeamLogic;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import javax.inject.Inject;
import javax.inject.Named;

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
//        List<Team> teams = teamDao.findAll();


    }
}
