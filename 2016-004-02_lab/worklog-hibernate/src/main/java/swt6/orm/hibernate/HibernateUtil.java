package swt6.orm.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import swt6.orm.util.HibernateHelper;

public class HibernateUtil {

	private static SessionFactory sessionFactory;

	// Creating a SessionFactory ist time consuming. This should
	// be done only once per database. SessionFactory is thread-safe.
	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null)
			sessionFactory = HibernateHelper.buildSessionFactory();
		// buildSessionFactory is back in Hibernate 5.
		// sessionFactory = new Configuration().configure("hibernate.cfg.xml")
		// .buildSessionFactory();
		return sessionFactory;
	}

	public static void closeSessionFactory() {
		if (sessionFactory != null) {
			sessionFactory.close();
			sessionFactory = null;
		}
	}

	// Version 1: only works for a single threaded application
	private static Session session;

	public static Session getSession() {
		if (session == null)
			session = getSessionFactory().openSession();
		return session;
	}

	// Version 1:
	public static void closeSession() {
		if (session != null)
			session.close();
		session = null;
	}

	// Version 2:
	// Session is a lightweight component, but it isn't thread-safe.
	// Therefore, a separate connection has to be opened for each connection.
	// Hibernate 3 offers a tailor-made solution for this task:
	// getCurrentSession() delivers a different session for each thread.
	// Don't forget to set the property
	// hibernate.current_session_context_class = thread.
	// Moreover, the session is closed if the associated transaction is
	// committed or rollbacked.
	public static Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}
}
