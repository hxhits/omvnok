package vn.com.omart.backend.application.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class DateUtils {

	public static Date getCurrentDate() {
		// TODO: we may need to convert datetime to UTC time zone
		return new Date();
	}

	public static int daysBetweenCurrent(Date fromDate) {
		return daysBetween(fromDate, new Date());
	}

	public static int daysBetween(Date fromDate, Date toDate) {

		DateTime fromDateTime = toJodaTime(fromDate);
		DateTime toDateTime = toJodaTime(toDate);

		Days diffInDays = Days.daysBetween(fromDateTime, toDateTime);

		return diffInDays.getDays();
	}

	public static DateTime toJodaTime(Date date) {
		DateTime dateTime = date == null ? null : new DateTime(date);
		return dateTime;
	}

	public static String getDateByFormat(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateFormat = formatter.format(date);
		return dateFormat;
	}

	public static String getCurrentDateByFormat(String format) {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateFormat = formatter.format(date);
		return dateFormat;
	}

	/**
	 * First day of quarter.
	 * 
	 * @param date
	 * @return timestamp
	 */
	public static Long getFirstDayOfQuarter(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) / 3 * 3);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTimeInMillis();
	}

	/**
	 * Last day of quarter.
	 * 
	 * @param date
	 * @return timestamp
	 */
	public static Long getLastDayOfQuarter(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) / 3 * 3 + 2);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return cal.getTimeInMillis();
	}

	/**
	 * First Day Of Week
	 * 
	 * @return timestamp
	 */
	public static Long getFirstDayOfWeek() {
		// get today and clear time of day
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		// get start of this week in milliseconds
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		return cal.getTimeInMillis();
	}

	/**
	 * First Day Of Month
	 * 
	 * @return timestamp
	 */
	public static Long getFirstDayOfMonth() {
		// get today and clear time of day
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		// get start of the month
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTimeInMillis();
	}
}