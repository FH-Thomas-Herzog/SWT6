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
     * Finds a entity by tis id
     *
     * @param id    the entity id
     * @param clazz the entity class
     * @param <I>   the entity id type
     * @param <T>   the entity type
     * @return the found entity or null if not found
     */
    <I extends Serializable, T extends Entity<I>> T loadForClassAndIds(I id,
                                                                       Class<T> clazz);

    /**
     * Finds a entity by tis id
     *
     * @param ids   the entity ids
     * @param clazz the entity class
     * @param <I>   the entity id type
     * @param <T>   the entity type
     * @return the found entities or an empty list no one could be found
     */
    <I extends Serializable, T extends Entity<I>> List<T> loadForClassAndIds(List<I> ids,
                                                                             Class<T> clazz);

    /**
     * Loads all instance of the given class.
     *
     * @param clazz the entity class
     * @param <I>   the entity id type
     * @param <T>   the entity type
     * @return thelist of loaded entity instances
     */
    <I extends Serializable, T extends Entity<I>> List<T> loadAllForClass(Class<T> clazz);

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
     * Persist the given entity.
     *
     * @param entities the entities to be persisted
     * @param <I>      the entity id type
     * @param <T>      the entity type
     * @return the persistet entities
     * @throws javax.persistence.PersistenceException if the entity could not be persistet
     */
    <I extends Serializable, T extends Entity<I>> List<T> batchPersist(List<T> entities);

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
     * Updates or persists the given entity.
     *
     * @param entities the entities to be updated
     * @param <I>      the entity id type
     * @param <T>      the entity type
     * @return the merged entities
     */
    <I extends Serializable, T extends Entity<I>> List<T> batchMerge(List<T> entities);

    /**
     * Removes the given entity.
     *
     * @param entity the entity to be removed
     * @param <I>    the entity id type
     * @param <T>    the entity type
     */
    <I extends Serializable, T extends Entity<I>> void remove(T entity);

    /**
     * Removes the given entities.
     *
     * @param entities the entities to be removed
     * @param <I>      the entity id type
     * @param <T>      the entity type
     */
    <I extends Serializable, T extends Entity<I>> void remove(List<T> entities);

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

    /**
     * Closes this DataManager instance by releasing the backed session.
     * From now on, all operations will wail.
     */
    void close();

    /**
     * Clears the current persistent context and will release all cahced data.
     */
    void clear();
}
