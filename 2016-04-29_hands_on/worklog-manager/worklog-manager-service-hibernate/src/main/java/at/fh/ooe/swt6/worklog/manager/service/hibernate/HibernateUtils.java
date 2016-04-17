package at.fh.ooe.swt6.worklog.manager.service.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Objects;

/**
 * Created by Thomas on 4/17/2016.
 */
public class HibernateUtils {

    private static HibernateUtils instance;
    private static SessionFactory factory;
    private static String CFG;

    private HibernateUtils(SessionFactory factory) {
        Objects.requireNonNull(factory, "Hibernate SessionFactory must not be null");
        this.factory = factory;
    }

    public static HibernateUtils getInstance(String cfg) {
        Objects.requireNonNull(cfg, "Config string must not be null");

        if (instance == null) {
            instance = new HibernateUtils(new Configuration().configure(cfg)
                                                             .buildSessionFactory());
        }

        return instance;
    }

    public Session createSession() {
        return factory.openSession();
    }

    public void disposeSession(Session session) {
        Objects.requireNonNull(session, "Cannot dispose null session");
        session.close();
    }
}
