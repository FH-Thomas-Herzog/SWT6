package at.fh.ooe.swt6.worklog.manager.service.hibernate;

import at.fh.ooe.swt6.worklog.manager.service.api.DataManager;
import at.fh.ooe.swt6.worklog.manager.service.api.DataManagerProvider;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Objects;

/**
 * Created by Thomas on 4/17/2016.
 */
public class HibernateDataManagerProvider implements DataManagerProvider {

    //<editor-fold desc="Private Members">
    private static HibernateDataManagerProvider instance;
    private static SessionFactory factory;
    private static boolean closed = Boolean.FALSE;
    private static final String CFG = "hibernate.cfg.xml";
    private static final ThreadLocal<Session> localEm = new ThreadLocal<>();
    //</editor-fold>

    //<editor-fold desc="Constructors">
    public HibernateDataManagerProvider() {
    }
    //</editor-fold>

    //<editor-fold desc="DataManagerProvider Interface Methods">
    @Override
    public DataManager create(boolean threadLocal) {
        if (closed) {
            throw new IllegalStateException("DataManagerProvider has been closed an cannot be used anymore");
        }
        final Session session;
        if (threadLocal) {
            if (localEm.get() == null) {
                Objects.requireNonNull(factory, "EntityManagerFactory is null");
                localEm.set(factory.openSession());
            }
            session = localEm.get();
        } else {
            Objects.requireNonNull(factory, "EntityManagerFactory is null");
            session = factory.openSession();
        }

        return new HibernateDataManager(session);
    }

    @Override
    public void recreateContext() {
        if (closed) {
            throw new IllegalStateException("DataManagerProvider has been closed an cannot be used anymore");
        }
        try {
            if ((factory != null) && (!factory.isClosed())) {
                factory.close();
                factory = null;
            }
            factory = new Configuration().configure(CFG)
                                         .buildSessionFactory();
        } catch (HibernateException e) {
            throw new IllegalStateException("Recreation of context failed", e);
        }
    }

    @Override
    public void close() {
        if ((!closed) && (factory != null) && (!factory.isClosed())) {
            factory.close();
            factory = null;
            closed = Boolean.TRUE;
        }
    }
    //</editor-fold>
}
