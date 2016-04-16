package at.fh.ooe.swt6.worklog.manager.model.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by Thomas on 4/16/2016.
 */
@Table(name = "PERMANENT_EMPLOYEE")
@Entity
@DiscriminatorValue("PERMANENT")
public class PermanentEmployee extends Employee {

    //<editor-fold desc="Properties">
    @Getter
    @Setter
    @NotNull
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal salary;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public PermanentEmployee() {
    }

    public PermanentEmployee(Long id) {
        super(id);
    }
    //</editor-fold>
}
