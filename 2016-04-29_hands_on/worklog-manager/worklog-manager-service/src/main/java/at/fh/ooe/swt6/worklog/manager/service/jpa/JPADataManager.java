package at.fh.ooe.swt6.worklog.manager.service.jpa;

import at.fh.ooe.swt6.worklog.manager.model.jpa.api.Entity;
import at.fh.ooe.swt6.worklog.manager.service.api.DataManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Thomas on 4/17/2016.
 */
public class JPADataManager implements DataManager {

    private final EntityManager em;

    /**
     * @param em the EntityManger instance to use
     */
    public JPADataManager(EntityManager em) {
        Objects.requireNonNull(em, "EntityManager must not be null");
        this.em = em;
    }

    @Override
    public <I extends Serializable, T extends Entity<I>> T byId(I id,
                                                                Class<T> clazz) {
        final T result = find(id,
                              clazz);
        if (result == null) {
            throw new EntityNotFoundException("Entity of type '" + clazz + "' with id '" + id + "' not found");
        }

        return result;
    }

    @Override
    public <I extends Serializable, T extends Entity<I>> T find(I id,
                                                                Class<T> clazz) {
        Objects.requireNonNull(id, "Cannot get entity for null id");
        Objects.requireNonNull(clazz, "Cannot find entity for null entity class");

        return em.find(clazz, id);
    }

    @Override
    public <I extends Serializable, T extends Entity<I>> T persist(T entity) {
        Objects.requireNonNull(entity, "Cannot persist null entity");

        em.persist(entity);

        return entity;
    }

    @Override
    public <I extends Serializable, T extends Entity<I>> T merge(T entity) {
        Objects.requireNonNull(entity, "Cannot persist null entity");

        T result;

        if (entity.getId() == null) {
            result = persist(entity);
        } else {
            result = em.merge(entity);
        }

        return result;
    }

    @Override
    public <I extends Serializable, T extends Entity<I>> void remove(T entity) {
        Objects.requireNonNull(entity, "Cannot remove null entity");
        Objects.requireNonNull(entity.getId(), "Cannot remove entity with null id");

        em.remove(entity);
    }

    @Override
    public <I extends Serializable, T extends Entity<I>> List<T> queryMultipleResult(String query,
                                                                                     Class<T> resultClass,
                                                                                     Map<String, Object> parameters) {
        return createQuery(query, resultClass, parameters).getResultList();
    }

    @Override
    public <I extends Serializable, T extends Entity<I>> T querySingleResult(String query,
                                                                             Class<T> resultClass,
                                                                             Map<String, Object> parameters) {
        return (T) createQuery(query, resultClass, parameters).getSingleResult();
    }

    @Override
    public int executeQuery(String query,
                            Map<String, Object> parameters) {
        return createQuery(query, null, parameters).executeUpdate();
    }

    /**
     * Helper method for creating the query object for reading access queries.
     *
     * @param queryString the query string to create query for
     * @param resultClass the result class
     * @param parameters  the parameters to apply, may be null
     * @param <T>         the result type
     * @return the create query object
     */
    private <T extends Entity> Query createQuery(String queryString,
                                                 Class<T> resultClass,
                                                 Map<String, Object> parameters) {
        Objects.requireNonNull(queryString, "Cannot query for null string");

        final Query query;
        if (resultClass != null) {
            query = em.createQuery(queryString, resultClass);
        } else {
            query = em.createQuery(queryString);
        }

        if ((parameters != null) && (!parameters.isEmpty())) {
            parameters.forEach((key, value) -> query.setParameter(key, value));
        }

        return query;
    }
}
