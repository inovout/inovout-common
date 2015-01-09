package org.inovout.cache.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.inovout.cache.PathRegion;
import org.inovout.cache.RegionFactory;
import org.inovout.zookeeper.ZooKeeperClientFactory;

public class ZooKeeperRegionFactory implements RegionFactory {
	private CuratorFramework zookeeperClient;

	@Override
	public void start() {
		zookeeperClient = ZooKeeperClientFactory
				.getClient(ZooKeeperRegionFactory.class);

		synchronized (zookeeperClient) {
			if (zookeeperClient.getState() != CuratorFrameworkState.STARTED) {
				zookeeperClient.start();
			}
		}
	}

	@Override
	public void stop() {
	}

	@Override
	public PathRegion buildPathRegion(String regionName, String rootPath) {
		return new ZooKeeperPathRegion(zookeeperClient, regionName, rootPath);
	}

}
