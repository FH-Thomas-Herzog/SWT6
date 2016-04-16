package at.fh.ooe.swt6.worklog.manager.model.jpa;

import at.fh.ooe.swt6.worklog.manager.model.jpa.api.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Thomas on 4/16/2016.
 */
@Table(name = "module")
@Entity
public class Module extends BaseEntity<Long> {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, insertable = false)
    private Long id;

    @Getter
    @Setter
    @NotNull
    @Column(nullable = false, length = 255)
    private String name;

    @Getter
    @Setter
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    @Getter
    @Setter
    @NotNull
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "module")
    private Set<LogBookEntry> logbookEntries = new HashSet<>();
}
