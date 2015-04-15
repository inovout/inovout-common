package org.inovout.test;

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
				.getClient(TestCacheFactory.class);
		curatorFramework.start();
	}

	@Test(expected = ClassCastException.class)
	public void testReadOnly() throws Exception {
		PathCache pathCache = CacheFactory.builderPathCache()
				.setAccessType(AccessType.READ_ONLY)
				.setRegionName("test").build();
		String a= pathCache.getString("topics");
		System.out.println(a);
		Thread.sleep(10000);
		a= pathCache.getString("topics");
		System.out.println(a);
	}

	@Test(expected = ClassCastException.class)
	public void testWriteOnly() throws Exception {
		PathCache pathCache = CacheFactory.builderPathCache()
				.setAccessType(AccessType.WRITE_ONLY)
				.setRegionName("test").build();
		pathCache.put("topics", "a,b,c");
		Thread.sleep(1000);
		
		//assertEquals("qian1", result);
	}

}
