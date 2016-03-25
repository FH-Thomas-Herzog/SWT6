package at.fh.ooe.swt6.drive.analytics.util;

import java.util.EventObject;

/**
 * This class represent the event object the timer populates to the registered
 * event listener when the timer has expired.
 * 
 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
 * @date Mar 25, 2016
 */
public class TimerEvent extends EventObject {

	private static final long serialVersionUID = 3831514489602741896L;

	public TimerEvent(Object source) {
		super(source);
	}

}
