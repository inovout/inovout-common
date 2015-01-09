package org.inovout.zookeeper;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.inovout.BaseFactory;
import org.inovout.config.Configuration;

public class ZooKeeperClientFactory extends BaseFactory<CuratorFramework> {

	private ZooKeeperClientFactory() {
	}

	public static final ZooKeeperClientFactory instance = new ZooKeeperClientFactory();

	public static CuratorFramework getClient(Class<?> clazz) {
		return instance.getInstance(clazz);
	}

	public static CuratorFramework getClient(String name) {
		return instance.getInstance(name);
	}

	private static final String CONNECTION_STRING_KEY = "zk.connection";
	private static final String DEFAULT_CONNECTION_STRING_KEY = getLocalAddress()
			+ ":2181";
	private static final String SESSION_TIMEOUT_MS_KEY = "zk.session.timeout";
	private static final int DEFAULT_SESSION_TIMEOUT_MS = Integer.getInteger(
			"zookeeper-default-session-timeout", 60 * 1000);
	private static final String CONNECTION_TIMEOUT_MS_KEY = "zk.connection.timeout";
	private static final int DEFAULT_CONNECTION_TIMEOUT_MS = Integer
			.getInteger("zookeeper-default-connection-timeout", 15 * 1000);
	private static final String RETRY_SLEEP_TIME_MS_KEY = "zk.retry.sleep.time";
	private static final int DEFAULT_RETRY_SLEEP_TIME_MS = Integer.getInteger(
			"zookeeper-retry-sleep-time", 3 * 1000);

	private static final String MAX_RETRIES_KEY = "zk.max.retries";
	private static final int DEFAULT_MAX_RETRIES = Integer.getInteger(
			"zookeeper-max-retries", 3);
	private static final String MAX_RETRY_SLEEP_TIME_MS_KEY = "zk.max.retry.sleep.time";
	private static final int DEFAULT_MAX_RETRY_SLEEP_TIME_MS = Integer
			.getInteger("zookeeper-retry-sleep-time", 5 * 60 * 1000);

	private static final Configuration configuration;
	static {
		configuration = new Configuration();
		configuration.addResource("zookeeper.xml");
	}

	private static String getLocalAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "127.0.0.1";
	}

	@Override
	public CuratorFramework newInstance(String name) {
		String connectString = configuration.get(CONNECTION_STRING_KEY,DEFAULT_CONNECTION_STRING_KEY				);
		int sessionTimeoutMs = configuration.getInt(SESSION_TIMEOUT_MS_KEY,
				DEFAULT_SESSION_TIMEOUT_MS);
		int connectionTimeoutMs = configuration.getInt(
				CONNECTION_TIMEOUT_MS_KEY, DEFAULT_CONNECTION_TIMEOUT_MS);

		int retrySleepTimeMs=configuration.getInt(RETRY_SLEEP_TIME_MS_KEY,DEFAULT_RETRY_SLEEP_TIME_MS);
		int maxRetries=configuration.getInt(MAX_RETRIES_KEY,DEFAULT_MAX_RETRIES);
		int maxRetrySleepMs=configuration.getInt(MAX_RETRY_SLEEP_TIME_MS_KEY,DEFAULT_MAX_RETRY_SLEEP_TIME_MS);
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(retrySleepTimeMs, maxRetries,maxRetrySleepMs);
		return CuratorFrameworkFactory.builder().connectString(connectString)
				.sessionTimeoutMs(sessionTimeoutMs)
				.connectionTimeoutMs(connectionTimeoutMs)
				.retryPolicy(retryPolicy).build();

	}
}