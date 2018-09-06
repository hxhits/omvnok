package vn.com.omart.sharedkernel;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtils {
	public static String removeAccent(String s) {
		if (!org.apache.commons.lang3.StringUtils.isBlank(s)) {
			String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
			Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
			return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replace("đ", "d");
			// return pattern.matcher(temp).replaceAll("");
		}
		return s;
	}
}
