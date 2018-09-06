package vn.com.omart.messenger.util;

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
}