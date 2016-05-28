package at.fh.ooe.swt6.em.web.mvc.model;

import at.fh.ooe.swt6.em.model.jpa.api.Entity;
import at.fh.ooe.swt6.em.model.view.team.AbstractEntityView;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * The base for all view session models.
 * <p>
 * Created by Thomas on 5/23/2016.
 */
public abstract class SessionModel<I extends Serializable, E extends Entity<I>, V extends AbstractEntityView<I>> implements Serializable {

    //<editor-fold desc="Properties">
    @Getter
    @Setter
    private String errorAction;
    @Getter
    @Setter
    private String errorMethod;
    @Getter
    @Setter
    private boolean error = Boolean.FALSE;
    //</editor-fold>

    //<editor-fold desc="Private Fields">
    @Getter
    private SortedSet<V> views;
    //</editor-fold>

    //<editor-fold desc="Constructors">

    /**
     * Empty constructor which initializes this model.
     */
    public SessionModel() {
        this(null, null);
    }

    /**
     * @param errorAction the action to call in case of an error
     * @param method      the request method of the error action
     */
    public SessionModel(String errorAction,
                        String method) {
        this.errorAction = errorAction;
        this.errorMethod = method;
        clear();
    }
    //</editor-fold>

    //<editor-fold desc="Abstract Methods">
    public abstract V createViewFromEntity(E entity);

    public abstract V createViewFromId(I id);

    public abstract Comparator<V> createComparator();
    //</editor-fold>

    //<editor-fold desc="Public Methods">
    public void addNew(E entity) {
        Objects.requireNonNull(entity, "Cannot null add null item");

        final V view = createViewFromEntity(entity);
        removeNew(view.getId());
        views.add(view);
    }

    public void removeNew(final I id) {
        views.remove(createViewFromId(id));
    }

    public void clear() {
        views = new TreeSet<>(createComparator());
    }
    //</editor-fold>

}
