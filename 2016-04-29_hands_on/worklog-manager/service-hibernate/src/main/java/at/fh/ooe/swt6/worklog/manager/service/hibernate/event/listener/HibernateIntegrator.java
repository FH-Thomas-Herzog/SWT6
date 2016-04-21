package at.fh.ooe.swt6.worklog.manager.service.hibernate.event.listener;

import at.fh.ooe.swt6.worklog.manager.model.api.BaseEntity;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.internal.DefaultSaveOrUpdateEventListener;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventSource;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.hibernate.event.spi.SaveOrUpdateEventListener;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

import java.io.Serializable;

/**
 * Created by Thomas on 4/21/2016.
 */
public class HibernateIntegrator implements Integrator {

    @Override
    public void integrate(Configuration configuration,
                          SessionFactoryImplementor sessionFactory,
                          SessionFactoryServiceRegistry serviceRegistry) {
        final EventListenerRegistry eventListenerRegistry = serviceRegistry.getService(EventListenerRegistry.class);
        eventListenerRegistry.setListeners(EventType.SAVE, PrePersistListener.class);
        eventListenerRegistry.setListeners(EventType.UPDATE, PrePersistListener.class);
        eventListenerRegistry.setListeners(EventType.SAVE_UPDATE, PrePersistListener.class);
    }

    @Override
    public void integrate(MetadataImplementor metadata,
                          SessionFactoryImplementor sessionFactory,
                          SessionFactoryServiceRegistry serviceRegistry) {

    }

    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactory,
                             SessionFactoryServiceRegistry serviceRegistry) {

    }

    public static final class PrePersistListener extends DefaultSaveOrUpdateEventListener {
        public PrePersistListener() {
        }

        @Override
        protected Serializable performSave(Object entity,
                                           Serializable id,
                                           EntityPersister persister,
                                           boolean useIdentityColumn,
                                           Object anything,
                                           EventSource source,
                                           boolean requiresImmediateIdAccess) {
            if (entity != null) {
                ((BaseEntity<?>) entity).prePersist();
            }
            return super.performSave(entity,
                                     id,
                                     persister,
                                     useIdentityColumn,
                                     anything,
                                     source,
                                     requiresImmediateIdAccess);
        }

        @Override
        protected void performUpdate(SaveOrUpdateEvent event,
                                     Object entity,
                                     EntityPersister persister) throws HibernateException {
            if (entity != null) {
                ((BaseEntity<?>) entity).preUpdate();
            }
            super.performUpdate(event, entity, persister);
        }
    }

    public static final class PreUpdateListener implements SaveOrUpdateEventListener {
        public PreUpdateListener() {
        }

        @Override
        public void onSaveOrUpdate(SaveOrUpdateEvent event) throws HibernateException {
            final BaseEntity<?> entity = (BaseEntity<?>) event.getObject();
            if (entity != null) {
                entity.preUpdate();
            }
        }

    }
}
