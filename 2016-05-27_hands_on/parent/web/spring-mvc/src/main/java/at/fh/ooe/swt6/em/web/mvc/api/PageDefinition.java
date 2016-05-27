package at.fh.ooe.swt6.em.web.mvc.api;

import java.io.Serializable;

/**
 * Created by Thomas on 5/22/2016.
 */
public interface PageDefinition extends Serializable {
    String getTemplate();
    String getTitleKey();
    String getContentFragment();
    String toActionUrl(String name);
}
