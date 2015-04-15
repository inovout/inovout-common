package org.inovout.test;

import static org.junit.Assert.*;

import org.apache.curator.framework.CuratorFramework;
import org.inovout.zookeeper.ZooKeeperClientFactory;
import org.junit.Test;

public class TestZooKeeperClientFactory {

	private CuratorFramework client = null;

	public TestZooKeeperClientFactory() {
		
	}

	@Test
	public void testNewInstance() throws Exception {
	 client = ZooKeeperClientFactory.instance
				.newInstance();
		assertNotNull("CuratorFramework对象不为空", client);
	}
}
