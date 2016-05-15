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
@Table(name = "USER")
public class User extends BaseEntity<Long> {

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
    @Column(length = 100, nullable = false)
    public String email;

    @Getter
    @Setter
    @NotNull
    @Column(length = 100, nullable = false)
    public String username;

    @Getter
    @Setter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    public Set<Tip> tips = new HashSet<>(0);
    //</editor-fold>
}
