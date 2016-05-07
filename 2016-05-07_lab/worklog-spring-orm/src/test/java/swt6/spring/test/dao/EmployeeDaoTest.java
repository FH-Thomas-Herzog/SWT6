package swt6.spring.test.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import swt6.spring.worklog.dao.EmployeeDao;
import swt6.spring.worklog.domain.Employee;
import swt6.util.DateUtil;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/swt6/spring/test/dao/applicationContext-jpa.xml")
@Transactional(transactionManager = "transactionManagerJpa")
@Rollback
public class EmployeeDaoTest {

    @Autowired
    private EmployeeDao emplDao;

    @Test
    public void testAddEmployee() {
        assertNotNull(emplDao);

        assertNotNull(emplDao);

        Employee empl = new Employee("Franz", "Klammer", DateUtil.getDate(1950, 11, 1));
        assertNull(empl.getId());

        emplDao.save(empl);
        assertNotNull(empl.getId());

        Employee emplCopy = emplDao.findById(empl.getId());
        assertEquals(empl.getLastName(), emplCopy.getLastName());
    } // transaction is rolled back here
}
