package org.helper.service;

import java.util.HashSet;
import java.util.Set;

public abstract class ServiceFactory {

	private static Set<Object> servicePool = new HashSet<>();

	@SuppressWarnings("unchecked")
	public static <S> S getService(Class<S> c) {
		for (Object service : servicePool) {
			if (service.getClass().equals(c)) {
				return (S) service;
			}
		}
		S service = null;
		try {
			service = (S) Class.forName(c.getName()).newInstance();
			servicePool.add(service);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			e.printStackTrace();
		}
		return service;
	}
}
