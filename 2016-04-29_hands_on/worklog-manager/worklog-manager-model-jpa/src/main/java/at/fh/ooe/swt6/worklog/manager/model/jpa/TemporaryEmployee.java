package at.fh.ooe.swt6.worklog.manager.model.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Created by Thomas on 4/16/2016.
 */
@Table(name = "TEMPORARY_EMPLOYEE")
@Entity
@DiscriminatorValue("TEMPORARY")
public class TemporaryEmployee extends Employee {

    //<editor-fold desc="Properties">
    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Calendar startDate;

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Calendar endDate;

    @Getter
    @Setter
    @NotNull
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal hourlyRate;

    @Getter
    @Setter
    @NotNull
    @Column(nullable = false, length = 1)
    private Boolean renter = Boolean.FALSE;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public TemporaryEmployee() {
        super();
    }

    public TemporaryEmployee(Long id) {
        super(id);
    }

    public TemporaryEmployee(String firstName,
                             String lastName,
                             Calendar dateOfBirth,
                             Address address,
                             BigDecimal hourlyRate,
                             Boolean renter,
                             Calendar startDate,
                             Calendar endDate) {
        super(firstName,
              lastName,
              dateOfBirth,
              address);
        this.hourlyRate = hourlyRate;
        this.renter = renter;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    //</editor-fold>

}
