package at.fh.ooe.swt6.em.model.jpa.model;

import at.fh.ooe.swt6.em.model.jpa.api.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Thomas on 5/15/2016.
 */
@Entity
@Table(name = "USER_TIP")
@NoArgsConstructor
@AllArgsConstructor
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
    @ManyToOne(fetch = FetchType.EAGER)
    public User user;

    @Getter
    @Setter
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    public Game game;
    //</editor-fold>

    public Tip(Long id) {
        this.id = id;
    }
}
