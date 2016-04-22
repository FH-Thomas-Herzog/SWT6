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
     * Creates the predefined phases.
     *
     * @param count the count of phases to create
     * @return the list of created phases
     */
    public static List<Phase> createPhases(int count) {
        final List<Phase> phases = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            phases.add(new Phase(("Phase_" + i)));
        }

        return phases;
    }

    /**
     * Creates the predefined modules.
     *
     * @param count the count of modules to create
     * @return the list of created modules
     */
    public static List<Module> createModules(int count, Project project) {
        final List<Module> modules = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            modules.add(new Module(("MODULE_" + i), project));
        }

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

    /**
     * Creates a project for each leader and assigns all project employees to all projects.
     *
     * @param projectLeader    the leader of the project
     * @param projectEmployees the employees working on the project
     * @return the created projects
     */
    public static List<Project> createProjects(final List<? extends Employee> projectLeader,
                                               final List<? extends Employee> projectEmployees) {
        return projectLeader.stream().map(item -> {
            final Project project = new Project("project_" + item.getId(), item);
            project.getProjectEmployees().addAll(projectEmployees);
            return project;
        }).collect(Collectors.toList());
    }
}
