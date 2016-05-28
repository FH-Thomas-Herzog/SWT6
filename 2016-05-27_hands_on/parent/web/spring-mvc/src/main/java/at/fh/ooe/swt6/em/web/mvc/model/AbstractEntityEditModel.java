package at.fh.ooe.swt6.em.web.mvc.model;

import at.fh.ooe.swt6.em.model.jpa.api.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * The base moel for all entity edit models.
 * <p>
 * Created by Thomas on 5/23/2016.
 */
@EqualsAndHashCode(of = {"id",
                         "version"})
public abstract class AbstractEntityEditModel<I extends Serializable, E extends Entity<I>> {

    @Getter
    @Setter
    private I id;

    @Getter
    @Setter
    private Long version;

    /**
     * Initializes the model from the entity
     *
     * @param entity the entity to initialize from
     */
    public abstract void fromEntity(E entity);

    /**
     * @return the create entity from this model set attributes
     */
    public abstract E toEntity();

    /**
     * @return true if this a new model, means no entity id set
     */
    public boolean isNewModel() {
        return id == null;
    }

    /**
     * @param entity the entity to be set. Model will initialize itself with common entity attributes
     */
    protected void setEntity(final E entity) {
        Objects.requireNonNull(entity, "Cannot initialize from null entity");
        this.id = entity.getId();
        this.version = entity.getVersion();
    }
}
