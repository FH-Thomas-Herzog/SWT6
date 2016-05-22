package at.fh.ooe.swt6.em.web.mvc.controller;

import at.fh.ooe.swt6.em.logic.api.TeamLogic;
import at.fh.ooe.swt6.em.model.view.team.TeamView;
import at.fh.ooe.swt6.em.web.mvc.app.constants.ControllerConstants;
import at.fh.ooe.swt6.em.web.mvc.app.constants.SessionHelper;
import at.fh.ooe.swt6.em.web.mvc.app.constants.pages.TeamPageDefinition;
import org.springframework.stereotype.Controller;
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

    public class TeamControllerActions {
        public static final String PREFIX = "/teams";
        public static final String INDEX = PREFIX + "/index";
        public static final String SAVE = PREFIX + "/save";
    }

    @Inject
    private TeamLogic teamLogic;

    @Inject
    private TeamPageDefinition teamDefinition;
    @Inject
    private SessionHelper sessionResolver;

    @RequestMapping(value = TeamControllerActions.INDEX, method = RequestMethod.GET)
    public ModelAndView index() {
        sessionResolver.setCurrentView(teamDefinition);
        final ModelAndView model = new ModelAndView(ControllerConstants.PAGE_MAIN);
        final List<TeamView> teams = teamLogic.findAllWithGameStatistics();
        model.addObject("models", teams);

        return model;
    }
}
