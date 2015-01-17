package org.inovout.test;

import static org.junit.Assert.*;



import org.apache.curator.framework.CuratorFramework;
import org.inovout.cache.AccessType;
import org.inovout.cache.CacheFactory;
import org.inovout.cache.PathCache;
import org.inovout.zookeeper.ZooKeeperClientFactory;
import org.junit.Test;

public class TestCacheFactory {
	private static CuratorFramework curatorFramework = null;
	

	static {
		curatorFramework = ZooKeeperClientFactory
				.getClient(WriteData2Path.class);
		curatorFramework.start();
	}

	@Test
	public void testReadOnly() throws Exception {

		PathCache pathCache = CacheFactory.builderPathCache()
				.setAccessType(AccessType.READ_ONLY)
				.setRegionName("region-one").build();
		String zkPath = pathCache.getRegionPath() + "/keytest";

		WriteData2Path writeData2Path = new WriteData2Path(zkPath,"datatest");
		writeData2Path.start();
		Thread.sleep(10000);
		String result = ByteToString((byte[]) pathCache.get("keytest"));
		assertEquals("datatest",result);
	}

	@Test
	public void testWriteOnly() throws Exception {
		PathCache pathCache = CacheFactory.builderPathCache()
				.setAccessType(AccessType.WRITE_ONLY)
				.setRegionName("region-two").build();
		pathCache.put("keytest", "qian1");
		String zkPath = pathCache.getRegionPath() + "/keytest";
		Thread.sleep(10000);
		String result = ReadDataFromPath(zkPath);
		
		assertEquals("qian1", result);
	}

	private String ReadDataFromPath(String path) throws Exception {

		byte[] byBuffer = curatorFramework.getData().forPath(path);
		String strRead = new String(byBuffer);
		strRead = String.copyValueOf(strRead.toCharArray(), 0, byBuffer.length);
		return strRead;
	}

	private String ByteToString(byte[] result) throws Exception {

		byte[] byBuffer = result;
		String strRead = new String(byBuffer);
		strRead = String.copyValueOf(strRead.toCharArray(), 0, byBuffer.length);
		return strRead;
	}

	public class WriteData2Path extends Thread {

		private String pathName;
		private String pathData;

		public WriteData2Path(String pathName, String pathData) {
			this.pathName = pathName;
			this.pathData = pathData;
		}

		public void run() {

			try {
				if (curatorFramework.checkExists().forPath(pathName) == null) {
					curatorFramework.create().forPath(pathName,
							pathData.getBytes());
				} else {
					curatorFramework.setData().forPath(this.pathName,
							pathData.getBytes());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
