package at.fh.ooe.swt6.worklog.manager.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Calendar;

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
    @DecimalMin("0.0")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal salary;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public PermanentEmployee() {
    }

    /**
     * @param id
     */
    public PermanentEmployee(Long id) {
        super(id);
    }

    /**
     * @param firstName
     * @param lastName
     * @param dateOfBirth
     * @param address
     * @param salary
     */
    public PermanentEmployee(String firstName,
                             String lastName,
                             Calendar dateOfBirth,
                             Address address,
                             BigDecimal salary) {
        super(firstName, lastName, dateOfBirth, address);
        this.salary = salary;
    }

    //</editor-fold>
}
