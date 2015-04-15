package org.inovout.test;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.inovout.lock.InterProcessMutex;
import org.inovout.zookeeper.ZooKeeperClientFactory;
import org.junit.Test;

public class TestInterProcessMutex {

	private String pathData = "testData9";
	private static final String PATH_NAME  = "/path-one";
	private static final String LOCK_NAME="lock-one";

	@Test
	public void testLock() throws Exception {

		//锁定path
		LockPath lockPath = new LockPath(LOCK_NAME);
		lockPath.start();

		//设置path的值
		CuratorFramework curatorFramework = ZooKeeperClientFactory
				.getClient("0113");
		curatorFramework.start();
		InterProcessMutex lock = new InterProcessMutex(LOCK_NAME);
		if (lock.acquire(10000, TimeUnit.MILLISECONDS)) {
			try {
				curatorFramework.setData().forPath(PATH_NAME,
						pathData.getBytes());

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Thread.sleep(20000);
		//读取path的值
		String result = readDataFromPath(curatorFramework,PATH_NAME);
		curatorFramework.close();
		assertEquals(pathData, result);

	}
	
	

	private String readDataFromPath(CuratorFramework client, String path) {
		
		byte[] byBuffer = null;
		try {
			byBuffer = client.getData().forPath(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String result = new String(byBuffer);
		result = String.copyValueOf(result.toCharArray(), 0, byBuffer.length);
		return result;
	}

	public class LockPath extends Thread  {
		
		private String lockName;
		public LockPath(String lockName){
			this.lockName=lockName;
		}
		
		public void run() {
			InterProcessMutex lock = new InterProcessMutex(lockName);
			try {
				lock.acquire();
				Thread.sleep(15000);
				lock.release();
			} catch (Exception e) {
				e.printStackTrace();
				
			}
			
		}
		
	}
}
