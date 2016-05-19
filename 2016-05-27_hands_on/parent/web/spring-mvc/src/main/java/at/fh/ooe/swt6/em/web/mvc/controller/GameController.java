package at.fh.ooe.swt6.em.web.mvc.controller;

import at.fh.ooe.swt6.em.logic.api.GameLogic;
import at.fh.ooe.swt6.em.model.jpa.model.Game;
import at.fh.ooe.swt6.em.model.jpa.model.Team;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Thomas on 5/16/2016.
 */
@Controller
public class GameController implements Serializable {


    @Inject
    private GameLogic gameLogic;

    @RequestMapping("/listGames")
    public String listGames() {
        final List<Game> games = gameLogic.findAllGames();
        gameLogic.saveTeam(new Team("MyTeam"));
        // return the path to the xhtml which is supposed to be rendered
        throw new IllegalArgumentException("My exception thrown");
//        return "index.xhtml";
    }
}
