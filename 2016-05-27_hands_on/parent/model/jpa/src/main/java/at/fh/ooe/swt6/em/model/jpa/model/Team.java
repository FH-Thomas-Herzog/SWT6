package at.fh.ooe.swt6.em.model.jpa.model;

import at.fh.ooe.swt6.em.model.jpa.api.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Thomas on 5/15/2016.
 */
@Entity
@Table(name = "TEAM")
@AllArgsConstructor
@NoArgsConstructor
public class Team extends BaseEntity<Long> {

    //<editor-fold desc="Properties">
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, insertable = false, updatable = false)
    private Long id;

    @Getter
    @Setter
    @NotNull
    @Column(length = 50, nullable = false)
    private String name;

    @Getter
    @Setter
    @NotNull
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "team1", cascade = {CascadeType.REMOVE})
    private Set<Game> gamesAsTeam1 = new HashSet<>(0);

    @Getter
    @Setter
    @NotNull
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "team2", cascade = {CascadeType.REMOVE})
    private Set<Game> gamesAsTeam2 = new HashSet<>(0);

    public Team(Long id) {
        this.id = id;
    }

    public Team(Long id,
                Long version) {
        this.id = id;
        this.setVersion(version);
    }

    public Team(final String name) {
        super();
        this.name = name;
    }

    //</editor-fold>
}
