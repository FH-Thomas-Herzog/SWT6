package at.fh.ooe.swt6.em.web.spring.mvc.controller;

import at.fh.ooe.swt6.em.logic.api.GameLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.Serializable;

/**
 * Created by Thomas on 5/16/2016.
 */
@Controller
public class GameController implements Serializable {

    @Autowired
    private GameLogic gameLogic;

    @RequestMapping("/listGames")
    public String listGames() {
        return "hello";
    }
}
