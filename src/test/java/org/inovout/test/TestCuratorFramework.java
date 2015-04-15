package org.inovout.test;

import static org.junit.Assert.assertEquals;


import org.apache.curator.framework.CuratorFramework;

import org.apache.curator.framework.recipes.cache.PathChildrenCache;

import org.inovout.InovoutException;
import org.inovout.zookeeper.ZooKeeperClientFactory;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;



@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCuratorFramework {

	private static CuratorFramework curatorFramework = null;
	private String path = "/3cloud/cache/topics";

	static {
		curatorFramework = ZooKeeperClientFactory.getClient("zk01111");
		curatorFramework.start();

	}

	@Test
	public void testCreatePath() throws Exception {

		curatorFramework.inTransaction().create().forPath(this.path).and()
				.setData().forPath(this.path, "d,e,f".getBytes()).and()
				.commit();
		System.out.println("Executing first test");
	}

	@Test
	public void testReadPath() throws Exception {

		byte[] byBuffer = curatorFramework.getData().forPath(this.path);
		String strRead = new String(byBuffer);
		strRead = String.copyValueOf(strRead.toCharArray(), 0, byBuffer.length);
		assertEquals("bb", strRead);
		System.out.println("Executing two test");

	}

	@Test
	public void testWritePath() throws Exception {
		curatorFramework.setData().forPath(this.path, "mm".getBytes());
		System.out.println("Executing three test");
	}

	@Test
	public void testDeletePath() throws Exception {
		curatorFramework.delete().forPath(this.path);
		System.out.println("Executing four test");
	}

	@Test
	public void testPathChildrenCache() throws Exception {
		PathChildrenCache zookeeperCache = new PathChildrenCache(
				curatorFramework, "/test/pathdata/topics/topic", true);
		try {
			zookeeperCache.start();
		} catch (Exception e) {
			zookeeperCache.close();
			throw new InovoutException(e);
		}
		//addListener(zookeeperCache);
		//curatorFramework.create().forPath("/test/pathdata/topics/topic");

		curatorFramework.setData().forPath("/test/pathdata/topics/topic", "22aaaa".getBytes());
		/*curatorFramework.delete().forPath("/test/pathdata/topics/topic");
		
		Thread.sleep(5000);*/
		
		Thread.sleep(10000);
		zookeeperCache.close();
	}

	/*private void addListener(PathChildrenCache zookeeperCache) {
		PathChildrenCacheListener listener = new PathChildrenCacheListener() {		
			@Override
			public void childEvent(CuratorFramework client,
					PathChildrenCacheEvent event) throws Exception {
				System.out.println(event.getType());
				switch (event.getType()) {
				case CHILD_ADDED: {
					System.out.println("Node added: ");
					break;
				}

				case CHILD_UPDATED: {
					System.out.println("Node changed: ");
					break;
				}

				case CHILD_REMOVED: {
					System.out.println("Node removed: ");
					break;
				}
				}
			}
		};
		zookeeperCache.getListenable().addListener(listener);
	}*/
}