package org.inovout.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.inovout.config.Configuration;

/**
 * General reflection utils
 */
public class ReflectionUtils {

	private static final Class<?>[] EMPTY_ARRAY = new Class[] {};

	/**
	 * Cache of constructors for each class. Pins the classes so they can't be
	 * garbage collected until ReflectionUtils can be collected.
	 */
	private static final Map<Class<?>, Constructor<?>> CONSTRUCTOR_CACHE = new ConcurrentHashMap<Class<?>, Constructor<?>>();

	/**
	 * Create an object for the given class and initialize it from conf
	 * 
	 * @param theClass
	 *            class of which an object is created
	 * @param conf
	 *            Configuration
	 * @return a new object
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<T> theClass, Configuration conf) {
		T result;
		try {
			Constructor<T> meth = (Constructor<T>) CONSTRUCTOR_CACHE
					.get(theClass);
			if (meth == null) {
				meth = theClass.getDeclaredConstructor(EMPTY_ARRAY);
				meth.setAccessible(true);
				CONSTRUCTOR_CACHE.put(theClass, meth);
			}
			result = meth.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// setConf(result, conf);
		return result;
	}

	static private ThreadMXBean threadBean = ManagementFactory
			.getThreadMXBean();

	public static void setContentionTracing(boolean val) {
		threadBean.setThreadContentionMonitoringEnabled(val);
	}

	private static String getTaskName(long id, String name) {
		if (name == null) {
			return Long.toString(id);
		}
		return id + " (" + name + ")";
	}

	/**
	 * Print all of the thread's information and stack traces.
	 * 
	 * @param stream
	 *            the stream to
	 * @param title
	 *            a string title for the stack trace
	 */
	public synchronized static void printThreadInfo(PrintWriter stream,
			String title) {
		final int STACK_DEPTH = 20;
		boolean contention = threadBean.isThreadContentionMonitoringEnabled();
		long[] threadIds = threadBean.getAllThreadIds();
		stream.println("Process Thread Dump: " + title);
		stream.println(threadIds.length + " active threads");
		for (long tid : threadIds) {
			ThreadInfo info = threadBean.getThreadInfo(tid, STACK_DEPTH);
			if (info == null) {
				stream.println("  Inactive");
				continue;
			}
			stream.println("Thread "
					+ getTaskName(info.getThreadId(), info.getThreadName())
					+ ":");
			Thread.State state = info.getThreadState();
			stream.println("  State: " + state);
			stream.println("  Blocked count: " + info.getBlockedCount());
			stream.println("  Waited count: " + info.getWaitedCount());
			if (contention) {
				stream.println("  Blocked time: " + info.getBlockedTime());
				stream.println("  Waited time: " + info.getWaitedTime());
			}
			if (state == Thread.State.WAITING) {
				stream.println("  Waiting on " + info.getLockName());
			} else if (state == Thread.State.BLOCKED) {
				stream.println("  Blocked on " + info.getLockName());
				stream.println("  Blocked by "
						+ getTaskName(info.getLockOwnerId(),
								info.getLockOwnerName()));
			}
			stream.println("  Stack:");
			for (StackTraceElement frame : info.getStackTrace()) {
				stream.println("    " + frame.toString());
			}
		}
		stream.flush();
	}

	private static long previousLogTime = 0;

	/**
	 * Log the current thread stacks at INFO level.
	 * 
	 * @param log
	 *            the logger that logs the stack trace
	 * @param title
	 *            a descriptive title for the call stacks
	 * @param minInterval
	 *            the minimum time from the last
	 */
	public static void logThreadInfo(Log log, String title, long minInterval) {
		boolean dumpStack = false;
		if (log.isInfoEnabled()) {
			synchronized (ReflectionUtils.class) {
				long now = Time.now();
				if (now - previousLogTime >= minInterval * 1000) {
					previousLogTime = now;
					dumpStack = true;
				}
			}
			if (dumpStack) {
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				printThreadInfo(new PrintWriter(buffer), title);
				log.info(buffer.toString());
			}
		}
	}

	/**
	 * Return the correctly-typed {@link Class} of the given object.
	 * 
	 * @param o
	 *            object whose correctly-typed <code>Class</code> is to be
	 *            obtained
	 * @return the correctly typed <code>Class</code> of the given object.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClass(T o) {
		return (Class<T>) o.getClass();
	}

	// methods to support testing
	static void clearCache() {
		CONSTRUCTOR_CACHE.clear();
	}

	static int getCacheSize() {
		return CONSTRUCTOR_CACHE.size();
	}

	/**
	 * Gets all the declared fields of a class including fields declared in
	 * superclasses.
	 */
	public static List<Field> getDeclaredFieldsIncludingInherited(Class<?> clazz) {
		List<Field> fields = new ArrayList<Field>();
		while (clazz != null) {
			for (Field field : clazz.getDeclaredFields()) {
				fields.add(field);
			}
			clazz = clazz.getSuperclass();
		}

		return fields;
	}

	/**
	 * Gets all the declared methods of a class including methods declared in
	 * superclasses.
	 */
	public static List<Method> getDeclaredMethodsIncludingInherited(
			Class<?> clazz) {
		List<Method> methods = new ArrayList<Method>();
		while (clazz != null) {
			for (Method method : clazz.getDeclaredMethods()) {
				methods.add(method);
			}
			clazz = clazz.getSuperclass();
		}

		return methods;
	}

	public static Object newInstance(String className) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		return Class.forName(className).getConstructor().newInstance();
	}
}
