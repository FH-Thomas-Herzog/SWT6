package at.fh.ooe.swt6.worklog.manager.service.hibernate;

import at.fh.ooe.swt6.worklog.manager.model.api.Entity;
import at.fh.ooe.swt6.worklog.manager.service.api.DataManager;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Thomas on 4/17/2016.
 */
public class HibernateDataManager implements DataManager {

    private Transaction tx;

    private final Session session;

    public HibernateDataManager() {
        this.session = HibernateUtils.getInstance("hibernate.cfg.xml")
                                     .createSession();
    }

    @Override
    public <I extends Serializable, T extends Entity<I>> T byId(I id,
                                                                Class<T> clazz) {

        final T result = find(id, clazz);
        if (result == null) {
            throw new EntityNotFoundException("Entity of type '" + clazz + "' with id '" + id + "'");
        }

        return result;
    }

    @Override
    public <I extends Serializable, T extends Entity<I>> T find(I id,
                                                                Class<T> clazz) {
        Objects.requireNonNull(id, "Cannot find entity for null id");
        Objects.requireNonNull(clazz, "Cannot find entity for null class");

        return (T) session.load(clazz, id);
    }

    @Override
    public <I extends Serializable, T extends Entity<I>> T persist(T entity) {
        Objects.requireNonNull(entity, "Cannot persist null entity");

        session.persist(entity);
        return entity;
    }

    @Override
    public <I extends Serializable, T extends Entity<I>> T merge(T entity) {
        Objects.requireNonNull(entity, "Cannot merge null entity");

        if (entity.getId() == null) {
            entity = persist(entity);
        } else {
            entity = (T) session.merge(entity);
        }

        return entity;
    }

    @Override
    public <I extends Serializable, T extends Entity<I>> void remove(T entity) {
        Objects.requireNonNull(entity, "Cannot remove null entity");

        session.delete(entity);
    }

    @Override
    public <I extends Serializable, T extends Entity<I>> List<T> queryMultipleResult(String query,
                                                                                     Class<T> resultClass,
                                                                                     Map<String, Object> parameters) {
        return createQuery(query, null, parameters).list();
    }

    @Override
    public <I extends Serializable, T extends Entity<I>> T querySingleResult(String query,
                                                                             Class<T> resultClass,
                                                                             Map<String, Object> parameters) {
        return (T) createQuery(query, null, parameters).uniqueResult();
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

        tx = session.getTransaction();
        tx.begin();
    }

    @Override
    public void commit() {
        if (tx == null) {
            throw new PersistenceException("No transaction open");
        }
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
        if (session.isOpen()) {
            session.clear();
            session.close();
        }
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

        final Query query = session.createQuery(queryString);

        if ((parameters != null) && (!parameters.isEmpty())) {
            parameters.forEach((key, value) -> query.setParameter(key, value));
        }

        return query;
    }
}
