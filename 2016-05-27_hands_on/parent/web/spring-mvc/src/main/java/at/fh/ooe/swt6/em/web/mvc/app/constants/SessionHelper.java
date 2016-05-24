package at.fh.ooe.swt6.em.web.mvc.app.constants;

import at.fh.ooe.swt6.em.web.mvc.api.PageDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
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
        CURRENT_PAGE("currentPage"),
        FORMER_PAGE("formerPage"),
        VIEW_SESSION_DATA("viewSessionData");
        public final String name;

        SessionConstants(String name) {
            this.name = this.getClass().getSimpleName().concat("#").concat(name);
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
        return (PageDefinition) getSession(false).getAttribute(SessionConstants.CURRENT_PAGE.name);
    }

    public void setCurrentView(final PageDefinition definition) {
        Objects.requireNonNull(definition, "current page must not be empty");

        final PageDefinition oldDefinition = getCurrentView();
        final HttpSession session = getSession(false);
        session.setAttribute(SessionConstants.CURRENT_PAGE.name, definition);
        if (oldDefinition != null) {
            session.setAttribute(SessionConstants.FORMER_PAGE.name, oldDefinition);
        }
    }

    public void setAttribute(String name,
                             Object attriute) {
        Objects.requireNonNull(name, "Session attribute needs a name");
        Objects.requireNonNull(attriute, "Use SessionHelper#removeAttribute(name) instead");
        getSession(false).setAttribute(name, attriute);
    }

    public <T> T getAttribute(String name,
                              Class<T> clazz) {
        Objects.requireNonNull(name, "Session attribute needs a name");
        return (T) getSession(false).getAttribute(name);
    }

    public void removeAttribute(final String name) {
        Objects.requireNonNull(name, "Session attribute has a name");
        getSession(false).removeAttribute(name);
    }

    private HttpSession getSession(boolean create) {
        final ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(create);
    }
}
