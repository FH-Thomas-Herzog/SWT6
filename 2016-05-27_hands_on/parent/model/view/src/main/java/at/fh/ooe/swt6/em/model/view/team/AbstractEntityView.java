package at.fh.ooe.swt6.em.model.view.team;

import lombok.*;

import java.io.Serializable;

/**
 * Created by Thomas on 5/26/2016.
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id",
                         "version"})
public abstract class AbstractEntityView<I extends Serializable> {

    @Getter
    @Setter
    protected I id;
    @Getter
    @Setter
    protected Long version;
}
