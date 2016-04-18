package at.fh.ooe.swt6.worklog.manager.model;

import at.fh.ooe.swt6.worklog.manager.model.api.ModifiableBaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by Thomas on 4/16/2016.
 */
@Table(name = "PROJECT_EMPLOYEE")
@Entity
public class ProjectEmployee extends ModifiableBaseEntity<ProjectEmployeeId> {

    //<editor-fold desc="Properties">
    @Getter
    @Setter
    @EmbeddedId
    private ProjectEmployeeId id;

    @Getter
    @Setter
    @JoinColumn(name = "project_id", referencedColumnName = "id", updatable = false, insertable = false)
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Project project;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", updatable = false, insertable = false)
    private Employee employee;

    //</editor-fold>


    public ProjectEmployee() {
    }

    public ProjectEmployee(ProjectEmployeeId id) {
        this.id = id;
    }

    public ProjectEmployee(Project project,
                           Employee employee) {
        this.project = project;
        this.employee = employee;
    }
}
