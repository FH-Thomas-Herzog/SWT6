package at.fh.ooe.swt6.worklog.manager.model;

import at.fh.ooe.swt6.worklog.manager.model.api.BaseEntity;
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
@Table(name = "module")
@Entity
public class Module extends BaseEntity<Long> {

    //<editor-fold desc="Properties">
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, insertable = false)
    private Long id;

    @Getter
    @Setter
    @NotNull
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @Getter
    @Setter
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false)
    private Project project;

    @Getter
    @Setter
    @NotNull
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "module")
    private Set<LogBookEntry> logbookEntries = new HashSet<>();
    //</editor-fold>

    //<editor-fold desc="Constructor">
    public Module() {
    }

    /**
     * @param name
     * @param project
     */
    public Module(String name,
                  Project project) {
        this.name = name;
        this.project = project;
    }
    //</editor-fold>

}
