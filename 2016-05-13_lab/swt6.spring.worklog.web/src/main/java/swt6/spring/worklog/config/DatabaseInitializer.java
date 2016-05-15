package swt6.spring.worklog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import swt6.spring.worklog.domain.Employee;
import swt6.spring.worklog.domain.LogbookEntry;
import swt6.spring.worklog.logic.WorkLogFacade;
import swt6.util.DateUtil;

public class DatabaseInitializer implements CommandLineRunner {

  @Autowired
  private WorkLogFacade workLog;

  @Override
  public void run(String... args) throws Exception {

    Employee empl1 = new Employee("Sepp", "Forcher", DateUtil.getDate(1935, 12, 12));
    empl1.addLogbookEntry(new LogbookEntry("Jour Fixe", DateUtil.getTime(8, 15), DateUtil.getTime(10, 0)));
    empl1.addLogbookEntry(new LogbookEntry("Analyse", DateUtil.getTime(10, 0), DateUtil.getTime(13, 45)));
    empl1.addLogbookEntry(new LogbookEntry("Implementierung", DateUtil.getTime(10, 15), DateUtil.getTime(14, 30)));
    workLog.syncEmployee(empl1);

    Employee empl2 = new Employee("Alfred", "Kunz", DateUtil.getDate(1944, 8, 10));
    empl2.addLogbookEntry(new LogbookEntry("Jour Fixe", DateUtil.getTime(8, 15), DateUtil.getTime(10, 0)));
    empl2.addLogbookEntry(new LogbookEntry("Unit-Test schreiben", DateUtil.getTime(10, 15), DateUtil.getTime(14, 30)));
    empl2.addLogbookEntry(new LogbookEntry("Integations-Tests wiederholen", DateUtil.getTime(14, 30), DateUtil.getTime(16, 00)));
    workLog.syncEmployee(empl2);

    Employee empl3 = new Employee("Sigfried", "Hinz", DateUtil.getDate(1954, 5, 3));
    empl3.addLogbookEntry(new LogbookEntry("Jour Fixe", DateUtil.getTime(8, 15), DateUtil.getTime(10, 0)));
    empl3.addLogbookEntry(new LogbookEntry("Benutzerdoku aktualisieren", DateUtil.getTime(8, 15), DateUtil.getTime(16, 30)));
    workLog.syncEmployee(empl3);
  }
}