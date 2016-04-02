package swt6.orm.util;

import org.hibernate.SessionFactory;
import org.hibernate.SessionFactoryObserver;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateHelper {
	public static SessionFactory buildSessionFactory() {
		return buildSessionFactory("hibernate.cfg.xml");
	}

	@SuppressWarnings("serial")
	public static SessionFactory buildSessionFactory(String configFile) {
		// variant 1: simplest but deprecated
		// Configuration configuration =
		// new Configuration().configure(configFile);
		// return configuration.buildSessionFactory();

		// variant 2:
		// ServiceRegistry registry =
		// new StandardServiceRegistryBuilder().configure(configFile).build();
		// return
		// new MetadataSources(registry).buildMetadata().buildSessionFactory();

		// variant 3:

		Configuration configuration = new Configuration().configure(configFile);

		final ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).build();

		configuration.setSessionFactoryObserver(new SessionFactoryObserver() {
			@Override
			public void sessionFactoryCreated(SessionFactory factory) {
			}

			@Override
			public void sessionFactoryClosed(SessionFactory factory) {
				StandardServiceRegistryBuilder.destroy(serviceRegistry);
			}
		});

		return configuration.buildSessionFactory(serviceRegistry);
	}
}
