package at.fh.ooe.swt6.em.web.mvc.app.constants;

import at.fh.ooe.swt6.em.web.mvc.api.PageDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Thomas on 5/22/2016.
 */
@Named("sessionHelper")
@SessionScoped
@Slf4j
public class SessionHelper implements Serializable {

    public enum SessionConstants {
        CURRENT_PAGE("currentPage");
        public final String name;

        SessionConstants(String name) {
            this.name = name;
        }
    }

    @PostConstruct
    public void postConstruct() {
        log.debug("SessionHelper created");
    }

    public boolean isOnPage(final String page) {
        String currentView = getCurrentView().getTemplate();
        return ((currentView != null) && (currentView.equalsIgnoreCase(page)));
    }

    public String getCurrentViewTemplate() {
        final PageDefinition definition = getCurrentView();
        return (definition != null) ? definition.getTemplate() : null;
    }

    public String getCurrentViewTitleKey() {
        final PageDefinition definition = getCurrentView();
        return (definition != null) ? definition.getTitleKey() : null;
    }

    public PageDefinition getCurrentView() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return (PageDefinition) attr.getRequest().getSession(false).getAttribute(SessionConstants.CURRENT_PAGE.name);
    }

    public void setCurrentView(final PageDefinition definition) {
        Objects.requireNonNull(definition, "current page must not be empty");

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        attr.getRequest().getSession(false).setAttribute(SessionConstants.CURRENT_PAGE.name, definition);
    }
}
