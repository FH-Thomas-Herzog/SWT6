package at.fh.ooe.swt6.worklog.manager.model.jpa;

import at.fh.ooe.swt6.worklog.manager.model.jpa.api.ModifiableBaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Thomas on 4/16/2016.
 */
@Table(name = "EMPLOYEE")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "discriminator", length = 50, discriminatorType = DiscriminatorType.STRING)
public abstract class Employee extends ModifiableBaseEntity<Long> {

    //<editor-fold desc="Properties">
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, insertable = false)
    private Long id;

    @Getter
    @Setter
    @NotNull
    @Column(nullable = false)
    private String firstName;

    @Getter
    @Setter
    @NotNull
    @Column(nullable = false)
    private String lastName;

    @Getter
    @Setter
    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Calendar dateOfBirth;

    @Getter
    @Setter
    @NotNull
    @Column(nullable = false)
    @Embedded
    private Address address;

    @Getter
    @Setter
    @NotNull
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    private Set<LogBookEntry> logbookEntries = new HashSet<>();

    @Getter
    @Setter
    @NotNull
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    private Set<ProjectHasEmployee> employeeProjects;

    @Getter
    @Setter
    @NotNull
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "leader")
    private Set<Project> leadingProjects;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Employee() {
    }

    /**
     * @param id
     */
    public Employee(Long id) {
        this.id = id;
    }

    /**
     * @param firstName
     * @param lastName
     * @param dateOfBirth
     * @param address
     */
    public Employee(String firstName,
                    String lastName,
                    Calendar dateOfBirth,
                    Address address) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }
    //</editor-fold>


}
