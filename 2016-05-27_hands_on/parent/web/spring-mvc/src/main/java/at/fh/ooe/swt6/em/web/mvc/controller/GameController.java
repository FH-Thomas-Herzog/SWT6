package at.fh.ooe.swt6.em.web.mvc.controller;

import at.fh.ooe.swt6.em.data.dao.api.GameDao;
import at.fh.ooe.swt6.em.data.dao.api.TeamDao;
import at.fh.ooe.swt6.em.logic.api.GameLogic;
import at.fh.ooe.swt6.em.model.jpa.model.Game;
import at.fh.ooe.swt6.em.model.view.team.TeamView;
import at.fh.ooe.swt6.em.web.mvc.api.PageDefinition;
import at.fh.ooe.swt6.em.web.mvc.app.constants.ControllerConstants;
import at.fh.ooe.swt6.em.web.mvc.app.constants.SessionHelper;
import at.fh.ooe.swt6.em.web.mvc.app.constants.pages.GameEditPageDefinition;
import at.fh.ooe.swt6.em.web.mvc.app.constants.pages.GamePageDefinition;
import at.fh.ooe.swt6.em.web.mvc.model.GameEditModel;
import at.fh.ooe.swt6.em.web.mvc.model.GameSessionModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Thomas on 5/16/2016.
 */
@Controller
public class GameController implements Serializable {

    //<editor-fold desc="Inner Types">
    public static final class GameControllerActions {
        public static final String PREFIX = "/games";
        public static final String NEW = PREFIX + "/new";
        public static final String EDIT = PREFIX + "/edit";
        public static final String BACK = PREFIX + "/back";
        public static final String INDEX = PREFIX + "/index";
        public static final String SAVE = PREFIX + "/save";
        public static final String DELETE = PREFIX + "/delete";
        public static final String TIP = PREFIX + "/tip";
    }
    //</editor-fold>

    //<editor-fold desc="Injections Logic">
    @Inject
    private GameLogic gameLogic;
    @Inject
    private GameDao gameDao;
    @Inject
    private TeamDao teamDao;
    //</editor-fold>

    //<editor-fold desc="Injections Web">
    @Inject
    private GamePageDefinition gameDefinition;
    @Inject
    private GameEditPageDefinition gameEditDefinition;
    @Inject
    private SessionHelper sessionHelper;
    //</editor-fold>

//    <editor-fold desc="Controller Methods">

    /**
     * Start of this page. Loads whole html
     *
     * @return the ModelAndView instance
     */
    @RequestMapping(value = GameControllerActions.INDEX, method = RequestMethod.GET)
    public ModelAndView index() {
        sessionHelper.setCurrentView(gameDefinition);
        sessionHelper.setAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name, new GameSessionModel());

        return new ModelAndView(ControllerConstants.PAGE_MAIN, "models", gameLogic.findAllGames());
    }

    /**
     * Switches back to index content
     *
     * @return the ModelAndView instance
     */
    @RequestMapping(value = GameControllerActions.BACK, method = RequestMethod.GET)
    public ModelAndView backToIndex() {
        sessionHelper.setCurrentView(gameDefinition);

        // Clear TeamSessionModel
        sessionHelper.getAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name, GameSessionModel.class)
                     .clear();

        return new ModelAndView(gameDefinition.getContentFragment(),
                                "models",
                                gameLogic.findAllGames());
    }


    /**
     * Prepares the form for creating a new team
     *
     * @return the ModelAndView instance
     */

    @RequestMapping(value = GameControllerActions.NEW, method = RequestMethod.GET)
    public ModelAndView newGame() {
        final GameSessionModel sessionModel = sessionHelper.getAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name,
                                                                         GameSessionModel.class);
        final GameEditModel model = new GameEditModel();
        model.fromEntity(new Game());

        final ModelAndView modelAndview = new ModelAndView(gameEditDefinition.getContentFragment(),
                                                           "editModel",
                                                           model);

        // Only if we switch to form
        if (sessionHelper.getCurrentView().equals(gameDefinition)) {
            sessionHelper.setCurrentView(gameEditDefinition);
        }

        modelAndview.addObject("createdModels", sessionModel.getViews());

        return modelAndview;
    }

    /**
     * Prepares the form for editing a game
     *
     * @return the ModelAndView instance
     */
    @RequestMapping(value = GameControllerActions.EDIT, method = RequestMethod.GET)
    public ModelAndView editTeam(@RequestParam("id") final Long id) {
        final GameSessionModel sessionModel = sessionHelper.getAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name,
                                                                         GameSessionModel.class);

        final Game game = gameDao.findOne(id);
        final GameEditModel model = new GameEditModel();
        model.fromEntity(game);

        final ModelAndView modelAndView = new ModelAndView(gameEditDefinition.getContentFragment(),
                                                           "editModel",
                                                           model);
        modelAndView.addObject("createdModels", sessionModel.getViews());

        // switch to form if invoked from index
        if (sessionHelper.getCurrentView().equals(gameDefinition)) {
            sessionHelper.setCurrentView(gameEditDefinition);
            sessionModel.addNew(game);
        }

        return modelAndView;
    }

    /**
     * Saves the game and returns to team form
     *
     * @return the ModelAndView instance
     */

    @RequestMapping(value = GameControllerActions.SAVE, method = RequestMethod.POST)
    public ModelAndView saveGame(@Valid GameEditModel model) {
        final GameSessionModel sessionModel = sessionHelper.getAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name,
                                                                         GameSessionModel.class);

        final Game game = gameLogic.saveGame(model.toEntity());
        model.fromEntity(game);
        sessionModel.addNew(game);

        final ModelAndView modelAndview = new ModelAndView(gameEditDefinition.getContentFragment(),
                                                           "editModel",
                                                           model);
        modelAndview.addObject("createdModels", sessionModel.getViews());

        return modelAndview;
    }


    /**
     * Deletes the game and returns to proper page
     *
     * @return the ModelAndView instance
     */

    @RequestMapping(value = GameControllerActions.DELETE, method = RequestMethod.POST)
    public ModelAndView deleteGame(@RequestParam("id") Long id) {
        final GameSessionModel sessionModel = sessionHelper.getAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name,
                                                                         GameSessionModel.class);

        gameLogic.delete(id);

        final PageDefinition currentPage = sessionHelper.getCurrentView();
        // We are on form
        if (currentPage.equals(gameEditDefinition)) {
            final GameEditModel model = new GameEditModel();
            model.fromEntity(new Game());
            final ModelAndView modelAndView = new ModelAndView(gameEditDefinition.getContentFragment(),
                                                               "editModel",
                                                               model);
            sessionModel.removeNew(id);
            modelAndView.addObject("createdModels", sessionModel.getViews());
            return modelAndView;
        }
        // We are on index
        else if (currentPage.equals(gameDefinition)) {
            return new ModelAndView(gameDefinition.getContentFragment(),
                                    "models",
                                    gameLogic.findAllGames());

        } else {
            throw new IllegalStateException("Current view: " + currentPage.getTemplate() + " not managed here");
        }
    }
    //</editor-fold>

    //<editor-fold desc="Model Attributes">
    @ModelAttribute("teamViews")
    public List<TeamView> getTeamViews() {
        return teamDao.findAllByOrderByNameAsc()
                      .stream()
                      .map(team -> new TeamView(team.getId(), team.getVersion(), team.getName()))
                      .collect(Collectors.toList());
    }
    //</editor-fold>


}
