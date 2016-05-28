package at.fh.ooe.swt6.em.model.view.team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Thomas on 5/26/2016.
 */
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractEntityView<I extends Serializable> {

    @Getter
    @Setter
    protected I id;
    @Getter
    @Setter
    protected Long version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntityView<?> that = (AbstractEntityView<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
