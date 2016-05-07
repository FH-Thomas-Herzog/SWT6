package swt6.spring.worklog.dao.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import swt6.spring.worklog.dao.EmployeeDao;
import swt6.spring.worklog.domain.Employee;
import swt6.util.DateUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class EmployeeDaoJdbc extends JdbcDaoSupport implements EmployeeDao {

  // Can be shared between different read queries.
  protected static class EmployeeRowMapper implements RowMapper<Employee> {
    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
      Employee empl = new Employee();

      empl.setId(rs.getLong(1));
      empl.setFirstName(rs.getString(2));
      empl.setLastName(rs.getString(3));
      empl.setDateOfBirth(rs.getDate(4));

      return empl;
    }
  }

  // version 1: Data access code without spring
  public void save1(final Employee e) throws DataAccessException {
    final String sql = "insert into Employee (firstName, lastName, dateofbirth) " + "values (?, ?, ?)";
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = getDataSource().getConnection();
      stmt = conn.prepareStatement(sql);
      stmt.setString(1, e.getFirstName());
      stmt.setString(2, e.getLastName());
      stmt.setDate(3, DateUtil.toSqlDate(e.getDateOfBirth()));
      stmt.executeUpdate();
    } catch (SQLException ex) {
      System.err.println(ex);
    } finally {
      try {
        if (stmt != null) {
          stmt.close();
        }
      } catch (SQLException ex) {
        System.err.println(ex);
      }

      try {
        if (conn != null) {
          conn.close();
        }
      } catch (SQLException ex) {
        System.err.println(ex);
      }
    }
  }

  @Override
  public void save(final Employee e) throws DataAccessException {
    final String sql = "insert into Employee (firstName, lastName, dateofbirth) " + "values (?, ?, ?)";

    KeyHolder keyHolder = new GeneratedKeyHolder();
    getJdbcTemplate().update(conn -> {
      PreparedStatement ps = conn.prepareStatement(sql, new String[] { "id" });
      ps.setString(1, e.getFirstName());
      ps.setString(2, e.getLastName());
      ps.setDate(3, DateUtil.toSqlDate(e.getDateOfBirth()));
      return ps;
    } , keyHolder);
    e.setId(keyHolder.getKey().longValue());
  }

  // version 3 (Java 8): same as version 3a but with lambda expression.
  public void save3(final Employee e) throws DataAccessException {
    final String sql = "insert into Employee (firstName, lastName, dateofbirth) " + "values (?, ?, ?)";

    getJdbcTemplate().update(sql, ps -> {
      ps.setString(1, e.getFirstName());
      ps.setString(2, e.getLastName());
      ps.setDate(3, DateUtil.toSqlDate(e.getDateOfBirth()));
    });
  }

  // version 4: The actual parameters are pass to the update method,
  // no callback handler is needed
  // not suitable if you need the generated key.
  public void save4(Employee e) throws DataAccessException {
    String sql = "insert into Employee (firstName, lastName, dateofbirth) " + "values (?, ?, ?)";
    Object[] params = new Object[] { e.getFirstName(), e.getLastName(), e.getDateOfBirth() };
    getJdbcTemplate().update(sql, params);
  }

  // Version 1: Fetch all employees using a RowMapper.
  @Override
  public List<Employee> findAll() throws DataAccessException {
    final String sql = "select id, firstName, lastName, dateofbirth from Employee";
    return getJdbcTemplate().query(sql, new EmployeeRowMapper());
  }

  public Employee findById1(final Long id) throws DataAccessException {
    final String sql = "select id, firstName, lastName, dateofbirth from Employee" + " where id = ?";

    final Employee empl = new Employee();
    getJdbcTemplate().query(sql, new Object[] { id }, (ResultSet rs) -> {
      empl.setId(rs.getLong(1));
      empl.setFirstName(rs.getString(2));
      empl.setLastName(rs.getString(3));
      empl.setDateOfBirth(rs.getDate(4));
    });
    return empl.getId() == null ? null : empl;
  }

  // version 2: Fetch employee using the RowMapper class.
  public Employee findById2(final Long id) throws DataAccessException {
    final String sql = "select id, firstName, lastName, dateofbirth from Employee" + " where id = ?";
    final Object[] params = { id };
    List<Employee> emplList = getJdbcTemplate().query(sql, params, new EmployeeRowMapper());
    return emplList.size() == 0 ? null : emplList.get(0);
  }

  // version 3: Fetch employee using a RowMapper
  @Override
  public Employee findById(final Long id) throws DataAccessException {
    final String sql = "select id, firstName, lastName, dateofbirth from Employee" + " where id = ?";
    final Object[] params = { id };
    try {
      return getJdbcTemplate().queryForObject(sql, params, new EmployeeRowMapper());
    } catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  @Override
  public Employee merge(Employee empl) throws DataAccessException {
    this.save(empl);
    return empl;
  }

}
