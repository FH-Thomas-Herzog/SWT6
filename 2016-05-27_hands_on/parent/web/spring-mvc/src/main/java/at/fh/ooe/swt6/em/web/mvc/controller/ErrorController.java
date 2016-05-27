package at.fh.ooe.swt6.em.web.mvc.controller;

import at.fh.ooe.swt6.em.web.mvc.api.PageDefinition;
import at.fh.ooe.swt6.em.web.mvc.app.constants.SessionHelper;
import at.fh.ooe.swt6.em.web.mvc.model.SessionModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import javax.inject.Inject;

/**
 * Created by Thomas on 5/27/2016.
 */
@Controller
public class ErrorController {

    @Inject
    private SessionHelper sessionHelper;

    //<editor-fold desc="Inner Types">
    public static final class ErrorControllerActions {
        public static final String PREFIX = "/error";
        public static final String BACK_FORMER = PREFIX + "/back/former";
        public static final String BACK_HOME = PREFIX + "/back/home";
    }

    //</editor-fold>
    @RequestMapping(path = ErrorControllerActions.BACK_FORMER, method = RequestMethod.GET)
    public RedirectView backToFormer() {
        final PageDefinition definition = sessionHelper.getFormerView();
        if (definition != null) {
            sessionHelper.setCurrentView(definition);
        }

        final SessionModel model = sessionHelper.getAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name,
                                                              SessionModel.class);
        if (model != null) {
            return new RedirectView(model.getErrorAction());
        }

        return new RedirectView(GameController.GameControllerActions.INDEX);
    }

    @RequestMapping(path = ErrorControllerActions.BACK_HOME, method = RequestMethod.GET)
    public RedirectView backToHome() {
        return new RedirectView(GameController.GameControllerActions.INDEX);
    }
}
