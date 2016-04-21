package at.fh.ooe.swt6.worklog.manager.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * An embeddable entity which represents an address.
 * <p>
 * Created by Thomas Herzog <S1310307011@students.fh-hagenberg.at> on 4/16/2016.
 */
@Embeddable
public class Address implements Serializable {

    //<editor-fold desc="Properties">
    @Getter
    @Setter
    @NotNull
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    public String street;

    @Getter
    @Setter
    @NotNull
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    public String city;

    @Getter
    @Setter
    @NotNull
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    public String zipCode;
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public Address() {
        super();
    }

    /**
     * @param street
     * @param city
     * @param zipCode
     */
    public Address(String street,
                   String city,
                   String zipCode) {
        super();
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
    }
    //</editor-fold>

    //<editor-fold desc="Hash and Equals">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (!street.equals(address.street)) return false;
        if (!city.equals(address.city)) return false;
        return zipCode.equals(address.zipCode);

    }

    @Override
    public int hashCode() {
        int result = street.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + zipCode.hashCode();
        return result;
    }
    //</editor-fold>
}
