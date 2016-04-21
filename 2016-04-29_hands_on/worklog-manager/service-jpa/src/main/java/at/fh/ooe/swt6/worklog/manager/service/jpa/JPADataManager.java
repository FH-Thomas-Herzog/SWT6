package at.fh.ooe.swt6.worklog.manager.service.jpa;

import at.fh.ooe.swt6.worklog.manager.model.api.Entity;
import at.fh.ooe.swt6.worklog.manager.service.api.DataManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is the JPA implementation of the {@link DataManager} interface.
 * Here we work with the {@link EntityManager} instance.
 * <p>
 * Created by Thomas on 4/17/2016.
 */
public class JPADataManager implements DataManager {

    //<editor-fold desc="Private Members">
    private final EntityManager em;
    private EntityTransaction tx;
    //</editor-fold>

    //<editor-fold desc="Constructors">

    /**
     * @param em the entity manager to use
     */
    public JPADataManager(EntityManager em) {
        Objects.requireNonNull(em, "EntityManager must not be null");
        this.em = em;
    }
    //</editor-fold>

    //<editor-fold desc="DataManager Interface Methods">
    @Override
    public <I extends Serializable, T extends Entity<I>> T loadForClassAndIds(I id,
                                                                              Class<T> clazz) {
        Objects.requireNonNull(clazz, "Cannot loadForClassAndIds entity for null entity class");

        return (id != null) ? em.find(clazz, id) : null;
    }

    @Override
    public <I extends Serializable, T extends Entity<I>> List<T> loadForClassAndIds(List<I> ids,
                                                                                    Class<T> clazz) {
        Objects.requireNonNull(ids, "Cannot load entities for null ids");

        return getForClassAndIds(clazz, ids);
    }

    @Override
    public <I extends Serializable, T extends Entity<I>> List<T> loadAllForClass(Class<T> clazz) {
        Objects.requireNonNull(clazz, "Entity class must not be null");
        return getForClassAndIds(clazz, null);
    }

    @Override
    public <I extends Serializable, T extends Entity<I>> T persist(T entity) {
        Objects.requireNonNull(entity, "Cannot persist null entity");

        em.persist(entity);

        return entity;
    }

    @Override
    public <I extends Serializable, T extends Entity<I>> List<T> batchPersist(List<T> entities) {
        Objects.requireNonNull(entities, "Cannot persist null entities");

        return entities.stream()
                       .map(item -> persist(item))
                       .collect(Collectors.toList());
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
    public <I extends Serializable, T extends Entity<I>> List<T> batchMerge(List<T> entities) {
        Objects.requireNonNull(entities, "Cannot merge null entities");

        return entities.stream()
                       .map(item -> merge(item))
                       .collect(Collectors.toList());
    }

    @Override
    public <I extends Serializable, T extends Entity<I>> void remove(T entity) {
        Objects.requireNonNull(entity, "Cannot remove null entity");
        Objects.requireNonNull(entity.getId(), "Cannot remove entity with null id");

        em.remove(entity);
    }

    @Override
    public <I extends Serializable, T extends Entity<I>> void remove(List<T> entities) {
        Objects.requireNonNull(entities, "Cannot delete null entities");

        entities.forEach(item -> remove(item));
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

    @Override
    public void startTx() {
        if (tx != null) {
            throw new PersistenceException("Transaction already open");
        }

        tx = em.getTransaction();
        tx.begin();
    }

    @Override
    public void commit() {
        if (tx == null) {
            throw new PersistenceException("No transaction open");
        }
        em.flush();
        tx.commit();
        tx = null;
    }

    @Override
    public void rollback() {
        if (tx == null) {
            throw new PersistenceException("No transaction open");
        }

        tx.rollback();
        tx = null;
    }

    @Override
    public boolean isTxOpen() {
        return (tx != null) && (tx.isActive());
    }

    @Override
    public void close() {
        if ((em != null) && (em.isOpen())) {
            // In case of an not catched exception
            if ((tx != null) && (tx.isActive())) {
                tx.rollback();
                tx = null;
            }
            clear();
            em.close();
        }
    }

    @Override
    public void clear() {
        if ((em != null) && (em.isOpen())) {
            em.clear();
        }
    }
    //</editor-fold>

    //<editor-fold desc="Private Helper">

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

    /**
     * Helper which is able to load all entity or with where claus on their ids.
     *
     * @param entityClazz the entity class
     * @param ids         the ids of the entity type, may be null
     * @param <I>         the entity id type
     * @param <T>         the entity type
     * @return the found entities, an empty list otherwise
     */
    private <I extends Serializable, T extends Entity<I>> List<T> getForClassAndIds(final Class<T> entityClazz,
                                                                                    final List<I> ids) {
        Objects.requireNonNull(entityClazz, "Cannot load for null class");
        final boolean isWithIds = (ids != null);
        // id list given but empty means no result
        if ((isWithIds) && (ids.isEmpty())) {
            return new ArrayList<>(0);
        }
        final String entityName = entityClazz.getSimpleName();
        final String query = ("SELECT entity FROM " + entityName + " entity") + ((isWithIds) ? " WHERE entity.id in (:ids)" : "");
        final Map<String, Object> parameters = (isWithIds) ? new HashMap() {{
            put("ids", ids);
        }} : null;
        return queryMultipleResult(query, entityClazz, parameters);
    }
    //</editor-fold>
}
