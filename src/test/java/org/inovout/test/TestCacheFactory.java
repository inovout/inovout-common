package org.inovout.test;


import static org.junit.Assert.assertNotNull;

import org.apache.curator.framework.CuratorFramework;
import org.inovout.cache.AccessType;
import org.inovout.cache.CacheFactory;
import org.inovout.cache.PathCache;
import org.inovout.zookeeper.ZooKeeperClientFactory;
import org.junit.Test;


public class TestCacheFactory {
	CuratorFramework curatorFramework = null;

	public TestCacheFactory() {
		curatorFramework = ZooKeeperClientFactory
				.getClient(WriteData2Path.class);
		curatorFramework.start();
	}

	@Test
	public void testReadOnly() throws Exception {

		PathCache pathCache = CacheFactory.builderPathCache()
				.setAccessType(AccessType.READ_ONLY)
				.setRegionName("regino-one").build();
		//pathCache.get("")
		String zkPath=pathCache.getRegionPath();
		
		
		if(curatorFramework.checkExists().forPath(zkPath)==null){
			System.out.println("路径不存在");
		}else{
			System.out.println(pathCache.get("regino-one"));
		}
		
		
		WriteData2Path writeData2Path = new WriteData2Path(
				pathCache.getRegionPath());
		writeData2Path.start();
		Thread.sleep(10000);
		
		System.out.println(pathCache.get("regino-one"));
		/*if (curatorFramework.getState() != CuratorFrameworkState.STARTED) {
			curatorFramework.start();
		}*/
		System.out.println(ReadDataFromPath(zkPath));
		
		assertNotNull("通过字符串获取的client 不为空", pathCache);
	}
	
	
	private String ReadDataFromPath(String path) throws Exception{
		
		byte[] byBuffer = curatorFramework.getData().forPath(path);
		String strRead = new String(byBuffer);
		strRead = String.copyValueOf(strRead.toCharArray(), 0, byBuffer.length);
		return strRead;
	}

	public class WriteData2Path extends Thread {

		private String pathName;

		public WriteData2Path(String pathName) {
			this.pathName = pathName;
		}

		public void run() {

			try {
				curatorFramework.setData().forPath(this.pathName,
						"mm".getBytes());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	

}
