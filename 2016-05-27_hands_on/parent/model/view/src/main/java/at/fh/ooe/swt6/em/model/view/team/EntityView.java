package at.fh.ooe.swt6.em.model.view.team;

import java.io.Serializable;

/**
 * Created by Thomas on 5/26/2016.
 */
public interface EntityView<I extends Serializable> {

    I getId();

    Long getVersion();
}
