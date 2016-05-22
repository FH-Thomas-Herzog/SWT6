package at.fh.ooe.swt6.em.web.mvc.controller;

import at.fh.ooe.swt6.em.logic.api.TeamLogic;
import at.fh.ooe.swt6.em.model.view.team.TeamView;
import at.fh.ooe.swt6.em.web.mvc.app.constants.ControllerConstants;
import at.fh.ooe.swt6.em.web.mvc.app.constants.SessionHelper;
import at.fh.ooe.swt6.em.web.mvc.app.constants.pages.TeamEditPageDefinition;
import at.fh.ooe.swt6.em.web.mvc.app.constants.pages.TeamPageDefinition;
import at.fh.ooe.swt6.em.web.mvc.model.TeamEditModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Thomas on 5/16/2016.
 */
@Controller
public class TeamController implements Serializable {

    //<editor-fold desc="Inner Types">
    public static final class TeamControllerActions {
        public static final String PREFIX = "/teams";
        public static final String NEW = PREFIX + "/new";
        public static final String BACK = PREFIX + "/back";
        public static final String INDEX = PREFIX + "/index";
        public static final String SAVE = PREFIX + "/save";
        public static final String DELETE = PREFIX + "/delete";
    }
    //</editor-fold>

    //<editor-fold desc="Injections Logic">
    @Inject
    private TeamLogic teamLogic;
    //</editor-fold>

    //<editor-fold desc="Injections Web">
    @Inject
    private TeamPageDefinition teamDefinition;
    @Inject
    private TeamEditPageDefinition teamEditDefinition;
    @Inject
    private SessionHelper sessionResolver;
    //</editor-fold>

    //<editor-fold desc="Controller Methods">

    /**
     * Start of this page. Loads whole html
     *
     * @return the ModelAndView instance
     */
    @RequestMapping(value = TeamControllerActions.INDEX, method = RequestMethod.GET)
    public ModelAndView index() {
        sessionResolver.setCurrentView(teamDefinition);
        return new ModelAndView(ControllerConstants.PAGE_MAIN);
    }

    /**
     * Prepares the form for creating a new team
     *
     * @return the ModelAndView instance
     */
    @RequestMapping(value = TeamControllerActions.NEW, method = RequestMethod.GET)
    public ModelAndView newTeam() {
        sessionResolver.setCurrentView(teamEditDefinition);

        return new ModelAndView(teamEditDefinition.getTeamEditorFragment(), "editModel", new TeamEditModel());
    }

    /**
     * Switches back to index content
     *
     * @return the ModelAndView instance
     */
    @RequestMapping(value = TeamControllerActions.BACK, method = RequestMethod.GET)
    public ModelAndView back() {
        sessionResolver.setCurrentView(teamDefinition);

        return new ModelAndView(teamDefinition.getTeamContentFragment());
    }

    /**
     * Saves the team and returns to team form
     *
     * @return the ModelAndView instance
     */
    @RequestMapping(value = TeamControllerActions.SAVE, method = RequestMethod.POST)
    public ModelAndView saveTeam(@ModelAttribute("editModel") TeamEditModel model) {
        sessionResolver.setCurrentView(teamEditDefinition);

        // TODO: Create or Update and reload newly created or updated team
        return new ModelAndView(teamEditDefinition.getTeamEditorFragment(), "editModel", model);
    }
    //</editor-fold>

    //<editor-fold desc="Model Attributes">
    @ModelAttribute("models")
    public List<TeamView> getModels() {
        return teamLogic.findAllWithGameStatistics();
    }
    //</editor-fold>


}
