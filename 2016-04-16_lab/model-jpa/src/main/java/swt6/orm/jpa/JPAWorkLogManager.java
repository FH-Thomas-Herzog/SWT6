package swt6.orm.jpa;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import swt6.orm.domain.annotated.Address;
import swt6.orm.domain.annotated.Employee;
import swt6.orm.domain.annotated.LogbookEntry;
import swt6.util.DateUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.text.DateFormat;
import java.util.List;

public class JPAWorkLogManager {
    private static final DateFormat fmt = DateFormat.getDateTimeInstance();

    static class Test{
        @Getter@Setter
        public String test;
    }

    private static Long saveEmployee(Employee empl) {
        EntityManager em = JPAUtil.getTransactedEntityManager();
        em.persist(empl);
        return empl.getId();
    }

    private static void listEmployees() {
        final EntityManager em = JPAUtil.getEntityManager();
        final List<Employee> result = em.createQuery("SELECT e FROM Employee e", Employee.class).getResultList();
        result.forEach(item -> {
            System.out.println("Employee.id = " + item.getId());
            if (item.getLogbookEntries().size() > 0) {
                System.out.println("  logbookEntries:");
                for (LogbookEntry lbe : item.getLogbookEntries()) {
                    System.out.println("    " + lbe.getActivity() + ": " + lbe.getStartTime() + " - " + lbe.getEndTime());
                }
            }
        });
        JPAUtil.commit();
    }

    private static void addLogbookEntries(Employee empl) {
        EntityManager em = JPAUtil.getTransactedEntityManager();

        empl = em.merge(empl);

        // method 1
        LogbookEntry entry = new LogbookEntry("Analyse", DateUtil.getTime(10, 15), DateUtil.getTime(15, 30));
        entry.attachEmployee(empl);

        // method 2
        empl.addLogbookEntry(new LogbookEntry("Implementierung", DateUtil.getTime(8, 45), DateUtil.getTime(17, 15)));
        empl.addLogbookEntry(new LogbookEntry("Testen", DateUtil.getTime(12, 30), DateUtil.getTime(17, 00)));

        JPAUtil.commit();
    }

    private static void getEmployee(long emplId) {
        final Employee employee = JPAUtil.getEntityManager().find(Employee.class, emplId);

        if (employee != null) {
            System.out.println("Employee '" + emplId + "' loaded");
        }
        JPAUtil.commit();
    }

    private static void getLogbookEntry(long entryId) {
        final LogbookEntry entry = JPAUtil.getEntityManager().find(LogbookEntry.class, entryId);
        if (entry != null) {
            System.out.println("LogbookEntry '" + entryId + "' loaded");
        }
        JPAUtil.commit();
    }

    private static void assignProjectsToEmployees(Employee empl1, Employee empl2) {
        // TODO implement
    }

    private static void listLogbookEntriesOfEmployee(Employee empl1) {
        EntityManager em = JPAUtil.getTransactedEntityManager();

        System.out.println("logbook entries of employee: " + empl1.getLastName() + " (" + empl1.getId() + ")");

        // Keep in mind: JPA-QL queries refer to Java (domain) objects not to
        // database tables.

        // Version 1:
        // Query qry = em.createQuery("from LogbookEntry where employee.id=:emplId")
        // .setParameter("emplId", empl1.getId());

        // Version 2:
        TypedQuery<LogbookEntry> qry = em.createQuery("from LogbookEntry where employee=:emplId", LogbookEntry.class).setParameter("emplId",
                empl1);

        List<LogbookEntry> entries = qry.getResultList();
        for (LogbookEntry entry : entries) {
            System.out.println(entry);
        }
    }

    public static void listEmployeesOfProject(Long projectId) {

    }

    public static void main(String[] args) {
        try {
            System.out.println("----- create schema -----");
            JPAUtil.getEntityManager();

            Employee empl1 = new Employee("Franz", "Mayr", DateUtil.getDate(1980, 12, 24));
            Employee empl2 = new Employee("Bill", "Gates", DateUtil.getDate(1970, 1, 21));
            empl1.setAddress(new Address("4232", "Hagenberg", "Hauptstraße 1"));
            empl2.setAddress(new Address("77777", "Redmond", "Clinton Street"));

            // PermanentEmployee empl1 = new PermanentEmployee("Franz", "Mayr", DateUtil.getDate(1980, 12, 24));
            // empl1.setAddress(new Address("4232", "Hagenberg", "Hauptstraße 1"));
            // empl1.setSalary(5000.0);
            //
            // TemporaryEmployee empl2 = new TemporaryEmployee("Bill", "Gates", DateUtil.getDate(1970, 1, 21));
            // empl2.setAddress(new Address("77777", "Redmond", "Clinton Street"));
            // empl2.setHourlyRate(50.0);
            // empl2.setRenter("Microsoft");
            // empl2.setStartDate(DateUtil.getDate(2006, 3, 1));
            // empl2.setEndDate(DateUtil.getDate(2006, 4, 1));

            System.out.println("----- saveEmployee -----");
            saveEmployee(empl1);

            System.out.println("----- saveEmployee -----");
            saveEmployee(empl2);

            System.out.println("----- getEmployee -----");
            // getEmployee(empl2.getId());

            System.out.println("----- addLogbookEntries -----");
            addLogbookEntries(empl1);

            System.out.println("----- listEmployees -----");
            listEmployees();

            System.out.println("----- getLogbookEntry -----");
            getLogbookEntry(1L);

            System.out.println("----- assignProjectsToEmployees -----");
            assignProjectsToEmployees(empl1, empl2);

            System.out.println("----- listEmployeesOfProject -----");
            listEmployeesOfProject(32768L);
            listEmployeesOfProject(32769L);

            System.out.println("----- listEmployees -----");
            listEmployees();

            System.out.println("----- listLogbookEntriesOfEmployee -----");
            listLogbookEntriesOfEmployee(empl1);
        } finally {
            JPAUtil.closeEntityManagerFactory();
        }

        Test t = new Test();
        t.getTest();
        t.setTest("");
    }

}
