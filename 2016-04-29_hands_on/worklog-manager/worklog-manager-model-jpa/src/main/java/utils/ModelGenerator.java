package utils;

import at.fh.ooe.swt6.worklog.manager.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Thomas on 4/17/2016.
 */
public class ModelGenerator {

    private ModelGenerator() {

    }

    /**
     * Creates predefined LogbookEntry instance for the given entities;
     *
     * @param employees the entity to create entries for
     * @param phases    the pahses to create entries for
     * @param modules   the modules to create entries for
     * @param count     the count of entries for each entity
     * @return the created entries
     */
    public static List<LogBookEntry> createLogbookEntries(final List<? extends Employee> employees,
                                                          final List<Phase> phases,
                                                          final List<Module> modules,
                                                          final int count) {
        final List<LogBookEntry> entries = new ArrayList<>(employees.size() * phases.size() * modules.size() * count);

        employees.forEach(employee -> phases.forEach(phase -> modules.forEach(module -> {
            for (int i = 1; i <= count; i++) {
                entries.add(new LogBookEntry(("bla_" + i),
                                             Calendar.getInstance()
                                                     .getTime(),
                                             Calendar.getInstance()
                                                     .getTime(),
                                             employee,
                                             phase,
                                             module));
            }
        })));

        return entries;
    }

    /**
     * Creates the project.
     *
     * @param name      the project name
     * @param leader    the leader of the project
     * @param employees the employees working on this project
     * @return the created project.
     */
    public static Project createProject(final String name,
                                        final Employee leader,
                                        final List<? extends Employee> employees) {
        final Project project = new Project(name, leader);
        project.setProjectEmployees(employees.stream()
                                             .map(item -> new ProjectEmployee(project, item))
                                             .collect(
                                                     Collectors.toSet()));

        return project;
    }

    /**
     * Creates the predefined phases.
     *
     * @return the list of predefined phases
     */
    public static List<Phase> createPhases() {
        final List<Phase> phases = new ArrayList<>();
        phases.add(new Phase("Konzeption"));
        phases.add(new Phase("Plannung"));
        phases.add(new Phase("Prototype"));
        phases.add(new Phase("Implementierung"));
        phases.add(new Phase("Tests"));
        phases.add(new Phase("Dokumentation"));

        return phases;
    }

    /**
     * Creates the predefined modules.
     *
     * @return the list of predefined modules
     */
    public static List<Module> createModules(Project project) {
        final List<Module> modules = new ArrayList<>();
        modules.add(new Module("GUI", project));
        modules.add(new Module("REST-Services", project));
        modules.add(new Module("Database Access", project));
        modules.add(new Module("Service", project));
        modules.add(new Module("Testdata", project));
        modules.add(new Module("Database", project));

        return modules;
    }

    /**
     * Creates count times predefined PermanentEmployee instances.
     *
     * @param count the count of employees to create
     * @return the created employees
     */
    public static List<PermanentEmployee> createPermanatEmployees(int count) {
        final List<PermanentEmployee> employees = new ArrayList<>(count);
        for (int i = 1; i <= count; i++) {
            employees.add(new PermanentEmployee(("FirstName_" + i),
                                                ("Lastname_" + i),
                                                Calendar.getInstance(),
                                                new Address(("street_" + i), ("city" + i), ("zipCode_" + i)),
                                                BigDecimal.ONE));
        }

        return employees;
    }

    /**
     * Creates count times predefined TemporaryEmployee instances.
     *
     * @param count the count of employees to create
     * @return the created employees
     */
    public static List<TemporaryEmployee> createTemporaryEmployees(int count) {
        final List<TemporaryEmployee> employees = new ArrayList<>(count);
        for (int i = 1; i <= count; i++) {
            employees.add(new TemporaryEmployee(("FirstName_" + i),
                                                ("Lastname_" + i),
                                                Calendar.getInstance(),
                                                new Address(("street_" + i), ("city" + i), ("zipCode_" + i)),
                                                BigDecimal.ONE,
                                                Boolean.TRUE,
                                                Calendar.getInstance(),
                                                Calendar.getInstance()));
        }

        return employees;
    }
}
