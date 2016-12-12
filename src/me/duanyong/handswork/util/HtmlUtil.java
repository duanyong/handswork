package me.duanyong.handswork.util;

public final class HtmlUtil {

	public static String escape(String text) {
		if (text == null) {
			return null;
		}

		String t = text;
		t = t.replaceAll("&", "&amp;");
		t = t.replaceAll(" ", "&nbsp;");
		t = t.replaceAll("\"", "&quot;");
		t = t.replaceAll("\\\\", "\\\\");
		t = t.replaceAll("<", "&lt;");
		t = t.replaceAll(">", "&gt;");
		t = t.replaceAll("\n", "<br/>");
		t = t.replaceAll("\r", "");

		return t;
	}

	public static String jsString(String str) {
		if (str == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder(str.length());

		for (int i = 0; i < str.length(); ++i) {
			switch (str.charAt(i)) {
			case '"':
				sb.append("\\\"");
				break;
			case '\'':
				sb.append("\\'");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '>':
				sb.append("\\>");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\t':
				sb.append("\\t");
				break;
			default:
				sb.append(str.charAt(i));
			}
		}

		return sb.toString();
	}

	private HtmlUtil() {
	}

}
