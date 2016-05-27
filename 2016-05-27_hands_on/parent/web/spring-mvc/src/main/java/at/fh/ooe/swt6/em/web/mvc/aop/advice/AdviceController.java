package at.fh.ooe.swt6.em.web.mvc.aop.advice;

import at.fh.ooe.swt6.em.logic.api.exception.LogicException;
import at.fh.ooe.swt6.em.web.mvc.app.constants.ControllerConstants;
import at.fh.ooe.swt6.em.web.mvc.app.constants.SessionHelper;
import at.fh.ooe.swt6.em.web.mvc.app.constants.pages.ErrorPageDefinition;
import at.fh.ooe.swt6.em.web.mvc.model.ErrorModel;
import at.fh.ooe.swt6.em.web.mvc.model.SessionModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * Created by Thomas on 5/19/2016.
 */
@ControllerAdvice
@Slf4j
public class AdviceController implements Serializable {

    @Inject
    private SessionHelper sessionHelper;
    @Inject
    private ErrorPageDefinition errorPageDefinition;

    private enum ServiceCodeLocalization {
        ENTRY_NOT_FOUND(LogicException.ServiceCode.ENTITY_NOT_FOUND, "error.entry.notFound"),
        ENTRY_EXISTS(LogicException.ServiceCode.ENTITY_EXISTS, "error.entry.exists"),
        ACTION_INVALID(LogicException.ServiceCode.INVALID_ACTION, "error.action.invalid"),
        DATA_INVALID(LogicException.ServiceCode.INVALID_DATA, "error.data.invalid"),
        UNKNOWN(LogicException.ServiceCode.INVALID_DATA, "error.unknown");

        public final LogicException.ServiceCode code;
        public final String key;

        ServiceCodeLocalization(LogicException.ServiceCode code,
                                String key) {
            this.code = code;
            this.key = key;
        }

        public static ServiceCodeLocalization getForServiceCode(LogicException.ServiceCode code) {
            if (code != null) {
                for (ServiceCodeLocalization loc : ServiceCodeLocalization.values()) {
                    if (loc.code.equals(code)) {
                        return loc;
                    }
                }
            }

            return UNKNOWN;
        }
    }

    @ExceptionHandler(LogicException.class)
    public ModelAndView handleException(final HttpServletRequest request,
                                        final LogicException t) {
        log.error("LogicException occurred", t);
        sessionHelper.setCurrentView(errorPageDefinition);
        final SessionModel sessionModel = sessionHelper.getAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name,
                                                                     SessionModel.class);
        if (sessionModel != null) {
            sessionModel.setError(Boolean.TRUE);
        }

        ServiceCodeLocalization localization;
        localization = ServiceCodeLocalization.getForServiceCode(t.getCode());
        if (localization == null) {
            localization = ServiceCodeLocalization.UNKNOWN;
        }

        final String action = request.getRequestURI();
        final boolean postBack = action.toLowerCase().contains(ControllerConstants.AJAX_PATH);
        final ErrorModel model = new ErrorModel(localization.key,
                                                (sessionModel != null) ? sessionModel.getErrorAction() : null,
                                                (sessionModel != null) ? sessionModel.getErrorMethod() : null,
                                                postBack);
        // Handle ajax call
        if (postBack) {
            return new ModelAndView(sessionHelper.getCurrentView().getContentFragment(),
                                    "model",
                                    model);
        }
        // Handle full get
        else {
            return new ModelAndView(ControllerConstants.PAGE_ERROR,
                                    "model",
                                    model);
        }
    }

    @ExceptionHandler(Throwable.class)
    public ModelAndView handleException(final HttpServletRequest request,
                                        final Throwable t) {
        log.error("Unexpected Exception occurred", t);
        sessionHelper.setCurrentView(errorPageDefinition);

        ServiceCodeLocalization localization = ServiceCodeLocalization.UNKNOWN;
        final SessionModel sessionModel = sessionHelper.getAttribute(SessionHelper.SessionConstants.VIEW_SESSION_DATA.name,
                                                                     SessionModel.class);
        if (sessionModel != null) {
            sessionModel.setError(Boolean.TRUE);
        }

        final String action = request.getRequestURI();
        final boolean postBack = action.toLowerCase().contains(ControllerConstants.AJAX_PATH);
        final ErrorModel model = new ErrorModel(localization.key,
                                                (sessionModel != null) ? sessionModel.getErrorAction() : null,
                                                (sessionModel != null) ? sessionModel.getErrorMethod() : null,
                                                postBack);

        // Handle ajax call
        if (postBack) {
            return new ModelAndView(ControllerConstants.PAGE_ERROR,
                                    "model",
                                    model);
        }
        // Handle full get
        else {
            return new ModelAndView(sessionHelper.getCurrentView().getContentFragment(),
                                    "model",
                                    model);
        }
    }
}
