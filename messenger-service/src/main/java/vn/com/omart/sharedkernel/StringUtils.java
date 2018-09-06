package vn.com.omart.sharedkernel;

import java.text.Normalizer;
import java.util.regex.Pattern;

import vn.com.omart.messenger.common.constant.Messenger;

public class StringUtils {
	public static String removeAccent(String s) {
		if (!org.apache.commons.lang3.StringUtils.isBlank(s)) {
			String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
			Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
			return pattern.matcher(temp).replaceAll("");
		}
		return s;
	}

	private static boolean isIgnoreEmoij(String content) {
		String[] emoijs = Messenger.EMOIJ_NO_SUPPORTS.split(",");
		for (String str : emoijs) {
			if (content.contains(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Some emoij no support in mysql so we need normalize before store.
	 * 
	 * @param content
	 * @return String
	 */
	public static String emoijNormalize(String content) {
		if (org.apache.commons.lang3.StringUtils.isNotBlank(content)) {
			if (isIgnoreEmoij(content)) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < content.length(); i++) {
					char ch = content.charAt(i);
					if (!Character.isHighSurrogate(ch) && !Character.isLowSurrogate(ch)) {
						sb.append(ch);
					}
				}
				return sb.toString();
			}
		}
		return content;
	}
}
