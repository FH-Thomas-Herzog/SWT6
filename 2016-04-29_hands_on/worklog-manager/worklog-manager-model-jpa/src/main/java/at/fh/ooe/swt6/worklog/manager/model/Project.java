package at.fh.ooe.swt6.worklog.manager.model;

import at.fh.ooe.swt6.worklog.manager.model.api.ModifiableBaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Thomas on 4/16/2016.
 */
@Table(name = "PROJECT")
@Entity
public class Project extends ModifiableBaseEntity<Long> {

    //<editor-fold desc="Properties">
    @Getter
    @Setter
    @NotNull
    @Column(nullable = false, length = 255)
    public String name;

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, insertable = false)
    public Long id;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "leader_employee_id", nullable = false)
    private Employee leader;

    @Getter
    @Setter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    private Set<ProjectEmployee> projectEmployees = new HashSet<>();

    @Getter
    @Setter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    private Set<Module> modules = new HashSet<>();
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Project() {
    }

    public Project(String name,
                   Employee leader) {
        this.name = name;
        this.leader = leader;
    }
    //</editor-fold>
}
