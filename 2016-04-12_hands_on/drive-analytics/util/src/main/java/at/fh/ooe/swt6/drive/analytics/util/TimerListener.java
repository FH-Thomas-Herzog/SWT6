package at.fh.ooe.swt6.drive.analytics.util;

import java.util.EventListener;

/**
 * This interface specifies an {@link Timer} listener instance.
 * 
 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
 * @date Mar 25, 2016
 */
public interface TimerListener extends EventListener {

	/**
	 * Called when the time were this listener were registered has expired.
	 * 
	 * @param event
	 *            the event object provided by the timer.
	 */
	void expired(TimerEvent event);

}
