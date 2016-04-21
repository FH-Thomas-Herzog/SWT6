package at.fh.ooe.swt6.test.worklog.manager.util;

import at.fh.ooe.swt6.worklog.manager.service.jpa.JPADataManagerProvider;

import javax.persistence.Persistence;

/**
 * Created by Thomas on 4/21/2016.
 */
public class GenerateSchema {

    public static void main(String args[]) {
        Persistence.generateSchema(JPADataManagerProvider.PERSISTENCE_UNIT, null);
    }
}
