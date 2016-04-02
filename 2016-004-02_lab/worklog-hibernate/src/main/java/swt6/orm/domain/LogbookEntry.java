package swt6.orm.domain;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

public class LogbookEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String activity;
	private Date startTime;
	private Date endTime;
	private Employee employee;

	public LogbookEntry() {
	}

	public LogbookEntry(String activity, Date start, Date end) {
		this.activity = activity;
		this.startTime = start;
		this.endTime = end;
	}

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public Employee getEmployee() {
		return employee;
	}

	// setEmployee is also invoked when logbook entries are being
	// loaded lazily. This causes an exception.
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public void attachEmployee(Employee employee) {
		Objects.requireNonNull(employee, "Cannot attach null employy");
		if (!Objects.isNull(this.employee)) {
			detachEmployee();
		}
		this.employee = employee;
		this.employee.getLogbookEntries().add(this);
	}

	public void detachEmployee() {
		Objects.requireNonNull(employee, "Entry not attached to any employee");
		employee.getLogbookEntries().remove(this);
		employee = null;
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
		DateFormat fmt = DateFormat.getDateTimeInstance();
		return activity + ": " + fmt.format(startTime) + " - " + fmt.format(endTime) + " ("
				+ getEmployee().getLastName() + ")";

	}
}
