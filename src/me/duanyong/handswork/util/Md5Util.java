package me.duanyong.handswork.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Md5Util {
	public static final String SALT = "jx&*2(257=-\";[2;ad]vx";
	
	private static final MessageDigest MD5 = getMd5Digest();

	private static final MessageDigest SHA512 = getSha512Digest();

	public static String toMD5(String str) {
		if (str == null) {
			return null;
		}

		return byteArrayToHexString(MD5.digest(str.getBytes()));
	}

	public static String toSha512(String str) {
		if (str == null) {
			return null;
		}

		return byteArrayToHexString(SHA512.digest(str.getBytes()));
	}

	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();

		for (int i = 0; i < b.length; i++) {
			resultSb.append(Integer.toHexString(0x0100 + (b[i] & 0x00ff)).substring(1));
		}

		return resultSb.toString();
	}

	private static MessageDigest getSha512Digest() {
		MessageDigest md = null;

		try {
			md = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return md;
	}

	private static MessageDigest getMd5Digest() {
		MessageDigest md = null;

		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return md;
	}

	private Md5Util() {

	}

}
