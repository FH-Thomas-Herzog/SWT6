/**
 * 
 */
package at.fh.ooe.swt6.drive.analytics.ui.favafx.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

/**
 * @author Thomas Herzog <S1310307011@students.fh-hagenberg.at>
 * @date Mar 25, 2016
 */
public class JavaFXUtils {

	private static JFXPanel jFxPanel;

	public static void initJavaFx() {
		if (jFxPanel == null) {
			jFxPanel = new JFXPanel(); // initialize JavaFX toolkit
			Platform.setImplicitExit(false);
		}
	}

	public static void exitJavaFx() {
		Platform.runLater(() -> Platform.exit());
	}

	public static void runAndWait(Runnable runnable) throws InterruptedException, ExecutionException {
		if (Platform.isFxApplicationThread()) {
			try {
				runnable.run();
			} catch (Exception e) {
				throw new ExecutionException(e);
			}
		} else {
			FutureTask<Object> futureTask = new FutureTask<>(runnable, null);
			Platform.runLater(futureTask);
			futureTask.get();
		}
	}

	public static void runLater(Runnable runnable) {
		Platform.runLater(runnable);
	}
}
