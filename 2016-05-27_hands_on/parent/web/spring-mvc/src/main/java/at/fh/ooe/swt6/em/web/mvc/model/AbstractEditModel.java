package at.fh.ooe.swt6.em.web.mvc.model;

import at.fh.ooe.swt6.em.model.jpa.api.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Thomas on 5/23/2016.
 */
@EqualsAndHashCode(of = {"id",
                         "version"})
public abstract class AbstractEditModel<I extends Serializable, E extends Entity<I>> {

    @Getter
    @Setter
    private  I id;

    @Getter
    @Setter
    private Long version;

    private E entity;

    public abstract void fromEntity(E entity);

    public abstract E toEntity();

    public boolean isNewModel() {
        return id == null;
    }

    protected void setEntity(final E entity) {
        Objects.requireNonNull(entity, "Cannot initialize from null entity");
        this.entity = entity;
        this.id = entity.getId();
        this.version = entity.getVersion();
    }

    protected E getEntity() {
        return entity;
    }

}
