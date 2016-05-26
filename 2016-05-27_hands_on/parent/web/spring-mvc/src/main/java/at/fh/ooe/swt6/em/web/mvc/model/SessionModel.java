package at.fh.ooe.swt6.em.web.mvc.model;

import at.fh.ooe.swt6.em.model.jpa.api.Entity;
import at.fh.ooe.swt6.em.model.view.team.EntityView;
import lombok.Getter;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Thomas on 5/23/2016.
 */
public abstract class SessionModel<I extends Serializable, E extends Entity<I>, V extends EntityView<I>> implements Serializable {

    @Getter
    private SortedSet<V> views;

    public SessionModel() {
        clear();
    }

    public abstract V createViewFromEntity(E entity);

    public abstract V createViewFromId(I id);

    public abstract Comparator<V> createComparator();

    public void addNew(E entity) {
        Objects.requireNonNull(entity, "Cannot null add null item");

        final V view = createViewFromEntity(entity);
        views.remove(view);
        views.add(view);
    }

    public void removeNew(final I id) {
        views.remove(createViewFromId(id));
    }

    public void clear() {
        views = new TreeSet<>(createComparator());
    }
}
