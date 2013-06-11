package org.helper.service;

public abstract class ServiceFactory {
	@SuppressWarnings("unchecked")
	public static <S> S getService(Class<S> c) {
		S service = null;
		try {
			service = (S) Class.forName(c.getName()).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return service;
	}
}
