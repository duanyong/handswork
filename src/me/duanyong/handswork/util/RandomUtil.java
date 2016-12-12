package me.duanyong.handswork.util;

import java.util.Random;

public final class RandomUtil {
	public static final String chars = "abcdefghijklmnopqrstuvwxyz";
	public static final String nums = "0123456789";
	public static final String all = chars + nums + "~!@#$%^&*()_+{}";
	
	public static String getLetter() {
		return getLetter(10);
	}
	
	public static String getLetter(Integer length) {
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		while (sb.length() < length) {
			sb.append(chars.charAt(random.nextInt(chars.length()-1)));
		}
		
		return sb.toString();
	}
	
	public static String getChar() {
		return getChar(10);
	}
	
	public static String getChar(Integer length) {
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		while (sb.length() < length) {
			sb.append(all.charAt(random.nextInt(all.length()-1)));
		}
		
		return sb.toString();
	}



	private RandomUtil() {

	}

}
