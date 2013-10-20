package org.helper.util;

import org.apache.http.Header;
import org.helper.domain.FarmDomain;
import org.helper.domain.login.UserDomain;
import org.helper.enums.EmCookieKeys;

public class CookieSplitter {

	public static void splitFarm(Header... headers) {
		for (Header header : headers) {
			saveFarmInfo(header.getValue());
		}
	}

	public static void splitLoginForVC(Header... headers) {
		for (Header header : headers) {
			for (String pair : cut(header.getValue())) {
				saveUserInfoForVC(pair);
			}
		}
	}

	public static void splitLoginForZN(Header... headers) {
		for (Header header : headers) {
			for (String pair : cut(header.getValue())) {
				saveUserInfoZN(pair);
			}
		}
	}

	public static void splitLoginForLP(Header... headers) {
		for (Header header : headers) {
			for (String pair : cut(header.getValue())) {
				saveUserInfoLP(pair);
			}
		}
	}

	private static void saveFarmInfo(String pairStr) {
		String[] pair = pairStr.split("=");
		if (pair != null && pair.length > 1) {
			FarmDomain.getInstance().addCookie(pair[0], pair[1]);
		}
	}

	private static void saveUserInfoForVC(String pairStr) {
		String[] pair = pairStr.split("=");
		if (pair != null && pair.length > 1 && EmCookieKeys.contains(pair[0])) {
			UserDomain.getInstance().addCookie(pair[0], pair[1]);
		}
	}

	private static void saveUserInfoZN(String pairStr) {
		String[] pair = pairStr.split("=");
		if (pair != null && pair.length > 1 && EmCookieKeys.contains(pair[0])) {
			if (!pair[1].equalsIgnoreCase("deleted")) {
				UserDomain.getInstance().addCookie(pair[0], pair[1]);
			}
		}
	}
	
	private static void saveUserInfoLP(String pairStr) {
		String[] pair = pairStr.split("=");
		if (pair != null && pair.length > 1 && EmCookieKeys.contains(pair[0])) {
			if (!pair[1].equalsIgnoreCase("deleted")) {
				UserDomain.getInstance().addCookie(pair[0], pair[1]);
			}
		}
	}

	private static String[] cut(String stringPiece) {
		return stringPiece.split(";");
	}
}
