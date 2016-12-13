package me.duanyong.handswork.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class JsonUtil {

	public static String toJs(Object obj) {
		StringBuilder sb = new StringBuilder();
		jsValue(obj, sb);
		return sb.toString();
	}

	// from freemarker.template.utility.StringUtil.javaScriptStringEnc()
	public static String javaScriptStringEscape(String s) {
		int ln = s.length();
		for (int i = 0; i < ln; i++) {
			char c = s.charAt(i);
			if (c == '"' || c == '\'' || c == '\\' || c == '>' || c < 0x20) {
				StringBuffer b = new StringBuffer(ln + 4);
				b.append(s.substring(0, i));
				while (true) {
					if (c == '"') {
						b.append("\\\"");
					} else if (c == '\'') {
						b.append("\\'");
					} else if (c == '\\') {
						b.append("\\\\");
					} else if (c == '>') {
						b.append("\\>");
					} else if (c < 0x20) {
						if (c == '\n') {
							b.append("\\n");
						} else if (c == '\r') {
							b.append("\\r");
						} else if (c == '\f') {
							b.append("\\f");
						} else if (c == '\b') {
							b.append("\\b");
						} else if (c == '\t') {
							b.append("\\t");
						} else {
							b.append("\\x");
							int x = c / 0x10;
							b.append((char) (x < 0xA ? x + '0' : x - 0xA + 'A'));
							x = c & 0xF;
							b.append((char) (x < 0xA ? x + '0' : x - 0xA + 'A'));
						}
					} else {
						b.append(c);
					}
					i++;
					if (i >= ln) {
						return b.toString();
					}
					c = s.charAt(i);
				}
			} // if has to be escaped
		} // for each characters
		return s;
	}

	private static void jsValue(Object value, StringBuilder sb) {
		if (value == null) {
			sb.append("null");
			return;
		}

		if (value instanceof Iterable) {
			jsValueArray((Iterable) value, sb);
		} else if (value instanceof Map) {
			jsValueHash((Map) value, sb);
		} else if (value instanceof Date) {
			jsValueDate((Date) value, sb);
		} else {
			try {
				if (value.getClass().getPackage().getName().indexOf("java") == 0) {
					jsValueDefault(value, sb);
				} else {
					jsValueObject(value, sb);
				}
			} catch (NullPointerException e) {
				// We can not get class name from an array type which we can't
				// deal with yet, so just ignore it here.
			}
		}
	}

	private static void jsValueArray(Iterable value, StringBuilder sb) {
		sb.append('[');

		boolean b = false;

		for (Object v : value) {
			b = true;
			jsValue(v, sb);
			sb.append(',');
		}

		if (b) {
			sb.deleteCharAt(sb.length() - 1);
		}

		sb.append(']');
	}

	private static void jsValueDate(Date value, StringBuilder sb) {
		sb.append("new Date(");
		sb.append(value.getTime());
		sb.append(")");
	}

	private static void jsValueDefault(Object value, StringBuilder sb) {
		if (value.toString().matches("^-?[\\.\\d]{1,9}$")) {
			// numeric
			sb.append(value.toString());

			return;
		} else if ("true".equalsIgnoreCase(value.toString()) || "false".equalsIgnoreCase(value.toString())) {
			sb.append(value.toString().toLowerCase());

			return;
		}

		sb.append('\'');
		sb.append(javaScriptStringEscape(value.toString()));
		sb.append('\'');
	}

	private static void jsValueHash(Map value, StringBuilder sb) {
		sb.append('{');

		boolean b = false;

		for (Object key : value.keySet()) {
			b = true;
			sb.append(key.toString());
			sb.append(':');

			Object obj = value.get(key);

			if (obj == null) {
				sb.append('"');
				sb.append('"');
			} else {
				jsValue(value.get(key), sb);
			}

			sb.append(',');
		}

		if (b) {
			sb.deleteCharAt(sb.length() - 1);
		}

		sb.append('}');
	}

	private static void jsValueObject(Object obj, StringBuilder sb) {
		Map map = new HashMap();

		try {
			
		} catch (Exception e) {
		}

		map.remove("class");
		jsValueHash(map, sb);
	}

	private JsonUtil() {
	}

}
