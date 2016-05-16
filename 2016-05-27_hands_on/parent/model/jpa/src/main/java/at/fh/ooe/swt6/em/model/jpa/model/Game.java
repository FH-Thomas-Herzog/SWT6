package at.fh.ooe.swt6.em.model.jpa.model;

import at.fh.ooe.swt6.em.model.jpa.api.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Thomas on 5/15/2016.
 */
@Entity
@Table(name = "GAME")
public class Game extends BaseEntity<Long> {

    //<editor-fold desc="Properties">
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, insertable = false, updatable = false)
    public Long id;

    @Getter
    @Setter
    @Column(length = 2)
    public Integer goalsTeam1;

    @Getter
    @Setter
    @Column(length = 2)
    public Integer goalsTeam2;

    @Getter
    @Setter
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    public Team team1;

    @Getter
    @Setter
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    public Team team2;

    @Getter
    @Setter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "game")
    public Set<Tip> tips = new HashSet<>(0);
    //</editor-fold>
}
