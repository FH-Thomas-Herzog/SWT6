package at.fh.ooe.swt6.worklog.manager.model;

import at.fh.ooe.swt6.worklog.manager.model.api.ModifiableBaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, insertable = false)
    public Long id;

    @Getter
    @Setter
    @NotNull
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    public String name;

    @Getter
    @Setter
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "leader_employee_id", nullable = false)
    private Employee leader;

    @Getter
    @Setter
    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "PROJECT_EMPLOYEES",
            joinColumns = @JoinColumn(name = "project_id",
                    referencedColumnName = "id",
                    nullable = false,
                    insertable = false,
                    updatable = false),
            inverseJoinColumns = @JoinColumn(name = "employee_id",
                    referencedColumnName = "id",
                    nullable = false,
                    insertable = false,
                    updatable = false))
    private Set<Employee> projectEmployees = new HashSet<>();

    @Getter
    @Setter
    @NotNull
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "project", cascade = CascadeType.REMOVE)
    private Set<Module> modules = new HashSet<>();
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Project() {
    }
    
    /**
     * @param name
     * @param leader
     */
    public Project(String name,
                   Employee leader) {
        this.name = name;
        this.leader = leader;
    }
    //</editor-fold>
}
