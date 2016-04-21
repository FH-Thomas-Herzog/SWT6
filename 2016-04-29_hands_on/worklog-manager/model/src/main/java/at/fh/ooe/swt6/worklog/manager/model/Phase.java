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
@Table(name = "PHASE")
@Entity
public class Phase extends BaseEntity<Long> {

    //<editor-fold desc="Properties">
    @Getter
    @Setter
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column()
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
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "phase")
    private Set<LogBookEntry> logbookEntries = new HashSet<>();
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Phase() {
    }

    /**
     * @param id
     */
    public Phase(Long id) {
        this.id = id;
    }

    /**
     * @param name
     */
    public Phase(String name) {
        this.name = name;
    }
    //</editor-fold>
}
