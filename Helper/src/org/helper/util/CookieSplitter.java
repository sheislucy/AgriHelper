package org.helper.util;

import org.apache.http.Header;
import org.helper.domain.FarmDomain;
import org.helper.domain.VeryCDUserDomain;

public class CookieSplitter {

	public static void splitFarm(Header... headers) {
		for (Header header : headers) {
			saveFarmInfo(header.getValue());
		}
	}

	public static void splitLogin(Header... headers) {
		for (Header header : headers) {
			for (String pair : cut(header.getValue())) {
				saveUserInfo(pair);
			}
		}
	}

	private static void saveFarmInfo(String pairStr) {
		String[] pair = pairStr.split("=");
		if (pair != null && pair.length > 1 ) {
			FarmDomain.getInstance().addCookie(pair[0], pair[1]);
		}
	}

	private static void saveUserInfo(String pairStr) {
		String[] pair = pairStr.split("=");
		if (pair != null && pair.length > 1 && EmCookieKeys.contains(pair[0])) {
			VeryCDUserDomain.getInstance().addCookie(pair[0], pair[1]);
		}
	}

	private static String[] cut(String stringPiece) {
		return stringPiece.split(";");
	}
}
