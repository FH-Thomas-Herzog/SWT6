package at.fh.ooe.swt6.worklog.manager.model.jpa.api;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Calendar;

/**
 * This class marks an entity as an modifiable entity.
 * <p>
 * Created by Thomas on 4/16/2016.
 */
@MappedSuperclass
public abstract class ModifiableBaseEntity<I extends Serializable> extends BaseEntity<I> implements Serializable {

    //<editor-fold desc="Properties">
    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    public Calendar creationDate;

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    public Calendar modificationDate;
    //</editor-fold>

    //<editor-fold desc="JPA Event Callbacks">
    @Override
    public void prePersist() {
        super.prePersist();
        creationDate = modificationDate = Calendar.getInstance();
    }

    @Override
    public void preUpdate() {
        super.preUpdate();
        modificationDate = Calendar.getInstance();
    }
    //</editor-fold>

    //<editor-fold desc="Constructors">

    /**
     * Empty constructor
     */
    public ModifiableBaseEntity() {
    }
    //</editor-fold>
}
