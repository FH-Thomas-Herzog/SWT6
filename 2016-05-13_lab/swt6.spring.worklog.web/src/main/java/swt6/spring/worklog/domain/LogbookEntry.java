package swt6.spring.worklog.domain;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class LogbookEntry implements Serializable, Comparable<LogbookEntry> {
  private static final long serialVersionUID = 1L;
  private static final DateFormat fmt = DateFormat.getDateTimeInstance();

  @Id @GeneratedValue
  private Long     id;
  
  private String   activity;

  @Temporal(TemporalType.TIME)
  private Date     startTime;
  
  @Temporal(TemporalType.TIME)
  private Date     endTime;
  
  @ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE}, fetch=FetchType.EAGER)
  private Employee employee;
  
  public LogbookEntry() {
  }
  
  public LogbookEntry(String activity, Date start, Date end) {
    this.activity = activity;
    this.startTime = start;
    this.endTime = end;
  }
  
  @Id @GeneratedValue
  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public String getActivity() {
    return activity;
  }
  
  public void setActivity(String activity) {
    this.activity = activity;
  }
  
  @ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
  public Employee getEmployee() {
    return employee;
  }
  
  // setEmployee is also invoked when log entries are being
  // loaded lazily. This cases an exception.
  public void setEmployee(Employee employee) {
    this.employee = employee;
  }
  
  public void attachEmployee(Employee employee) {
    // If this entry is already linked to some employee,
    // remove this link.
    if (this.employee != null)
      this.employee.getLogbookEntries().remove(this);
    
    // Add a bidirection link between this entry and employee.
    if (employee != null)
       employee.getLogbookEntries().add(this);
    this.employee = employee;
  }

  public void detachEmployee() {
    if (this.employee != null)
      this.employee.getLogbookEntries().remove(this);
    
    this.employee = null;
  }

  public Date getEndTime() {
    return endTime;
  }
  
  public void setEndTime(Date end) {
    this.endTime = end;
  }
  
  public Date getStartTime() {
    return startTime;
  }
  
  public void setStartTime(Date start) {
    this.startTime = start;
  }
  
  @Override
  public String toString() {
	String emplName = 
		getEmployee() == null ? "<null>" : getEmployee().getLastName();
    return activity + ": " 
           + fmt.format(startTime) + " - " 
           + fmt.format(endTime) + " ("
           + emplName + ")";
  }

  public int compareTo(LogbookEntry entry) {
    return this.startTime.compareTo(entry.startTime);
  }
}
