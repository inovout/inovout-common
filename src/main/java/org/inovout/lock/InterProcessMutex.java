package org.inovout.lock;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.inovout.config.Configuration;
import org.inovout.zookeeper.ZooKeeperClientFactory;

public class InterProcessMutex {

	private String lockName;
	private InterProcessLock mutex;
	private static final String LOCK_ROOT_PATH_KEY = "lock.root.path";
	private static final String DEFAULT_LOCK_ROOT_PATH = "/3cloud/lock/";
	private static final String LOCK_ROOT_PATH;
	static {
		Configuration configuration = new Configuration();
		configuration.addResource("lock.xml");
		LOCK_ROOT_PATH = configuration.get(LOCK_ROOT_PATH_KEY,
				DEFAULT_LOCK_ROOT_PATH);
	}

	public InterProcessMutex(Class<?> clazz) {
		this(clazz.getName());
	}

	public InterProcessMutex(String lockName) {
		this.lockName = lockName;

		mutex = new InterProcessSemaphoreMutex(getZooKeeperClient(),
				(LOCK_ROOT_PATH + lockName));

	}

	private CuratorFramework getZooKeeperClient() {
		CuratorFramework zookeeperClient = ZooKeeperClientFactory
				.getClient(InterProcessMutex.class);
		synchronized (zookeeperClient) {
			if (zookeeperClient.getState() != CuratorFrameworkState.STARTED) {
				zookeeperClient.start();
			}
		}
		return zookeeperClient;
	}

	public void acquire() throws Exception {
		mutex.acquire();
	}

	public boolean acquire(long time, TimeUnit unit) throws Exception {
		return mutex.acquire(time, unit);
	}

	public void release() throws Exception {
		mutex.release();
	}

	public String getLockName() {
		return lockName;
	}
}
