package at.fh.ooe.swt6.worklog.manager.model;

import at.fh.ooe.swt6.worklog.manager.model.api.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @GeneratedValue()
    @Column()
    private Long id;

    @Getter
    @Setter
    @NotNull
    @Column(nullable = false, length = 255)
    private String name;

    @Getter
    @Setter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "phase")
    private Set<LogBookEntry> logbookEntries = new HashSet<>();
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Phase() {
    }

    public Phase(Long id) {
        this.id = id;
    }

    public Phase(String name) {
        this.name = name;
    }
    //</editor-fold>
}
