package at.fh.ooe.swt6.test.em.model.jpa.schema;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.persistence.Persistence;

/**
 * Created by Thomas on 4/21/2016.
 */
@RunWith(JUnit4.class)
@Slf4j
public class GenerateSchemaTest {

    @Test
    public void testEmSchemaCreation() {
        log.info("Schema creation started");
        Persistence.generateSchema("EmTest", null);
        log.info("Schema creation successful");
    }
}
