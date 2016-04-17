package at.fh.ooe.swt6.worklog.manager.service.jpa;

import at.fh.ooe.swt6.worklog.manager.model.Address;
import at.fh.ooe.swt6.worklog.manager.model.Employee;
import at.fh.ooe.swt6.worklog.manager.model.Project;
import at.fh.ooe.swt6.worklog.manager.model.TemporaryEmployee;
import at.fh.ooe.swt6.worklog.manager.service.api.DataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Objects;

/**
 * This class holds the JPA utilities for managing EntityManager and EntityMangerFactory instances.
 * <p>
 * Created by Thomas on 4/16/2016.
 */
public class JPAUtils {

    private static final Logger log = LoggerFactory.getLogger(JPAUtils.class);
    private static final String PERSISTENCE_UNIT_DEV = "WorklogManager";
    private static final String PERSISTENCE_UNIT_TEST = "WorklogManagerTest";
    private static final String CURRENT_PERSISTENT_UNIT = PERSISTENCE_UNIT_DEV;
    private static EntityManagerFactory emf = null;

    /**
     * Creates the entity manager factory in a static context,
     * because there is no need to create EntityManagerFactory all over again.
     */
    static {
        try {
            emf = Persistence.createEntityManagerFactory(CURRENT_PERSISTENT_UNIT);
            Persistence.generateSchema(CURRENT_PERSISTENT_UNIT, null);
        } catch (Exception e) {
            log.error("Could not create EntityManagerFactory for PersistenceUnit '{}'",
                      CURRENT_PERSISTENT_UNIT);
            log.error("Thrown exception: ",
                      e);
        }
    }

    /**
     * Gets an EntityManager from the EntityManagerFactory.
     *
     * @return the entity manager instance
     */
    public static EntityManager getEntityManager() {
        Objects.requireNonNull(emf,
                               "EntityManagerFactory hasn't been initialized");
        return emf.createEntityManager();
    }

    public static void main(String args[]) {

        try {
            final EntityManager em = getEntityManager();
            final DataManager dataManager = new JPADataManager();
            Employee e = new TemporaryEmployee("thomad",
                                               "herzog",
                                               Calendar.getInstance(),
                                               new Address("street",
                                                           "city",
                                                           "9020"),
                                               BigDecimal.valueOf(1.0),
                                               false,
                                               Calendar.getInstance(),
                                               Calendar.getInstance());
            Project p = new Project();
            p.setName("Test");

            final EntityTransaction tx = em.getTransaction();
            tx.begin();

            tx.commit();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
