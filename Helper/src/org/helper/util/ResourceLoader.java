package org.helper.util;

import java.io.InputStream;

public class ResourceLoader {
	public static InputStream load(String path) {
		InputStream is = ResourceLoader.class.getClassLoader().getResourceAsStream(path);
		return is;
	}
}
