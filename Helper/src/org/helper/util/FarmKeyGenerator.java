package org.helper.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FarmKeyGenerator {

	private static final String IMAGE_KEY = "15l3h4kh";

	public static String generatorFarmKey(String timeMillions) {
		String input = timeMillions + IMAGE_KEY;
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
		}
		byte tmp[] = md5.digest(input.getBytes());
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		char str[] = new char[16 * 2];
		int k = 0;
		for (int i = 0; i < 16; i++) {
			byte byte0 = tmp[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			str[k++] = hexDigits[byte0 & 0xf];
		}
		return new String(str);
	}
}
