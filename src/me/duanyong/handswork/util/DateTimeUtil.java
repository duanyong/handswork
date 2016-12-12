package me.duanyong.handswork.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public final class DateTimeUtil {
	private static final Logger log = LogManager.getLogger();

	public static final String DATEFORTMATER 	= "yyyy-MM-dd";
	public static final String TIMEFORTMATER 	= "HH:mm:ss";
	public static final String FULLFORTMATER 	= DATEFORTMATER + " " + TIMEFORTMATER;
	public static final String TIMEZONE			= "Asia/Shanghai";

	public static TimeZone getTimezone() {
		TimeZone.setDefault(TimeZone.getTimeZone(DateTimeUtil.TIMEZONE));

		return TimeZone.getDefault();
	}

	//获取日历对象，最主要是添加了本地时区
	public static Calendar getCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(DateTimeUtil.getTimezone());

		return calendar;
	}


	public static Calendar getCalendar(Date date) {
		Calendar calendar = DateTimeUtil.getCalendar();
		calendar.setTime(date);

		return calendar;
	}

	public static SimpleDateFormat getDateFormat(String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		format.setTimeZone(DateTimeUtil.getTimezone());

		return format;
	}

	public static String getDateTimeString() {
		return getDateTimeString(new Date());
	}

	public static String getDateTimeString(Date date) {
		return new SimpleDateFormat(FULLFORTMATER).format(date);
	}
	
	public static String getTimeString() {
		return getTimeString(new Date());
	}
	
	public static String getTimeString(Date date) {
		return new SimpleDateFormat(TIMEFORTMATER).format(date);
	}

	
	public static String getDateString() {
		return getDateString(new Date(), null);
	}

	
	public static String getDateString(Date date) {
		return getDateString(date, null);
	}

	public static String getDateString(Date date, String minusSign) {
		if (minusSign == null) {
			minusSign = "-";
		}

		StringBuilder sb = new StringBuilder();

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		int month = 1 + cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		sb.append(cal.get(Calendar.YEAR));
		sb.append(minusSign);

		if (month < 10) {
			sb.append('0');
		}
		sb.append(month);

		sb.append(minusSign);

		if (day < 10) {
			sb.append('0');
		}
		sb.append(day);

		return sb.toString();
	}

	public static Date getDate(String datetime) {
		return getDate(datetime, FULLFORTMATER);
	}


	public static Date getDate(String datetime, String formart) {
		SimpleDateFormat format = new SimpleDateFormat(formart);

		try {
			return format.parse(datetime);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date getDate(Date datetime, String formart) {
		String dateString = getDateTimeString(datetime);

		if (dateString == null) {
			return null;
		}

		return getDate(dateString, formart);
	}

	public static Date getDateByString(String date) {
		return getDateByString(date, DateTimeUtil.FULLFORTMATER);
	}


	public static Date getDateByString(String date, String minsSign) {
		SimpleDateFormat format = DateTimeUtil.getDateFormat(minsSign);

		try {
			return format.parse(date);
		} catch (ParseException e) {
			log.warn(e.getMessage());

			return null;
		}
	}

	public static String getStringByDate() {
		return DateTimeUtil.getStringByDate(new Date());
	}

	public static String getStringByDate(Date date) {
		return DateTimeUtil.getStringByDate(date, DateTimeUtil.FULLFORTMATER);
	}

	public static String getStringByDate(Date date, String minsSign) {
		return DateTimeUtil.getDateFormat(minsSign).format(date);
	}


	public static String format(String format) {
		return format(format, new Date());
	}

	public static String format(String format, Date date) {
		return DateTimeUtil.getStringByDate(date, format);
	}


	public static String getFullYear(Date date) {
		Calendar calendar = DateTimeUtil.getCalendar(date);

		return String.valueOf(calendar.get(Calendar.YEAR));
	}


	public static String getFullMonth(Date date) {
		Calendar calendar = DateTimeUtil.getCalendar(date);

		int month = calendar.get(Calendar.MONTH);

		return month < 9 ? "0" + ( month + 1) : String.valueOf(month + 1);
	}

	public static String getFullDay(Date date) {
		Calendar calendar = DateTimeUtil.getCalendar(date);

		return String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
	}


	public static String getFullHour(Date date) {
		Calendar calendar = DateTimeUtil.getCalendar(date);

		return String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
	}


	public static String getFullMinute(Date date) {
		Calendar calendar = DateTimeUtil.getCalendar(date);

		return String.valueOf(calendar.get(Calendar.MINUTE));
	}

	//List<Date firstDay, Date lastDay>
	public static List<Date> getFirstAndLastDay(Date date) {
		Date month = new Date();
		Calendar calendar = DateTimeUtil.getCalendar(month);

		String str1 = new SimpleDateFormat("yyyy-MM-01").format(month);
		String str2 = new SimpleDateFormat(String.format("yyyy-MM-%s", calendar.getActualMaximum(Calendar.DAY_OF_MONTH))).format(month);

		List<Date> list = new ArrayList<>();

		list.add(DateTimeUtil.getDateByString(str1, DateTimeUtil.DATEFORTMATER));
		list.add(DateTimeUtil.getDateByString(str2, DateTimeUtil.DATEFORTMATER));

		return list;
	}


	private DateTimeUtil() {
	}

}
