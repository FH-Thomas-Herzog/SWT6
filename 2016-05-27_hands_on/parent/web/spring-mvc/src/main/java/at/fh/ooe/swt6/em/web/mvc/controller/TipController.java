package at.fh.ooe.swt6.em.web.mvc.controller;

import at.fh.ooe.swt6.em.data.dao.api.GameDao;
import at.fh.ooe.swt6.em.logic.api.GameLogic;
import at.fh.ooe.swt6.em.logic.api.TipLogic;
import at.fh.ooe.swt6.em.logic.api.UserLogic;
import at.fh.ooe.swt6.em.model.jpa.model.Game;
import at.fh.ooe.swt6.em.model.jpa.model.Tip;
import at.fh.ooe.swt6.em.web.mvc.app.constants.ControllerConstants;
import at.fh.ooe.swt6.em.web.mvc.app.constants.SessionHelper;
import at.fh.ooe.swt6.em.web.mvc.app.constants.pages.TipEditPageDefinition;
import at.fh.ooe.swt6.em.web.mvc.app.constants.pages.TipPageDefinition;
import at.fh.ooe.swt6.em.web.mvc.model.SessionModel;
import at.fh.ooe.swt6.em.web.mvc.model.TipEntityEditModel;
import at.fh.ooe.swt6.em.web.mvc.model.TipSessionModel;
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
public class TipController implements Serializable {

    //<editor-fold desc="Inner Types">
    public static final class TipControllerActions {
        public static final String PREFIX = "/tips";
        public static final String INDEX = PREFIX + "/index";
        public static final String AJAX_PREFIX = PREFIX + ControllerConstants.AJAX_PATH;
        public static final String NEW = AJAX_PREFIX + "/new";
        public static final String SAVE = AJAX_PREFIX + "/create";
        public static final String DELETE = AJAX_PREFIX + "/delete";
    }
    //</editor-fold>

    //<editor-fold desc="Injections Logic">
    @Inject
    private GameLogic gameLogic;
    @Inject
    private TipLogic tipLogic;
    @Inject
    private GameDao gameDao;
    @Inject
    private UserLogic userLogic;
    //</editor-fold>

    //<editor-fold desc="Injections Web">
    @Inject
    private TipPageDefinition tipPageDefinition;
    @Inject
    private TipEditPageDefinition tipEditPageDefinition;
    @Inject
    private SessionHelper sessionHelper;
    //</editor-fold>

    //<editor-fold desc="Controller Methods">

    /**
     * start of this page. loads whole html
     *
     * @return the modelandview instance
     */
    @RequestMapping(value = TipControllerActions.INDEX, method = RequestMethod.GET)
    public ModelAndView index() {
        sessionHelper.setCurrentView(tipPageDefinition);
        sessionHelper.setAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name(),
                                   new TipSessionModel(tipPageDefinition.getActionIndex(), RequestMethod.GET.name()));


        return new ModelAndView(ControllerConstants.PAGE_MAIN, "models", userLogic.findAllUserScores());
    }

    /**
     * Prepares the form for creating a new tip
     *
     * @return the ModelAndView instance
     */
    @RequestMapping(value = TipControllerActions.NEW, method = RequestMethod.POST)
    public ModelAndView createNew(@RequestParam("id") Long id) {
        sessionHelper.setCurrentView(tipEditPageDefinition);
        SessionModel sessionModel = sessionHelper.getAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name,
                                                               SessionModel.class);
        // only if no error occurred before
        if ((sessionModel == null) || (!(sessionModel instanceof TipSessionModel)) || (!sessionModel.isError())) {
            sessionHelper.setAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name,
                                       new TipSessionModel());
            sessionModel = sessionHelper.getAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name,
                                                      SessionModel.class);
            setDefaultErrorHandling(sessionModel, id);
        }

        sessionModel.setError(Boolean.FALSE);

        final Game game = gameDao.findOne(id);
        final Tip tip = new Tip();
        tip.setGame(game);

        final TipEntityEditModel model = new TipEntityEditModel();
        model.fromEntity(tip);

        final ModelAndView modelAndview = new ModelAndView(tipEditPageDefinition.getContentFragment(),
                                                           "editModel",
                                                           model);

        modelAndview.addObject("createdModels", sessionModel.getViews());

        return modelAndview;
    }

    /**
     * Saves the users tip
     *
     * @return the ModelAndView instance
     */
    @RequestMapping(value = TipControllerActions.SAVE, method = RequestMethod.POST)
    public ModelAndView save(@Valid TipEntityEditModel model) {
        final TipSessionModel sessionModel = sessionHelper.getAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name,
                                                                        TipSessionModel.class);

        setDefaultErrorHandling(sessionModel, model.getGameId());

        final Tip tip = tipLogic.save(model.toEntity());
        model.fromEntity(tip);
        sessionModel.addNew(tip);

        final ModelAndView modelAndview = new ModelAndView(tipEditPageDefinition.getContentFragment(),
                                                           "editModel",
                                                           model);

        modelAndview.addObject("createdModels", sessionModel.getViews());

        return modelAndview;
    }

    /**
     * Deletes the users tip
     *
     * @return the ModelAndView instance
     */
    @RequestMapping(value = TipControllerActions.DELETE, method = RequestMethod.POST)
    public ModelAndView delete(@RequestParam("id") Long id,
                               @RequestParam("gameId") Long gameId) {
        final TipSessionModel sessionModel = sessionHelper.getAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name,
                                                                        TipSessionModel.class);

        setDefaultErrorHandling(sessionModel, gameId);

        tipLogic.delete(id);
        sessionModel.removeNew(id);

        final Game game = gameDao.findOne(gameId);
        final Tip tip = new Tip();
        tip.setGame(game);
        final TipEntityEditModel model = new TipEntityEditModel();
        model.fromEntity(tip);

        final ModelAndView modelAndview = new ModelAndView(tipEditPageDefinition.getContentFragment(),
                                                           "editModel",
                                                           model);

        modelAndview.addObject("createdModels", sessionModel.getViews());

        return modelAndview;
    }
    //</editor-fold>

    //<editor-fold desc="Private Helper">
    private void setDefaultErrorHandling(final SessionModel sessionModel,
                                         Long gameId) {
        sessionModel.setErrorAction(TipControllerActions.NEW + "?id=" + gameId);
        sessionModel.setErrorMethod(RequestMethod.POST.name());
        sessionModel.setError(Boolean.FALSE);
    }
    //</editor-fold>


}
