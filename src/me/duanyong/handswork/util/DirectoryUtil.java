package me.duanyong.handswork.util;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URI;

public final class DirectoryUtil {

	private static final URI classesURI = calcClassesURI();

	public static final String DOMAIN = calcDomain();

	public static final String SERVER_DIR = calcServerDir();

	public static final String CLASSES_DIR = classesURI.getPath();

	public static final String TMP_DIR = calcTmpDir();

	public static String create(String path) {
		File dir = new File(path);
		
		if (!dir.exists()
			|| !dir.isDirectory()
		) {
			if (!dir.mkdirs()) {
				return null;
			}
		}
		
		return dir.getPath();
		
	}
	
	public static File[] findClassesFilesInDirectory(File dir) {
		if (dir == null) {
			return new File[] {};
		}

		return dir.listFiles(new FilenameFilter() {
			public boolean accept(File file, String name) {
				if (name.endsWith(".class")) {
					return true;
				}

				return false;
			}
		});
	}

	public static File findDirectory(String name) {
		return findDirectory(name, new File(CLASSES_DIR).listFiles());
	}

	private static File findDirectory(String name, File[] files) {
		if (files == null) {
			return null;
		}

		for (File file : files) {
			if (file.isDirectory() && file.getName().equals(name)) {
				return file;
			}

			File f = findDirectory(name, file.listFiles());

			if (f != null) {
				return f;
			}
		}

		return null;
	}

	public static String toClassName(File file) {
		if ((file == null) || !file.getPath().startsWith(CLASSES_DIR) || !file.getName().endsWith(".class")) {
			return null;
		}

		return file.getPath().substring(CLASSES_DIR.length(), file.getPath().lastIndexOf(".class")).replace(
				File.separatorChar, '.');
	}

	public static File toDirectoryFile(Package p) {
		File file = new File(CLASSES_DIR + p.getName().replace('.', File.separatorChar));
		if (!file.isDirectory()) {
			return null;
		}

		return file;
	}

	public static String toPackageName(File dir) {
		String classes_path = new File(CLASSES_DIR).getPath();
		if (dir == null || !dir.getPath().startsWith(classes_path)) {
			return null;
		}

		return dir.getPath().substring(classes_path.length() + 1).replace(File.separatorChar, '.');
	}

	private static final URI calcClassesURI() {
		try {
			return DirectoryUtil.class.getResource(DirectoryUtil.class.getSimpleName() + ".class").toURI().resolve(
					(DirectoryUtil.class.getPackage().getName() + ".").replaceAll("\\w+\\.", "../"));
		} catch (Exception e) {
			throw new Error(e);
		}
	}

	private static final String calcDomain() {
		String[] d = DirectoryUtil.class.getPackage().getName().split("\\.");
		StringBuffer sb = new StringBuffer();
		for (int i=d.length-1; i>=0; --i) {
			sb.append('.');
			sb.append(d[i]);
		}
		
		return sb.substring(1, sb.length());
	}

	private static final String calcServerDir() {
		return classesURI.resolve("../../").getPath();
	}

	private static final String calcTmpDir() {
		return System.getProperty("java.io.tmpdir", "/tmp") + File.separator;
	}

	private DirectoryUtil() {
	}
}
