package swt6.beans;

import java.util.EventObject;

public class TimerEvent extends EventObject {

	private static final long serialVersionUID = 6165694680914489099L;

	private final int numTicks;
	private final int tickCount;

	public TimerEvent(Object arg0, int numTicks, int tickCount) {
		super(arg0);

		this.numTicks = numTicks;
		this.tickCount = tickCount;
	}

	@Override
	public Object getSource() {
		return super.getSource();
	}

	// #####################################
	// Getter and Setter
	// #####################################
	public int getNumTicks() {
		return numTicks;
	}

	public int getTickCount() {
		return tickCount;
	}
}
