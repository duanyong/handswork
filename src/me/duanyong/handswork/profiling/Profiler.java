package me.duanyong.handswork.profiling;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;




public final class Profiler {

	private static final Logger log = LogManager.getLogger();

	private static final NumberFormat NUMBER_FORMAT;
	static {
		NUMBER_FORMAT = NumberFormat.getInstance();
		NUMBER_FORMAT.setMaximumFractionDigits(1);
	}

	private static final ThreadLocal<P> checkpoints = new ThreadLocal<P>();

	private static boolean on = true;

	public static boolean isOn() {
		return on;
	}

	public static void switchOn() {
		on = true;
	}

	public static void switchOff() {
		on = false;
	}

	public static void clear() {
		if (!on) {
			return;
		}

		checkpoints.set(null);
	}

	public static void start(String message) {
		if (!on) {
			return;
		}

		checkpoints.set(new P(checkpoints.get(), message));
	}

	public static void finish() {
		if (!on) {
			return;
		}

		P p = checkpoints.get();
		if (!p.running) {
			// 客户端代码错误，不该在已经结束的计时结点上再次结束
			return;
		}

		p.duration = (System.nanoTime() - p.startTime) / 1000;
		p.running = false;

		checkpoints.set(p.parent);

		if (p.parent == null) {
			// 全部计时结束，输出log
			log(p);
		}
	}

	private static void log(P root) {
		StringBuilder sb = new StringBuilder();
		logRecursive(root, sb, (byte) 0, 0);

		sb.deleteCharAt(sb.length() - 1);

		log.info(sb);
	}

	private static void logRecursive(P p, StringBuilder sb, byte level,
			int position) {
		// log格式行首缩进
		for (byte i = 0; i < level; ++i) {
			sb.append('\t');
		}
		// 列表计数
		if (level > 0) {
			sb.append(position + 1);
			sb.append(". ");
		}

		// 输出消息和消息耗时
		sb.append(p.message);
		if (level == 0) {
			sb.append(" ----------> ");
		}
		sb.append(" (");
		sb.append(p.duration);
		sb.append("us)");

		// 输出耗时所占比重
		P p2 = p.parent;
		while (p2 != null) {
			sb.append(", ");
			sb.append(NUMBER_FORMAT.format((double) 100 * p.duration
					/ p2.duration));
			sb.append('%');

			p2 = p2.parent;
		}

		sb.append('\n');

		for (int pos = 0; pos < p.children.size(); ++pos) {
			logRecursive(p.children.get(pos), sb, (byte) (level + 1), pos);
		}
	}

	private static class P {
		String message;

		P parent;

		List<P> children = new ArrayList<P>();

		long startTime = System.nanoTime();

		boolean running = true;

		long duration;

		public P(P parent, String message) {
			this.parent = parent;
			this.message = message;

			if (this.parent != null) {
				this.parent.children.add(this);
			}
		}
	}

	private Profiler() {
	}
}
