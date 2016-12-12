package me.duanyong.handswork.util;

import javax.servlet.http.HttpServletRequest;

public final class RequestUtil {

	public static String getServletPath(HttpServletRequest request) {
		String requestUri = request.getRequestURI();
		int startIndex = request.getContextPath().equals("") ? 0 : request
				.getContextPath().length();
		int endIndex = (request.getPathInfo() == null) ? requestUri.length()
				: requestUri.lastIndexOf(request.getPathInfo());

		if (startIndex > endIndex) {
			endIndex = startIndex;
		}

		return requestUri.substring(startIndex, endIndex).trim();
	}

	public static String getIp(HttpServletRequest request) {
		String remoteAddr = request.getRemoteAddr();
		String x = request.getHeader("X-Forwarded-For");

		if (x == null || x.length() < 1) {
			return remoteAddr;
		} else {
			return remoteAddr + "|" + x;
		}
	}

	private RequestUtil() {
	}

}
