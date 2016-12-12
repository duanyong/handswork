package me.duanyong.handswork.util;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import me.duanyong.handswork.context.FrameworkContext;
import me.duanyong.handswork.support.Interceptor;
import me.duanyong.handswork.url.UrlMapping;
import me.duanyong.handswork.support.Task;

public final class ActionLoaderUtil {

	private static String toFullClassName(String actionName) {
		return FrameworkContext.BASE_PACKAGE + "." + actionName.toUpperCase().charAt(0) + actionName.substring(1);
	}

	public static Object newAction(UrlMapping mapping) {
		return ClassLoaderUtil.newClassInstance(toFullClassName(mapping.getAction()));
	}

	public static Class loadAction(String actionName) {
		return ClassLoaderUtil.loadClass(toFullClassName(actionName));
	}

	public static Method loadMethod(UrlMapping mapping) {
		return loadMethod(mapping.getAction(), mapping.getMethod());
	}

	public static Method loadMethod(Class actionClass, String methodName) {
		if (actionClass == null || methodName == null || methodName.length() < 1) {
			return null;
		}

		return ClassLoaderUtil.loadMethod(actionClass, methodName);
	}

	public static Method loadMethod(String actionName, String methodName) {
		if (actionName == null || actionName.length() < 1 || methodName == null || methodName.length() < 1) {
			return null;
		}

		return loadMethod(loadAction(actionName), methodName);
	}

	public static List<Task> loadAllTasks() {
		List<Task> tasks = new ArrayList<Task>();

		// task目录下找所有的Java类，并试图实例化。
		// 当它是继承framework.support.Task的类时，加入到tasks列表中
		for (File f : DirectoryUtil.findClassesFilesInDirectory(DirectoryUtil.findDirectory("task"))) {
			Object task = ClassLoaderUtil.newClassInstance(DirectoryUtil.toClassName(f));
			if (task instanceof Task) {
				tasks.add((Task) task);
			}
		}

		return tasks;
	}

	public static List<Interceptor> loadAllInterceptors() {
		return loadAllInterceptables("interceptor");
	}

	public static List<Interceptor> loadAllPlugins() {
		return loadAllInterceptables("plugin");
	}

	private static List<Interceptor> loadAllInterceptables(String dirName) {
		List<Interceptor> interceptors = new ArrayList<Interceptor>();

		// 在interceptor目录下找所有的Java类，并试图实例化。
		// 当它是实现Interceptor的接口时，加入到Interceptor列表中
		for (File f : DirectoryUtil.findClassesFilesInDirectory(DirectoryUtil.findDirectory(dirName))) {
			Object interceptor = ClassLoaderUtil.newClassInstance(DirectoryUtil.toClassName(f));
			if (interceptor instanceof Interceptor) {
				interceptors.add((Interceptor) interceptor);
			}
		}

		return interceptors;
	}

	public static boolean exists(Class actionClass, String method) {
		return loadMethod(actionClass, method) != null;
	}

	public static boolean exists(String action, String method) {
		return loadMethod(action, method) != null;
	}

	private ActionLoaderUtil() {
	}

}
