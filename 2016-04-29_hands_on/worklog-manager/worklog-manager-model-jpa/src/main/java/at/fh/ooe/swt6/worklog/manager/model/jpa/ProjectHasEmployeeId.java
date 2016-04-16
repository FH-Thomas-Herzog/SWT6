package at.fh.ooe.swt6.worklog.manager.model.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Thomas on 4/16/2016.
 */
@Embeddable
public class ProjectHasEmployeeId implements Serializable {

    //<editor-fold desc="Properties">
    @Getter
    @Setter
    @NotNull
    @Column(name = "project_id", nullable = false, updatable = false)
    private Long projectId;

    @Getter
    @Setter
    @NotNull
    @Column(name = "employee_id", nullable = false, updatable = false)
    private Long employeeId;
    //</editor-fold>


    public ProjectHasEmployeeId() {
    }

    public ProjectHasEmployeeId(Long projectId,
                                Long employeeId) {
        this.projectId = projectId;
        this.employeeId = employeeId;
    }

    //<editor-fold desc="Object Overwritten Methods">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectHasEmployeeId that = (ProjectHasEmployeeId) o;

        if (projectId != null ? !projectId.equals(that.projectId) : that.projectId != null) return false;
        return employeeId != null ? employeeId.equals(that.employeeId) : that.employeeId == null;

    }

    @Override
    public int hashCode() {
        int result = projectId != null ? projectId.hashCode() : 0;
        result = 31 * result + (employeeId != null ? employeeId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringBuilder(this.getClass()
                                     .getSimpleName()).append("[projectId=")
                                                      .append(projectId)
                                                      .append(", employeeId=")
                                                      .append(employeeId)
                                                      .append(']')
                                                      .toString();
    }
    //</editor-fold>
}
