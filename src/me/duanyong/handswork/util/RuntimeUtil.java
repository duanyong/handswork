package me.duanyong.handswork.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;




public final class RuntimeUtil {
	private static final Logger log = LogManager.getLogger();

	public static final String runCommandOnSystem(String... commands) {
		if (commands == null || commands.length < 1) {
			return null;
		}

		StringBuilder sb = new StringBuilder();

		log.debug("run command '" + buildCommandString(commands) + "'");

		try {
			Process process = commands.length == 1 ? Runtime.getRuntime().exec(commands[0]) : Runtime.getRuntime()
					.exec(commands);
			BufferedReader br = null;

			if (0 == process.waitFor()) {
				br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			} else {
				br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			}

			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append('\n');
			}
		} catch (Exception e) {
			log.info("error run command '" + buildCommandString(commands) + "'", e);
		}

		return sb.toString();
	}

	private static final String buildCommandString(String... commands) {
		if (commands == null || commands.length < 1) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		for (String c : commands) {
			sb.append(c);
			sb.append(' ');
		}

		return sb.toString();
	}

	private RuntimeUtil() {
	}

}
