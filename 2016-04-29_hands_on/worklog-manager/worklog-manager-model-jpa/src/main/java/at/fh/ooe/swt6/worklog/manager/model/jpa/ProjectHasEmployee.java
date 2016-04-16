package at.fh.ooe.swt6.worklog.manager.model.jpa;

import at.fh.ooe.swt6.worklog.manager.model.jpa.api.ModifiableBaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by Thomas on 4/16/2016.
 */
@Table(name = "PROJECT_HAS_EMPLOYEE")
@Entity
public class ProjectHasEmployee extends ModifiableBaseEntity<ProjectHasEmployeeId> {

    //<editor-fold desc="Properties">
    @Getter
    @Setter
    @EmbeddedId
    private ProjectHasEmployeeId id;

    @Getter
    @Setter
    @JoinColumn(name = "project_id", referencedColumnName = "id", updatable = false, insertable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Project project;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id", referencedColumnName = "id", updatable = false, insertable = false)
    private Employee employee;

    //</editor-fold>
}
