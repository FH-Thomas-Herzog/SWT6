package at.fh.ooe.swt6.em.model.jpa.model;

import at.fh.ooe.swt6.em.model.jpa.api.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Thomas on 5/15/2016.
 */
@Entity
@Table(name = "USER_TIP")
public class Tip extends BaseEntity<Long> {

    //<editor-fold desc="Properties">
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, insertable = false, updatable = false)
    public Long id;

    @Getter
    @Setter
    @NotNull
    @Column(nullable = false, updatable = false)
    public Integer tipGoalsTeam1;

    @Getter
    @Setter
    @NotNull
    @Column(nullable = false, updatable = false)
    public Integer tipGoalsTeam2;

    @Getter
    @Setter
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    public User user;

    @Getter
    @Setter
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    public Game game;
    //</editor-fold>
}
