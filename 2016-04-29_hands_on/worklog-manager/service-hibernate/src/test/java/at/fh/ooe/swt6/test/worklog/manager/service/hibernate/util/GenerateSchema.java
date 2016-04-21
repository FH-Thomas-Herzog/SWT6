package at.fh.ooe.swt6.test.worklog.manager.service.hibernate.util;

import org.hibernate.cfg.Configuration;

/**
 * Created by Thomas on 4/21/2016.
 */
public class GenerateSchema {

    public static void main(String args[]) {
        new Configuration().configure("hibernate.test.cfg.xml")
                           .buildSessionFactory();
    }
}
