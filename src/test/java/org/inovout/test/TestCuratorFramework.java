package org.inovout.test;

import static org.junit.Assert.assertEquals;

import org.apache.curator.framework.CuratorFramework;
import org.inovout.zookeeper.ZooKeeperClientFactory;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)  
public class TestCuratorFramework {
	private static CuratorFramework curatorFramework = null;
	private String path="/path-ten";

	static {
		curatorFramework = ZooKeeperClientFactory.getClient("zk0111");
		curatorFramework.start();

	}


	@Test
	public void test001CreatePath() throws Exception {

		curatorFramework.inTransaction().create().forPath(this.path).and()
				.setData().forPath(this.path, "bb".getBytes()).and()
				.commit();
		 System.out.println("Executing first test");  
	}

	@Test
	public void test002ReadPath() throws Exception {

		byte[] byBuffer = curatorFramework.getData().forPath(this.path);
		String strRead = new String(byBuffer);
		strRead = String.copyValueOf(strRead.toCharArray(), 0, byBuffer.length);
		assertEquals("bb", strRead);
		System.out.println("Executing two test");  

	}

	@Test
	public void test003WritePath() throws Exception {
		curatorFramework.setData().forPath(this.path, "mm".getBytes());
		 System.out.println("Executing three test");  
	}
	
	
	@Test
	public void test004DeletePath() throws Exception {
		curatorFramework.delete().forPath(this.path);
		 System.out.println("Executing four test");  
	}
}
