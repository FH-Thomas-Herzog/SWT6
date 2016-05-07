package swt6.spring.worklog.dao.jpa;

import org.springframework.dao.DataAccessException;
import swt6.spring.worklog.dao.EmployeeDao;
import swt6.spring.worklog.domain.Employee;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class EmployeeDaoJpa implements EmployeeDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Employee> findAll() throws DataAccessException {
        return em.createQuery("SELECT e FROM Employee e", Employee.class).getResultList();
    }

    @Override
    public Employee findById(Long id) throws DataAccessException {
        return em.find(Employee.class, id);
    }

    @Override
    public void save(Employee e) throws DataAccessException {
        em.persist(e);
    }

    @Override
    public Employee merge(Employee empl) throws DataAccessException {
        return em.merge(empl);
    }
}
