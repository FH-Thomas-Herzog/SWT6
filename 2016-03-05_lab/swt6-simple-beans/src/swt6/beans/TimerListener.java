package swt6.beans;

import java.util.EventListener;

public interface TimerListener extends EventListener {
	
	void expired(TimerEvent evt);
}
