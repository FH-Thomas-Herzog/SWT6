package at.fh.ooe.swt6.worklog.manager.service.jpa;

import at.fh.ooe.swt6.worklog.manager.model.api.Entity;
import at.fh.ooe.swt6.worklog.manager.service.api.DataManager;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Thomas on 4/17/2016.
 */
public class JPADataManager implements DataManager {

    private final EntityManager em;
    private EntityTransaction tx;

    /**
     */
    public JPADataManager(EntityManager em) {
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
        Objects.requireNonNull(clazz, "Cannot find entity for null entity class");

        return (id != null) ? em.find(clazz, id) : null;
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
    //</editor-fold>
}
