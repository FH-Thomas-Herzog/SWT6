package at.fh.ooe.swt6.em.logic.api;

import at.fh.ooe.swt6.em.model.jpa.model.Tip;

import java.io.Serializable;

/**
 * The logic interface for Tip data.
 * <p>
 * Created by Thomas on 5/16/2016.
 */
public interface TipLogic extends Serializable {

    /**
     * Saves the given tip
     *
     * @param tip the tip to be saved
     * @return the saved tip
     * @throws at.fh.ooe.swt6.em.logic.api.exception.LogicException if an required entity could not be found
     * @throws NullPointerException                                 if the game, team, user, one of their id or email is null
     * @see at.fh.ooe.swt6.em.logic.api.exception.LogicException.ServiceCode#ENTITY_NOT_FOUND
     */
    Tip save(Tip tip);

    /**
     * Deletes the tip with the given id.
     *
     * @param id the id of the tip to be deleted
     * @throws at.fh.ooe.swt6.em.logic.api.exception.LogicException if the entity could not be found
     * @see at.fh.ooe.swt6.em.logic.api.exception.LogicException.ServiceCode#ENTITY_NOT_FOUND
     */
    void delete(long id);
}
