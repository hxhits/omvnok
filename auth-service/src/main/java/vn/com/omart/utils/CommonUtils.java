package vn.com.omart.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * Common Utilities
 * 
 * @author Win10
 *
 */
public class CommonUtils {

	/**
	 * Private constructor. All methods of this class are static.
	 */
	public CommonUtils() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Get Http Headers.
	 * 
	 * @return HttpHeaders
	 */
	public static final HttpHeaders geHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add(ConstantUtils.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}

	public static String md5(String str) {
		String result = "";
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			digest.update(str.getBytes());
			BigInteger bigInteger = new BigInteger(1, digest.digest());
			result = bigInteger.toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String generatePassword() {
		Random r = new Random(System.currentTimeMillis());
		Integer activationCode = ((1 + r.nextInt(2)) * 1000 + r.nextInt(1000));
		return String.valueOf(activationCode);
	}
	
	public static boolean isUSPhoneNumber(String phone) {
		String phoneCheck = "";
		if(phone.startsWith("1")) {
			phoneCheck = phone.replaceFirst("1","").trim();
		} else if(phone.startsWith("+1")){
			phoneCheck = phone.substring(2, phone.length());
		}
		
		if (!phoneCheck.isEmpty()) {
			String regex = "^\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(phoneCheck);
			if(matcher.matches()) {
				return true;
			}
		}
		return false;
	}
}
