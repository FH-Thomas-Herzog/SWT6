package at.fh.ooe.swt6.em.web.mvc.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.Serializable;

/**
 * Created by Thomas on 5/16/2016.
 */
// ManagedBean did not work which is supposed to be an known issue, so we use component here.
@Component("myModel")
// JSF scopes and CDI scopes not working here, so use spring one, we use spring beans anyway.
@Scope("request")
// Lombok creates logger here
@Slf4j
public class MyModel implements Serializable {

    @PostConstruct
    public void postConstruct() {
        log.debug("Post construct called");
    }

    @PreDestroy
    public void postDestory() {
        log.debug("Pre destroy called");
    }

    public String getMe() {
        return "Form EL spring backing bean";
    }
}
