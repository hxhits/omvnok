package vn.com.omart.backend.application.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.google.common.base.Optional;

import lombok.extern.slf4j.Slf4j;
import vn.com.omart.backend.constants.ConstantUtils;

@Slf4j
public class CommonUtils {

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
			log.error("Failed while reading bytes from {}", imageURL.toExternalForm(), e);
			throw e;

		} finally {
			closeStream(is);
		}

	}

	public static String getImageName(String imgUrl) {

		final URI uri = URI.create(imgUrl);
		final String path = Optional.fromNullable(uri.getPath()).or("/");
		String imgName = path.substring(path.lastIndexOf('/') + 1);

		return imgName;
	}

	public static void closeStream(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				log.error("", e);
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
	 * Get Text By Word Limit.
	 *
	 * @param text
	 * @param limit
	 * @return String
	 */
	public static String getTextByWordLimit(String text, int limit) {
		int length = text.length();
		if (length > limit) {
			String str = text.substring(0, text.indexOf(" ", limit));
			return str + "...";
		}
		return text;
	}

	/**
	 * Get salary label.
	 *
	 * @param index
	 * @return String
	 */
	public static String getSalaryLable(int index) {
		if (index < ConstantUtils.SALARIES.length) {
			return ConstantUtils.SALARIES[index];
		}
		return ConstantUtils.SALARIES[0];
	}

	/**
	 * Get salary label V1.
	 * 
	 * @param index
	 * @return String
	 */
	public static String getSalaryLableV1(int index) {
		if (index < ConstantUtils.SALARIES_V1.length) {
			return ConstantUtils.SALARIES_V1[index];
		}
		return ConstantUtils.SALARIES_V1[0];
	}

	/**
	 * Get Recruit Title.
	 *
	 * @param index
	 * @return String
	 */
	public static String getRecruitTitle(int index) {
		if (index < ConstantUtils.RECRUIT_TITLES.length) {
			return ConstantUtils.RECRUIT_TITLES[index];
		}
		return "";
	}

	/**
	 * Checking Recruit Position Id is Existing.
	 * 
	 * @param id
	 * @return boolean
	 */
	public static boolean isRecruitPositionIdExisting(long id) {
		for (long recruitId : ConstantUtils.RECRUIT_POSITION_IDS) {
			if (recruitId == id) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Remove hyphen.
	 *
	 * @param value
	 * @return String
	 */
	public static String removeHyphen(String value) {
		if (value.contains("-")) {
			String[] provinces = value.split("-");
			String province = "";
			for (int i = 0; i < provinces.length; i++) {
				province += provinces[i].trim() + " ";
			}
			return province.trim();
		}
		return value;
	}

	/**
	 * Get google api key with random value.
	 * 
	 * @param googleApiKeys
	 * @return key
	 */
	public static String getGoogleAPIKey(String googleApiKeys) {
		String[] keys = googleApiKeys.split(",");
		Random r = new Random();
		int index = r.nextInt(keys.length);
		return keys[index];
	}

	/**
	 * Purchase Order Generator.
	 * 
	 * @return ID
	 */
	private static String purchaseOrderGenerator() {
		Random r = new Random(System.currentTimeMillis());
		Integer activationCode = ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
		return String.valueOf(activationCode);
	}

	/**
	 * Get order id auto generate
	 * 
	 * @return ID
	 */
	public static String getPurchaseOrderID() {
		String ID = "OD";
		String date = DateUtils.getCurrentDateByFormat(ConstantUtils.YYMMDD);
		return (ID + date + purchaseOrderGenerator());
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
	 * Remove space
	 * 
	 * @param text
	 * @return String
	 */
	public static String removeSpace(String text) {
		String value = text.replaceAll(" ", "");
		return value.trim();
	}

	/**
	 * Some emoij no support in mysql so we need normalize before store.
	 * 
	 * @param content
	 * @return String
	 */
	public static String emoijNormalize(String content) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < content.length(); i++) {
			char ch = content.charAt(i);
			if (!Character.isHighSurrogate(ch) && !Character.isLowSurrogate(ch)) {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isIdNullOrZero(Long number) {
		if (number == null) {
			return true;
		}
		if (number < 1) {
			return true;
		}
		return false;
	}
}
