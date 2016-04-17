package at.fh.ooe.swt6.worklog.manager.model;

import at.fh.ooe.swt6.worklog.manager.model.api.ModifiableBaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Thomas on 4/16/2016.
 */
@Table(name = "LOG_BOOK_ENTRY")
@Entity
public class LogBookEntry extends ModifiableBaseEntity<Long> {

    //<editor-fold desc="Properties">
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, insertable = false)
    private Long id;

    @Getter
    @Setter
    @Column(nullable = false, length = 100)
    private String activity;

    @Getter
    @Setter
    @Temporal(TemporalType.TIME)
    @Column(nullable = false)
    private Date startTime;

    @Getter
    @Setter
    @Temporal(TemporalType.TIME)
    @Column(nullable = false)
    private Date endTime;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "phase_id", referencedColumnName = "id", nullable = false)
    private Phase phase;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_id", referencedColumnName = "id", nullable = false)
    private Module module;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public LogBookEntry() {
    }

    public LogBookEntry(String activity,
                        Date startTime,
                        Date endTime,
                        Employee employee,
                        Phase phase,
                        Module module) {
        this.activity = activity;
        this.startTime = startTime;
        this.endTime = endTime;
        this.employee = employee;
        this.phase = phase;
        this.module = module;
    }
    //</editor-fold>
}
