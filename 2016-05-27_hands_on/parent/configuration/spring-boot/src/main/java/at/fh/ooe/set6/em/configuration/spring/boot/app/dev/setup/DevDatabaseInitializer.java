package at.fh.ooe.set6.em.configuration.spring.boot.app.dev.setup;

import at.fh.ooe.swt6.em.logic.api.TeamLogic;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import javax.inject.Inject;

/**
 * Created by Thomas on 5/19/2016.
 */
//@Named
public class DevDatabaseInitializer implements ApplicationRunner {

    @Inject
    private TeamLogic teamLogic;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        teamLogic.save("Bayern München",
                       "Austria Wien",
                       "FC Wolfsberg",
                       "FC Madrid",
                       "Schalke",
                       "FC Klagenfurt");
    }
}
