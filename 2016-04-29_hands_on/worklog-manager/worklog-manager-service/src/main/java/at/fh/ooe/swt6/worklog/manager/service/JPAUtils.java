package at.fh.ooe.swt6.worklog.manager.service;

import at.fh.ooe.swt6.worklog.manager.model.jpa.*;
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
    private static final String PERSISTENCE_UNIT = "WorklogManager";
    private static EntityManagerFactory emf = null;

    /**
     * Creates the entity manager factory in a static context,
     * because there is no need to create EntityManagerFactory all over again.
     */
    static {
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        } catch (Exception e) {
            log.error("Could not create EntityManagerFactory for PersistenceUnit '{}'",
                      PERSISTENCE_UNIT);
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
        final EntityManager em = getEntityManager();
        final Employee e = new TemporaryEmployee("thomad",
                                                 "herzog",
                                                 Calendar.getInstance(),
                                                 new Address("street",
                                                             "city",
                                                             "9020"),
                                                 BigDecimal.valueOf(1.0),
                                                 false,
                                                 Calendar.getInstance(),
                                                 Calendar.getInstance());
        final Project p = new Project();
        p.setName("Test");

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(e);
        p.setLeader(e);
        em.persist(p);
        final ProjectHasEmployee pe = new ProjectHasEmployee();
        pe.setId(new ProjectHasEmployeeId(p.getId(),
                                          e.getId()));
        em.persist(pe);
        pe.setProject(p);
        tx.commit();
    }
}
