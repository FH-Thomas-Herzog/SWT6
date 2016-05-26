package at.fh.ooe.swt6.em.logic.api;

import at.fh.ooe.swt6.em.model.jpa.model.Tip;

import java.io.Serializable;

/**
 * Created by Thomas on 5/16/2016.
 */
public interface TipLogic extends Serializable {

    Tip save(Tip tip);

    void delete(long id);
}
