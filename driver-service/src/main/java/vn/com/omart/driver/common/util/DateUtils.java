package vn.com.omart.driver.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import vn.com.omart.driver.common.constant.CommonConstant;

public class DateUtils {

	public static Date getCurrentDate() {
		// TODO: we may need to convert datetime to UTC time zone
		return new Date();
	}

	public static String getCurrentDateWithString() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(CommonConstant.YYYY_MM_DD);
		String format = formatter.format(date);
		return format;
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

}