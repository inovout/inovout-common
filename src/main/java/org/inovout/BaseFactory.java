package org.inovout;

import java.util.Hashtable;

public abstract class BaseFactory<T> {
	protected Hashtable<String, T> instances = new Hashtable<String, T>();

	public T getInstance(Class<?> clazz) {
		return (getInstance(clazz.getName()));
	}

	public T getInstance(String name) {
		T instance = (T) instances.get(name);
		if (instance == null) {
			instance = newInstance();
			instances.put(name, instance);
		}
		return (instance);
	}

	public abstract T newInstance();

}
