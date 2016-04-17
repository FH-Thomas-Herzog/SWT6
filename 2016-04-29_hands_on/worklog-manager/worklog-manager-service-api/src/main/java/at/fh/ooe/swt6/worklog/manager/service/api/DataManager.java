package at.fh.ooe.swt6.worklog.manager.service.api;

import at.fh.ooe.swt6.worklog.manager.model.api.Entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * This interface specifies the data manager.
 * The implementations can feel free to use any JPA provider intended.
 * <p>
 * Created by Thomas on 4/17/2016.
 */
public interface DataManager {

    /**
     * Gets the entity by its id
     *
     * @param id    the entity id
     * @param clazz the entity class
     * @param <I>   the entity id type
     * @param <T>   the entity type
     * @return the found entity
     * @throws javax.persistence.EntityNotFoundException if entity could not be found
     */
    <I extends Serializable, T extends Entity<I>> T byId(I id,
                                                         Class<T> clazz);

    /**
     * Finds a entity by tis id
     *
     * @param id    the entity id
     * @param clazz the entity class
     * @param <I>   the entity id type
     * @param <T>   the entity type
     * @return the found entity or null if not found
     */
    <I extends Serializable, T extends Entity<I>> T find(I id,
                                                         Class<T> clazz);

    /**
     * Persist the given entity.
     *
     * @param entity the entity to eb persisted
     * @param <I>    the entity id type
     * @param <T>    the entity type
     * @return the persistet entity
     * @throws javax.persistence.PersistenceException if the entity could not be persistet
     */
    <I extends Serializable, T extends Entity<I>> T persist(T entity);

    /**
     * Updates or persists the given entity.
     *
     * @param entity the entity to be updated
     * @param <I>    the entity id type
     * @param <T>    the entity type
     * @return the merged entity
     */
    <I extends Serializable, T extends Entity<I>> T merge(T entity);

    /**
     * Removes the given entity.
     *
     * @param entity the entity to be removed
     * @param <I>    the entity id type
     * @param <T>    the entity type
     */
    <I extends Serializable, T extends Entity<I>> void remove(T entity);

    /**
     * Executes the given query and expects and multiple result.
     *
     * @param query       the query string to be executed
     * @param resultClass the entity class
     * @param parameters  the map of parameters applied to the query
     * @param <I>         the entity id type
     * @param <T>         the entity type
     * @return the result list.
     */
    <I extends Serializable, T extends Entity<I>> List<T> queryMultipleResult(String query,
                                                                              Class<T> resultClass,
                                                                              Map<String, Object> parameters);

    /**
     * Executes the given query and expects and single result.
     *
     * @param query       the query string to be executed
     * @param resultClass the entity class
     * @param parameters  the map of parameters applied to the query
     * @param <I>         the entity id type
     * @param <T>         the entity type
     * @return the single result
     */
    <I extends Serializable, T extends Entity<I>> T querySingleResult(String query,
                                                                      Class<T> resultClass,
                                                                      Map<String, Object> parameters);

    /**
     * Executes the given query.
     *
     * @param query      the query string to be executed
     * @param parameters the parameters to apply to the query
     * @return the count of affected rows.
     */
    int executeQuery(String query,
                     Map<String, Object> parameters);

    /**
     * Opens a transaction
     *
     * @throws javax.persistence.PersistenceException if a transaction is already open
     */
    void startTx();

    /**
     * Commits the current transaction
     *
     * @throws javax.persistence.PersistenceException if a transaction hasn't been opened yet
     */
    void commit();

    /**
     * Rolls the current transaction back
     *
     * @throws javax.persistence.PersistenceException if a transaction hasn't been opened yet
     */
    void rollback();

    /**
     * Answers the question if the a transaction has been opened or not.
     *
     * @return true if a transaction has been opened, false otherwise
     */
    boolean isTxOpen();
}
