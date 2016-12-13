package me.duanyong.handswork.util;

import java.lang.reflect.Method;

@SuppressWarnings("unchecked")
public final class ClassLoaderUtil {

	public static Class loadClass(String className) {
		return loadClass(className, null);
	}

	public static Class loadClass(String className, Class callingClass) {
		Class c = null;

		if (className == null) {
			return c;
		}

		try {
			c = Thread.currentThread().getContextClassLoader().loadClass(className);
		} catch (Exception e) {
			try {
				c = Class.forName(className);
			} catch (Exception e1) {
				try {
					c = ClassLoaderUtil.class.getClassLoader().loadClass(className);
				} catch (Exception e2) {
					if (callingClass != null) {
						try {
							c = callingClass.getClassLoader().loadClass(className);
						} catch (Exception e3) {
							c = null;
						}
					}
				}
			}
		}

		return c;
	}

	public static Method loadMethod(Class c, String methodName) {
		return loadMethod(c, methodName, new Class[] {});
	}

	public static Method loadMethod(Class c, String methodName, Class[] types) {
		if (c == null || methodName == null || methodName.isEmpty()) {
			return null;
		}

		try {
			return c.getMethod(methodName, types);
		} catch (Exception e) {
			return null;
		}
	}

	public static <T> T newClassInstance(Class c) {
		try {
			return (T) c.newInstance();
		} catch (Exception e) {
			return null;
		}
	}

	public static <T> T newClassInstance(String className) {
		return (T) newClassInstance(className, null);
	}

	public static <T> T newClassInstance(String className, Class callingClass) {
		try {
			return (T) newClassInstance(loadClass(className, callingClass));
		} catch (Exception e) {
			return null;
		}
	}

	private ClassLoaderUtil() {
	}
}
