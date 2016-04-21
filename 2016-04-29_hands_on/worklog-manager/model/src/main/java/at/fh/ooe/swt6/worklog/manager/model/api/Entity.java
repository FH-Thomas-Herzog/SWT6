package at.fh.ooe.swt6.worklog.manager.model.api;

import java.io.Serializable;

/**
 * This interface marks an database entity.
 * <p>
 * Created by Thomas on 4/17/2016.
 */
public interface Entity<I extends Serializable> {

    /**
     * @return the entity id
     */
    I getId();

    /**
     * @param id the entity id to be set
     */
    void setId(I id);

    /**
     * Force implementation of  equals method
     *
     * @param o the other object
     * @return true if euql, false otherwise
     */
    boolean equals(Object o);

    /**
     * Force implementation of hash method.
     *
     * @return the has value of this entity
     */
    int hashCode();
}
