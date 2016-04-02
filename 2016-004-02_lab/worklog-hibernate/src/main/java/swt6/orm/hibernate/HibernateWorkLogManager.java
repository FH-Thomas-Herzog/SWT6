package swt6.orm.hibernate;

import java.text.DateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import swt6.orm.domain.Address;
import swt6.orm.domain.Employee;
import swt6.orm.domain.LogbookEntry;
import swt6.orm.domain.PermanentEmployee;
import swt6.orm.domain.Project;
import swt6.util.DateUtil;

public class HibernateWorkLogManager {

	private static final DateFormat fmt = DateFormat.getDateTimeInstance();

	@SuppressWarnings("unused")
	private static Long saveEmployee(Employee empl) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();

		session.saveOrUpdate(empl);

		tx.commit(); // session is closed automatically

		return empl.getId();
	}

	private static <T> void saveEntity(T entity) {
		Session session = HibernateUtil.getCurrentSession();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			session.saveOrUpdate(entity);

			tx.commit(); // session is closed automatically
		} catch (Exception e) {
			e.printStackTrace();
			if (!Objects.isNull(tx)) {
				tx.rollback();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void listEmployees() {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			List<Employee> emplList = session.createQuery("select e from Employee e").list();
			for (Employee e : emplList) {
				System.out.println(e);

				if (e.getLogbookEntries().size() > 0) {
					System.out.println("  logbookEntries:");
					for (LogbookEntry lbe : e.getLogbookEntries()) {
						System.out.println(
								"    " + lbe.getActivity() + ": " + lbe.getStartTime() + " - " + lbe.getEndTime());
					}
				}

				if (e.getProjects().size() > 0) {
					System.out.println("  projects:");
					for (Project p : e.getProjects()) {
						System.out.println("    " + p.toString());
					}
				}
			}

			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			if (!Objects.isNull(tx)) {
				tx.rollback();
			}
		}
	}

	private static Employee updateEmployee(Employee empl) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();

		session.get(Employee.class, empl.getId());

		empl.setLastName("Mayr-Huber");

		empl = (Employee) session.merge(empl);

		tx.commit();

		return empl;
	}

	private static Employee addLogbookEntries(Employee empl, LogbookEntry... entries) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();

		for (LogbookEntry entry : entries) {
			empl.addLogbookEntry(entry);
		}

		session.saveOrUpdate(empl);

		tx.commit();

		return empl;
	}

	private static void getEmployee(long emplId) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();

		Employee empl = (Employee) session.load(Employee.class, emplId);
		// Employee empl = (Employee)session.get(Employee.class, emplId);
		if (empl != null) {
			System.out.println(empl);
			System.out.println("  logbookEntries:");
			for (LogbookEntry lbe : empl.getLogbookEntries()) {
				System.out.println("    " + lbe.getActivity() + ": " + fmt.format(lbe.getStartTime()) + " - "
						+ fmt.format(lbe.getEndTime()));
			}
		}

		tx.commit();
	}

	private static void getLogbookEntry(long entryId) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();

		LogbookEntry entry = (LogbookEntry) session.get(LogbookEntry.class, entryId);
		if (entry != null) {
			System.out.println("LogbookEntry " + entryId + " --> " + entry.getEmployee());
		}

		tx.commit();
	}

	private static Employee assignProjectsToEmployee(Employee empl, Project... projects) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();

		empl = (Employee) session.merge(empl);
		for (Project project : projects) {
			empl.addProject(project);
		}

		tx.commit();

		return empl;
	}

	@SuppressWarnings("unchecked")
	private static void listLogbookEntriesOfEmployee(Employee empl1) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();

		System.out.println("logbook entries of employee: " + empl1.getLastName() + " (" + empl1.getId() + ")");

		// Keep in mind: HQL queries refer to Java (domain) objects not to
		// database tables.

		// Version 1: Concatenate SQL query string manually
		// Query qry = session.createQuery("from LogbookEntry where employee.id
		// = " +
		// empl1.getId());
		// Iterator<LogbookEntry> it = qry.iterate();

		// Version 2: Use unnamed SQL parameters
		// Query qry = session.createQuery("from LogbookEntry where
		// employee.id=?");
		// qry.setLong(0, empl1.getId());
		// Iterator<LogbookEntry> it = qry.iterate();

		// Version 3: Use named SQL parameters
		// Query qry =
		// session.createQuery("from LogbookEntry where employee.id=:emplId");
		// qry.setLong("emplId", empl1.getId());
		// Iterator<LogbookEntry> it = qry.iterate();

		// Version 4: Parameters can refer to domain objects
		Query qry = session.createQuery("select le from LogbookEntry le where employee=:empl");
		qry.setEntity("empl", empl1);
		Iterator<LogbookEntry> it = qry.iterate();

		// Version 5: Use Hibernate criteria API
		// Criteria crit = session.createCriteria(LogbookEntry.class);
		// crit.add(Restrictions.eq("employee", empl1));
		// Iterator<LogbookEntry> it = crit.list().iterator();

		while (it.hasNext()) {
			System.out.println(it.next());
		}

		tx.commit();
	}

	@SuppressWarnings("unchecked")
	private static void testFetchingStrategies() {
		// prepare: fetch valid ids for employee and logbookentry

		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();

		Long entryId = null;
		Long emplId = null;
		try {
			Iterator<LogbookEntry> entryIt = session.createQuery("select le from LogbookEntry le").setMaxResults(1)
					.iterate();
			if (!entryIt.hasNext()) {
				return;
			}
			entryId = entryIt.next().getId();

			Iterator<Employee> emplIt = session.createQuery("select e from Employee e").setMaxResults(1).iterate();
			if (!emplIt.hasNext()) {
				return;
			}
			emplId = emplIt.next().getId();
		} finally {
			tx.commit();
		}

		System.out.println("############################################");

		session = HibernateUtil.getCurrentSession();
		tx = session.beginTransaction();

		System.out.println("###> Fetching LogbookEntry ...");
		LogbookEntry entry = (LogbookEntry) session.get(LogbookEntry.class, entryId);
		System.out.println("###> Fetched LogbookEntry");
		Employee empl1 = entry.getEmployee();
		System.out.println("###> Fetched associated Employee");
		System.out.println(empl1);
		System.out.println("###> Accessed associated Employee");

		tx.commit();

		System.out.println("############################################");

		session = HibernateUtil.getCurrentSession();
		tx = session.beginTransaction();

		System.out.println("###> Fetching Employee ...");
		Employee empl2 = (Employee) session.get(Employee.class, emplId);
		System.out.println("###> Fetched Employee");
		Set<LogbookEntry> entries = empl2.getLogbookEntries();
		System.out.println("###> Fetched associated entries");
		for (LogbookEntry e : entries) {
			System.out.println("  " + e);
		}
		System.out.println("###> Accessed associated entries");

		tx.commit();

		System.out.println("############################################");
	}

	@SuppressWarnings("unused")
	private static void deleteEmployee(Employee empl) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();

		empl = (Employee) session.merge(empl);

		for (LogbookEntry entry : empl.getLogbookEntries()) {
			session.delete(entry);
		}

		for (Project project : empl.getProjects()) {
			session.delete(project);
		}

		empl.detach();
		session.delete(empl);

		tx.commit();
	}

	public static void testCascade() {
		Employee empl1 = new Employee("Franz", "Mayr", DateUtil.getDate(1980, 12, 24),
				new Address("4232", "Hagenberg", "Hauptstraße 1"));
		Employee empl2 = new Employee("Fritzi", "Huber", DateUtil.getDate(1980, 12, 24),
				new Address("4232", "Hagenberg", "Hauptstraße 1"));
		saveEntity(empl1);
		saveEntity(empl2);

		LogbookEntry e1 = new LogbookEntry("Analysis", DateUtil.getTime(13, 0), DateUtil.getTime(14, 0));
		LogbookEntry e2 = new LogbookEntry("Design", DateUtil.getTime(14, 5), DateUtil.getTime(17, 0));

		addLogbookEntries(empl1, e1, e2);
		addLogbookEntries(empl2, e1);

		saveEntity(e1);
		saveEntity(e2);
	}

	public static void main(String[] args) {
		try {
			System.out.println("----- create schema -----");
			HibernateUtil.getSessionFactory();

			// Employee empl1 = new Employee("Franz", "Mayr",
			// DateUtil.getDate(1980, 12, 24));
			PermanentEmployee pe = new PermanentEmployee("Franz", "Mayr", DateUtil.getDate(1980, 12, 24));
			Employee empl1 = pe;
			empl1.setAddress(new Address("4232", "Hagenberg", "Hauptstraße 1"));
			// pe.setSalary(5000.0);

			// Employee empl2 = new Employee("Bill", "Gates",
			// DateUtil.getDate(1970, 1, 21));
			// TemporaryEmployee te = new TemporaryEmployee("Bill", "Gates",
			// DateUtil.getDate(1970, 1, 21));
			// Employee empl2 = te;
			// empl2.setAddress(new Address("77777", "Redmond", "Clinton
			// Street"));
			// te.setHourlyRate(50.0);
			// te.setRenter("Microsoft");
			// te.setStartDate(DateUtil.getDate(2006, 3, 1));
			// te.setEndDate(DateUtil.getDate(2006, 4, 1));

			LogbookEntry entry1 = new LogbookEntry("Analyse", DateUtil.getTime(10, 15), DateUtil.getTime(15, 30));
			LogbookEntry entry2 = new LogbookEntry("Implementierung", DateUtil.getTime(8, 45),
					DateUtil.getTime(17, 15));
			LogbookEntry entry3 = new LogbookEntry("Testen", DateUtil.getTime(12, 30), DateUtil.getTime(17, 00));

			// Project p1 = new Project("Office");
			// Project p2 = new Project("Enterprise Server");

			System.out.println("----- saveEmployee -----");
			saveEntity(empl1);

			System.out.println("----- saveEmployee -----");
			// saveEntity(empl2);

			System.out.println("----- updateEmployee -----");
			updateEmployee(empl1);

			System.out.println("----- addLogbookEntries -----");
			empl1 = addLogbookEntries(empl1, entry1, entry2);
			// empl2 = addLogbookEntries(empl2, entry3);

			System.out.println("----- listEmployees -----");
			listEmployees();

			System.out.println("----- getEmployee -----");
			// getEmployee(empl1.getId());

			System.out.println("----- getLogbookEntry -----");
			// getLogbookEntry(1L);

			System.out.println("----- assignProjectsToEmployees -----");
			// empl1 = assignProjectsToEmployee(empl1, p1, p2);
			// empl2 = assignProjectsToEmployee(empl2, p2);

			System.out.println("----- listEmployees -----");
			// listEmployees();

			// System.out.println("----- deleteEmployee -----");
			// deleteEmployee(empl2);

			System.out.println("----- listEmployees -----");
			// listEmployees();

			System.out.println("----- listLogbookEntriesOfEmployee -----");
			// listLogbookEntriesOfEmployee(empl1);

			System.out.println("----- testFetchingStrategies ------");
			// testFetchingStrategies();

		} finally {
			HibernateUtil.closeSessionFactory();
		}
	}
}