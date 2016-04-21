package at.fh.ooe.swt6.worklog.manager.model.api;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Base class for all entities which implement proper hash and equals for JPA entities.
 * Additionally all entities are using optimistic locking.
 * <p>
 * Created by Thomas on 4/16/2016.
 */
@MappedSuperclass
public abstract class BaseEntity<I extends Serializable> implements at.fh.ooe.swt6.worklog.manager.model.api.Entity<I> {

    @Getter
    @Setter
    @NotNull
    @Version
    @Column(nullable = false)
    private Long version;
    //</editor-fold>

    //<editor-fold desc="JPA Event Callbacks">

    /**
     * Sets the version field to value 1 on before persist action.
     */
    @PrePersist
    public void prePersist() {
        version = 1L;
    }

    /**
     * Increments the version field by 1 on before update action.
     */
    @PreUpdate
    public void preUpdate() {
        version++;
    }

    //</editor-fold>

    //<editor-fold desc="Overwritten Object Methods">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModifiableBaseEntity)) return false;

        at.fh.ooe.swt6.worklog.manager.model.api.Entity<?> that = (at.fh.ooe.swt6.worklog.manager.model.api.Entity<?>) o;

        return getId() != null ? getId().equals(that.getId()) : super.equals(o);
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder(this.getClass()
                                     .getSimpleName()).append("[id=")
                                                      .append(getId())
                                                      .append("]")
                                                      .toString();
    }
    //</editor-fold>
}
