package vn.com.omart.driver.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import vn.com.omart.driver.common.constant.CommonConstant;

public class CommonUtils {

	private static final int SUB_CONTRACT_ID = 5;

	public static String getFullname(String firstName, String lastName) {

		String name = "";

		if (StringUtils.isNotEmpty(firstName)) {
			name += firstName;
		}

		if (StringUtils.isNotEmpty(lastName)) {

			if (StringUtils.isNotBlank(name)) {
				name += " ";
			}
			name += lastName;
		}

		return name;
	}

	public static byte[] imgUrlToBytes(String imgUrl) throws IOException {

		InputStream is = null;
		URL imageURL = null;

		try {
			imageURL = new URL(imgUrl);
			is = imageURL.openStream();
			byte[] imageBytes = IOUtils.toByteArray(is);

			return imageBytes;

		} catch (IOException e) {

			throw e;

		} finally {
			closeStream(is);
		}

	}

	public static void closeStream(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {

			}
		}
	}

	/**
	 * Convert meters to miles.
	 * 
	 * @param metersValue
	 * @return miles
	 */
	public static int metersToMiles(double metersValue) {
		int mile = 0;
		mile = (int) Math.round(metersValue * 0.00062137f);
		return mile;
	}

	/**
	 * Convert miles to meters.
	 * 
	 * @param milesValue
	 * @return meters
	 */
	public static int milestoMeters(double milesValue) {
		int meters = 0;
		meters = (int) Math.round(milesValue / 0.00062137f);
		return meters;
	}

	/**
	 * Split string to array.
	 * 
	 * @param stringNum
	 * @return numbers
	 */
	public static Long[] toLongNumber(String stringNum) {
		String[] ids = stringNum.split(",");
		Long[] longNums = new Long[ids.length];
		for (int i = 0; i < ids.length; i++) {
			longNums[i] = Long.parseLong(ids[i]);
		}
		return longNums;
	}

	/**
	 * Get short price.
	 * 
	 * @param price
	 * @return String
	 */
	public static String getShortPrice(long price) {
		String value = "";
		int million = 1000000;
		int billion = 1000000000;
		int thousand = 1000;
		if (price >= billion) {
			value = getShortPrice(price, billion, " Tỷ");
		} else if (price >= million && price < billion) {
			value = getShortPrice(price, million, "Tr");
		} else if (price >= thousand && price < billion) {
			value = getShortPrice(price, thousand, "K");
		} else {
			value = price + "K";
		}
		return value;
	}

	/**
	 * Get short price.
	 * 
	 * @param price
	 * @param mount
	 * @param unit
	 * @return String
	 */
	public static String getShortPrice(float price, int mount, String unit) {
		String value = "";
		float t = (float) (Math.round((price / mount) * 10.0) / 10.0);
		if (String.valueOf(t).contains(".0")) {
			value = Math.round(t) + "" + unit;
		} else {
			value = t + "" + unit;
		}
		return value;
	}

	/**
	 * Calculate star rating.
	 * 
	 * @param overallRating
	 * @param totalRating
	 * @param newRating
	 * @return float
	 */
	public static float calculateStarRating(float overallRating, int totalRating, int newRating) {
		float rate = 0f;
		if (overallRating > 0) {
			rate = (float) ((overallRating * totalRating) + newRating) / (totalRating + 1);
		} else {
			// init the first time rating.
			totalRating = 1;
			rate = (float) ((overallRating * totalRating) + newRating) / (totalRating);
		}
		return rate;
	}

	/**
	 * Make Round 2 Decimal.
	 * 
	 * @param number
	 * @return float
	 */
	public static float makeRound2Decimal(float number) {
		float result = (float) (Math.round(number * 10F) / 10F);
		return result;
	}

	/**
	 * Generate Sub Id.
	 * 
	 * @param lengthMax
	 * @param value
	 * @return String
	 */
	private static String generateSubId(int lengthMax, String value) {
		int length = lengthMax - value.length();
		String id = "";
		for (int i = 0; i < length; i++) {
			id += "0";
		}
		return (id + value).trim();
	}

	/**
	 * Get Sub Id.
	 * 
	 * @param oldId
	 * @return String
	 */
	private static String getSubId(String oldId) {
		String newId = oldId.substring(oldId.length() - SUB_CONTRACT_ID, oldId.length());
		int id = Integer.valueOf(newId) + 1;
		return String.valueOf(id);
	}

	/**
	 * Contract Id Generator
	 * 
	 * @param carTypeId
	 * @param dateOfRegistration
	 * @param subId
	 * @return String
	 */
	public static String contractIdGenerator(Long carTypeId, Date dateOfRegistration, int subId) {
		String contractId = "DR";
		String carTypeIdStr = String.valueOf(carTypeId);
		if (carTypeIdStr.length() == 1) {
			carTypeIdStr = "0" + carTypeIdStr;
		}
		String date = DateUtils.getDateByFormat(dateOfRegistration, CommonConstant.YYMMDD);
		String subIdStr = (subId + 1) + "";
		String id = generateSubId(SUB_CONTRACT_ID, subIdStr);
		contractId += carTypeIdStr + date + id;
		return contractId;
	}

	/**
	 * Contract Id Generator
	 * 
	 * @param carTypeId
	 * @param subId
	 * @return String
	 */
	public static String contractIdGenerator(Long carTypeId, String subId) {
		String contractId = "DR";
		String carTypeIdStr = String.valueOf(carTypeId);
		if (carTypeIdStr.length() == 1) {
			carTypeIdStr = "0" + carTypeIdStr;
		}
		String date = DateUtils.getCurrentDateByFormat(CommonConstant.YYMMDD);
		String id = generateSubId(SUB_CONTRACT_ID, getSubId(subId));
		contractId += carTypeIdStr + date + id;
		return contractId;
	}

	/**
	 * Remove space
	 * 
	 * @param text
	 * @return String
	 */
	public static String removeSpace(String text) {
		String value = text.replaceAll(" ", "");
		return value.trim();
	}

}
