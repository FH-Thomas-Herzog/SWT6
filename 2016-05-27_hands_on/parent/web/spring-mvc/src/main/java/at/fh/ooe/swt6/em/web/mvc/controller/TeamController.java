package at.fh.ooe.swt6.em.web.mvc.controller;

import at.fh.ooe.swt6.em.data.dao.api.TeamDao;
import at.fh.ooe.swt6.em.logic.api.TeamLogic;
import at.fh.ooe.swt6.em.model.jpa.model.Team;
import at.fh.ooe.swt6.em.web.mvc.api.PageDefinition;
import at.fh.ooe.swt6.em.web.mvc.app.constants.ControllerConstants;
import at.fh.ooe.swt6.em.web.mvc.app.constants.SessionHelper;
import at.fh.ooe.swt6.em.web.mvc.app.constants.pages.TeamEditPageDefinition;
import at.fh.ooe.swt6.em.web.mvc.app.constants.pages.TeamPageDefinition;
import at.fh.ooe.swt6.em.web.mvc.model.TeamEditModel;
import at.fh.ooe.swt6.em.web.mvc.model.TeamSessionModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.validation.Valid;
import java.io.Serializable;

/**
 * Created by Thomas on 5/16/2016.
 */
@Controller
public class TeamController implements Serializable {

    //<editor-fold desc="Inner Types">
    public static final class TeamControllerActions {
        public static final String PREFIX = "/teams";
        public static final String NEW = PREFIX + "/new";
        public static final String EDIT = PREFIX + "/edit";
        public static final String BACK = PREFIX + "/back";
        public static final String INDEX = PREFIX + "/index";
        public static final String SAVE = PREFIX + "/create";
        public static final String DELETE = PREFIX + "/delete";
    }
    //</editor-fold>

    //<editor-fold desc="Injections Logic">
    @Inject
    private TeamLogic teamLogic;
    @Inject
    private TeamDao teamDao;
    //</editor-fold>

    //<editor-fold desc="Injections Web">
    @Inject
    private TeamPageDefinition teamDefinition;
    @Inject
    private TeamEditPageDefinition teamEditDefinition;
    @Inject
    private SessionHelper sessionHelper;
    //</editor-fold>

    //<editor-fold desc="Controller Methods">

    /**
     * Start of this page. Loads whole html
     *
     * @return the ModelAndView instance
     */
    @RequestMapping(value = TeamControllerActions.INDEX, method = RequestMethod.GET)
    public ModelAndView index() {
        sessionHelper.setCurrentView(teamDefinition);
        sessionHelper.setAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name, new TeamSessionModel());

        return new ModelAndView(ControllerConstants.PAGE_MAIN, "models", teamLogic.findAllWithGameStatistics());
    }

    /**
     * Switches back to index content
     *
     * @return the ModelAndView instance
     */
    @RequestMapping(value = TeamControllerActions.BACK, method = RequestMethod.GET)
    public ModelAndView backFromForm() {
        sessionHelper.setCurrentView(teamDefinition);

        // Clear TeamSessionModel
        sessionHelper.getAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name, TeamSessionModel.class).clear();

        return new ModelAndView(teamDefinition.getContentFragment(),
                                "models",
                                teamLogic.findAllWithGameStatistics());
    }

    /**
     * Prepares the form for creating a new team
     *
     * @return the ModelAndView instance
     */
    @RequestMapping(value = TeamControllerActions.NEW, method = RequestMethod.GET)
    public ModelAndView newTeam() {
        final TeamSessionModel sessionModel = sessionHelper.getAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name,
                                                                         TeamSessionModel.class);
        final TeamEditModel model = new TeamEditModel();
        model.fromEntity(new Team());

        final ModelAndView modelAndview = new ModelAndView(teamEditDefinition.getContentFragment(),
                                                           "editModel",
                                                           model);

        // Only if we switch to form
        if (sessionHelper.getCurrentView().equals(teamDefinition)) {
            sessionHelper.setCurrentView(teamEditDefinition);
        }

        modelAndview.addObject("createdModels", sessionModel.getCreatedTeams());

        return modelAndview;
    }

    /**
     * Prepares the form for editing a team
     *
     * @return the ModelAndView instance
     */
    @RequestMapping(value = TeamControllerActions.EDIT, method = RequestMethod.GET)
    public ModelAndView editTeam(@RequestParam("id") final Long id) {
        final TeamSessionModel sessionModel = sessionHelper.getAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name,
                                                                         TeamSessionModel.class);

        final Team team = teamDao.findOne(id);
        final TeamEditModel model = new TeamEditModel();
        model.fromEntity(team);

        final ModelAndView modelAndView = new ModelAndView(teamEditDefinition.getContentFragment(),
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

    /**
     * Saves the team and returns to team form
     *
     * @return the ModelAndView instance
     */
    @RequestMapping(value = TeamControllerActions.SAVE, method = RequestMethod.POST)
    public ModelAndView saveTeam(@Valid TeamEditModel model) {
        final TeamSessionModel sessionModel = sessionHelper.getAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name,
                                                                         TeamSessionModel.class);

        final Team team = teamLogic.save(model.toEntity());
        model.fromEntity(team);
        sessionModel.addTeam(team);

        final ModelAndView modelAndview = new ModelAndView(teamEditDefinition.getContentFragment(),
                                                           "editModel",
                                                           model);
        modelAndview.addObject("createdModels", sessionModel.getCreatedTeams());

        return modelAndview;
    }

    /**
     * Deletes the team and returns to proper page
     *
     * @return the ModelAndView instance
     */
    @RequestMapping(value = TeamControllerActions.DELETE, method = RequestMethod.POST)
    public ModelAndView deleteTeam(@RequestParam("id") Long id) {
        final TeamSessionModel sessionModel = sessionHelper.getAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name,
                                                                         TeamSessionModel.class);

        teamLogic.delete(id);

        final PageDefinition currentPage = sessionHelper.getCurrentView();
        // We are on form
        if (currentPage.equals(teamEditDefinition)) {
            final TeamEditModel model = new TeamEditModel();
            model.fromEntity(new Team());
            final ModelAndView modelAndView = new ModelAndView(teamEditDefinition.getContentFragment(),
                                                               "editModel",
                                                               model);
            sessionModel.removeTeam(id);
            modelAndView.addObject("createdModels", sessionModel.getCreatedTeams());
            return modelAndView;
        }
        // We are on index
        else if (currentPage.equals(teamDefinition)) {
            return new ModelAndView(teamDefinition.getContentFragment(),
                                    "models",
                                    teamLogic.findAllWithGameStatistics());
        } else {
            throw new IllegalStateException("Current view: " + currentPage.getTemplate() + " not managed here");
        }
    }
    //</editor-fold>

    //<editor-fold desc="Model Attributes">
    //</editor-fold>


}
