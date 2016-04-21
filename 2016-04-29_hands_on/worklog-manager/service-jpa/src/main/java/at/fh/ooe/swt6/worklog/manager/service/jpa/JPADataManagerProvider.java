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

    public static final String PERSISTENCE_UNIT = "WorklogManager";
    public static final String PERSISTENCE_UNIT_TEST = "WorklogManagerTest";
    //</editor-fold>

    //<editor-fold desc="Interface DataManager Methods">
    @Override
    public DataManager create(boolean threadLocal) {
        if (closed) {
            throw new IllegalStateException("DataManagerProvider has been closed an cannot be used anymore");
        }
        final EntityManager em;
        if (threadLocal) {
            if (localEm.get() == null) {
                Objects.requireNonNull(emf, "EntityManagerFactory is null");
                localEm.set(emf.createEntityManager());
            }
            em = localEm.get();
        } else {
            Objects.requireNonNull(emf, "EntityManagerFactory is null");
            em = emf.createEntityManager();
        }

        return new JPADataManager(em);
    }

    @Override
    public void recreateContext() {
        if (closed) {
            throw new IllegalStateException("DataManagerProvider has been closed an cannot be used anymore");
        }
        try {
            if ((emf != null) && (emf.isOpen())) {
                emf.close();
                emf = null;
            }
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_TEST);
        } catch (Exception e) {
            throw new IllegalStateException("Could not recreate context");
        }
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
