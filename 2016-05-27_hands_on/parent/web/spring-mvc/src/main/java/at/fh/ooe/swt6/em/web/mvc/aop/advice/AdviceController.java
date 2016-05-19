package at.fh.ooe.swt6.em.web.mvc.aop.advice;

import at.fh.ooe.swt6.em.web.mvc.controller.GameController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Thomas on 5/19/2016.
 */
@ControllerAdvice(basePackageClasses = GameController.class)
@Slf4j
public class AdviceController {

    @ExceptionHandler(Throwable.class)
    public RedirectView handleException(final HttpServletRequest request,
                                        final Throwable t) {
        log.error("Exception occurred on request", t);
        return new RedirectView("error");
    }
}
