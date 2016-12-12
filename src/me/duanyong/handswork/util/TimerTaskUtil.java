package me.duanyong.handswork.util;

import java.util.List;
import java.util.Timer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import me.duanyong.handswork.support.Task;



public final class TimerTaskUtil {

	private static final Logger log = LogManager.getLogger();

	private static final Timer timer = new Timer();

	private static final boolean on = timerOn();

	public static void go() {
		List<Task> tasks = ActionLoaderUtil.loadAllTasks();
		for (Task task : tasks) {
			if (task.alwaysOn() || on) {
				timer.schedule(task, task.period(), task.period());
			}
		}
	}

	public static void stop() {
		timer.cancel();
	}

	private static boolean timerOn() {
		if ("on".equalsIgnoreCase(System.getProperty(VmArguments.FRAMEWORK_TASK))) {
			log.info("TimerTask on");
			return true;
		}

		log.info("TimerTask off");
		return false;
	}

	private TimerTaskUtil() {
	}

}
