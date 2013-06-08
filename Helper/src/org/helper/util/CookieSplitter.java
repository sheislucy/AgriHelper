package org.helper.util;

import org.apache.http.Header;
import org.helper.domain.UserDomain;

public class CookieSplitter {

	public static void split(Header... headers) {
		for (Header header : headers) {
			for (String pair : cut(header.getValue())) {
				System.out.println(pair);
				saveUserInfo(pair);
			}
		}
	}

	private static void saveUserInfo(String pairStr) {
		String[] pair = pairStr.split("=");
		if (pair != null && pair.length > 1 && EmCookieKeys.contains(pair[0])) {
			UserDomain.getInstance().addCookie(pair[0].toUpperCase(), pair[1]);
		}
	}

	private static String[] cut(String stringPiece) {
		return stringPiece.split(";");
	}
}
