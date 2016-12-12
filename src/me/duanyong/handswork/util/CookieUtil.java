package me.duanyong.handswork.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;




public final class CookieUtil {
	private static final Logger log = LogManager.getLogger();

	public static Cookie addCookie(HttpServletResponse response, String name, String value) {
		return addCookie(response, name, value, 1800);
	}

	public static Cookie addPermanentCookie(HttpServletResponse response, String name, String value) {
		return addCookie(response, name, value, 3600 * 24 * 365 * 5);
	}

	public static Cookie addCookie(HttpServletResponse response, String name, String value, int maxAge) {
		try {
			value = URLEncoder.encode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			log.warn("Encoding not supported(utf-8).", e);
		}

		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAge);
		cookie.setPath("/");
		cookie.setDomain(DirectoryUtil.DOMAIN);

		response.addCookie(cookie);

		return cookie;
	}

	public static void deleteCookie(HttpServletResponse response, String name) {
		Cookie cookie = new Cookie(name, "");
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	public static Cookie getCookie(Cookie[] cookies, String key) {
		if (cookies == null) {
			return null;
		}

		Cookie cookie = null;

		for (Cookie c : cookies) {
			if (c.getName().equals(key)) {
				cookie = c;

				break;
			}
		}

		return cookie;
	}

	public static String getCookieValue(Cookie[] cookies, String key) {
		Cookie cookie = getCookie(cookies, key);

		if (cookie == null) {
			return null;
		}

		try {
			return URLDecoder.decode(cookie.getValue(), "utf-8");
		} catch (Exception e) {
			log.warn("Encoding not supported(utf-8).", e);
			return null;
		}
	}

	private CookieUtil() {
	}
}
