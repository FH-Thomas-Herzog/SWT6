package at.fh.ooe.swt6.worklog.manager.service.jpa;

import at.fh.ooe.swt6.worklog.manager.service.api.DataManager;
import at.fh.ooe.swt6.worklog.manager.service.api.DataManagerProvider;
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
public class JPADataManagerProvider implements DataManagerProvider {

    //<editor-fold desc="Static Members">
    private static boolean closed = Boolean.FALSE;
    private static EntityManagerFactory emf = null;

    private static final Logger log = LoggerFactory.getLogger(JPADataManagerProvider.class);
    private static final ThreadLocal<EntityManager> localEm = new ThreadLocal<>();

    private static final String PERSISTENCE_UNIT_TEST = "WorklogManagerTest";
    //</editor-fold>

    //<editor-fold desc="Private Helpers">

    /**
     * Creates the entity manager factory.
     * This will set a local reference to the factory and will use it from now on.
     *
     * @return the created factory
     */
    private static EntityManagerFactory createEntityManagerFactory() {
        if (emf != null) {
            emf.close();
            emf = null;
        }
        return emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_TEST);
    }

    /**
     * Gets an EntityManager from the EntityManagerFactory.
     *
     * @return the entity manager instance
     */
    private static EntityManager getEntityManager(boolean threadLocal) {
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
    //</editor-fold>

    //<editor-fold desc="Interface DataManager Methods">
    @Override
    public DataManager create(boolean threadLocal) {
        if (closed) {
            throw new IllegalStateException("DataManagerProvider has been closed an cannot be used anymore");
        }
        return new JPADataManager(getEntityManager(threadLocal));
    }

    @Override
    public void recreateContext() {
        if (closed) {
            throw new IllegalStateException("DataManagerProvider has been closed an cannot be used anymore");
        }
        createEntityManagerFactory();
    }

    @Override
    public void close() {
        if ((emf != null) && (emf.isOpen())) {
            emf.close();
            emf = null;
            closed = Boolean.TRUE;
        }
    }
    //</editor-fold>
}
