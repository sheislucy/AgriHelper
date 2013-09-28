package org.helper.service;

import java.util.HashMap;
import java.util.Map;

public abstract class ServiceFactory {

	private static Map<Class<?>, Object> servicePool = new HashMap<>();

	@SuppressWarnings("unchecked")
	public static <S> S getService(Class<S> c) {
		S service = (S) servicePool.get(c);
		if (null != service) {
			return service;
		}
		try {
			service = (S) c.newInstance();
			servicePool.put(c, service);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return service;
	}
}
