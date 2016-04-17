package at.fh.ooe.swt6.worklog.manager.service.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Objects;

/**
 * This class holds the JPA utilities for managing EntityManager and EntityMangerFactory instances.
 * <p>
 * Created by Thomas on 4/16/2016.
 */
public class JPAUtils {

    private static EntityManagerFactory emf = null;

    private static final Logger log = LoggerFactory.getLogger(JPAUtils.class);
    private static final ThreadLocal<EntityManager> localEm = new ThreadLocal<>();

    public static final String PERSISTENCE_UNIT_TEST = "WorklogManagerTest";

    /**
     * Creates the entity manager factory.
     * This will set a local reference to the factory and will use it from now on.
     *
     * @return the created factory
     */
    public static EntityManagerFactory createEntityManagerFactory() {
        if (emf != null) {
            emf.close();
            emf = null;
        }
        return emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_TEST);
    }

    /**
     * Gets an EntityManager from the EntityManagerFactory.
     * Will create a new on from the factory per default.
     *
     * @return the entity manager instance
     * @see JPAUtils#getEntityManager(boolean)
     */
    public static EntityManager getEntityManager() {
        return getEntityManager(Boolean.FALSE);
    }

    /**
     * Gets an EntityManager from the EntityManagerFactory.
     *
     * @return the entity manager instance
     */
    public static EntityManager getEntityManager(boolean threadLocal) {
        if (threadLocal) {
            if (localEm.get() == null) {
                Objects.requireNonNull(emf, "EntityManagerFactory is null");
                localEm.set(emf.createEntityManager());
            }
            return localEm.get();
        } else {
            Objects.requireNonNull(emf, "EntityManagerFactory is null");
            return emf.createEntityManager();
        }
    }
}
