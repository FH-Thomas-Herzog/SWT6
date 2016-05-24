package at.fh.ooe.swt6.em.web.mvc.controller;

import at.fh.ooe.swt6.em.data.dao.api.GameDao;
import at.fh.ooe.swt6.em.logic.api.GameLogic;
import at.fh.ooe.swt6.em.web.mvc.app.constants.ControllerConstants;
import at.fh.ooe.swt6.em.web.mvc.app.constants.SessionHelper;
import at.fh.ooe.swt6.em.web.mvc.app.constants.pages.GameEditPageDefinition;
import at.fh.ooe.swt6.em.web.mvc.app.constants.pages.GamePageDefinition;
import at.fh.ooe.swt6.em.web.mvc.model.TeamSessionModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.io.Serializable;

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
    }
    //</editor-fold>

    //<editor-fold desc="Injections Logic">
    @Inject
    private GameLogic gameLogic;
    @Inject
    private GameDao gameDao;
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
        sessionHelper.setAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name, new TeamSessionModel());

        return new ModelAndView(ControllerConstants.PAGE_MAIN, "models", gameLogic.findAllGames());
    }
/*

    */
/**
 * Switches back to index content
 *
 * @return the ModelAndView instance
 *//*

    @RequestMapping(value = GameControllerActions.BACK, method = RequestMethod.GET)
    public ModelAndView backFromForm() {
        sessionHelper.setCurrentView(teamDefinition);

        // Clear TeamSessionModel
        sessionHelper.getAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name, TeamSessionModel.class).clear();

        return new ModelAndView(teamDefinition.getTeamContentFragment(),
                                "models",
                                null);
    }

    */
/**
 * Prepares the form for creating a new team
 *
 * @return the ModelAndView instance
 *//*

    @RequestMapping(value = GameControllerActions.NEW, method = RequestMethod.GET)
    public ModelAndView newTeam() {
        final TeamSessionModel sessionModel = sessionHelper.getAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name,
                                                                         TeamSessionModel.class);
        final TeamEditModel model = new TeamEditModel();
        model.fromEntity(new Team());

        final ModelAndView modelAndview = new ModelAndView(teamEditDefinition.getTeamEditContentFragment(),
                                                           "editModel",
                                                           model);

        // Only if we switch to form
        if (sessionHelper.getCurrentView().equals(teamDefinition)) {
            sessionHelper.setCurrentView(teamEditDefinition);
        }

        modelAndview.addObject("createdModels", sessionModel.getCreatedTeams());

        return modelAndview;
    }

    */
/**
 * Prepares the form for editing a team
 *
 * @return the ModelAndView instance
 *//*

    @RequestMapping(value = GameControllerActions.EDIT, method = RequestMethod.GET)
    public ModelAndView editTeam(@RequestParam("id") final Long id) {
        final TeamSessionModel sessionModel = sessionHelper.getAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name,
                                                                         TeamSessionModel.class);

        final Team team = null;// teamDao.findOne(id);
        final TeamEditModel model = new TeamEditModel();
        model.fromEntity(team);

        final ModelAndView modelAndView = new ModelAndView(teamEditDefinition.getTeamEditContentFragment(),
                                                           "editModel",
                                                           model);
        modelAndView.addObject("createdModels", sessionModel.getCreatedTeams());

        // switch to form if invoked from index
        if (sessionHelper.getCurrentView().equals(teamDefinition)) {
            sessionHelper.setCurrentView(teamEditDefinition);
            sessionModel.addTeam(team);
        }

        return modelAndView;
    }

    */
/**
 * Saves the team and returns to team form
 *
 * @return the ModelAndView instance
 *//*

    @RequestMapping(value = GameControllerActions.SAVE, method = RequestMethod.POST)
    public ModelAndView saveGame(@Valid TeamEditModel model) {
        final TeamSessionModel sessionModel = sessionHelper.getAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name,
                                                                         TeamSessionModel.class);

        final Team team = null;//teamLogic.save(model.toEntity());
        model.fromEntity(team);
        sessionModel.addTeam(team);

        final ModelAndView modelAndview = new ModelAndView(teamEditDefinition.getTeamEditContentFragment(),
                                                           "editModel",
                                                           model);
        modelAndview.addObject("createdModels", sessionModel.getCreatedTeams());

        return modelAndview;
    }

    */
/**
 * Deletes the team and returns to proper page
 *
 * @return the ModelAndView instance
 *//*

    @RequestMapping(value = GameControllerActions.DELETE, method = RequestMethod.POST)
    public ModelAndView deleteTeam(@RequestParam("id") Long id) {
        final TeamSessionModel sessionModel = sessionHelper.getAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name,
                                                                         TeamSessionModel.class);

//        teamLogic.delete(id);

        final PageDefinition currentPage = sessionHelper.getCurrentView();
        // We are on form
        if (currentPage.equals(teamEditDefinition)) {
            final TeamEditModel model = new TeamEditModel();
            model.fromEntity(new Team());
            final ModelAndView modelAndView = new ModelAndView(teamEditDefinition.getTeamEditContentFragment(),
                                                               "editModel",
                                                               model);
            sessionModel.removeTeam(id);
            modelAndView.addObject("createdModels", sessionModel.getCreatedTeams());
            return modelAndView;
        }
        // We are on index
        else if (currentPage.equals(teamDefinition)) {
            return new ModelAndView(teamDefinition.getTeamContentFragment(),
                                    "models",
                                    null);//teamLogic.findAllWithGameStatistics());
        } else {
            throw new IllegalStateException("Current view: " + currentPage.getTemplate() + " not managed here");
        }
    }
    //</editor-fold>

    //<editor-fold desc="Model Attributes">
    //</editor-fold>
*/


}
