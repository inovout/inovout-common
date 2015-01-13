package org.inovout.cache.zookeeper;

import java.util.Hashtable;

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
		for(ZooKeeperPathRegion region : regions.values())
		{
			region.dispose();
		}
	}

	private static final Hashtable<String, ZooKeeperPathRegion> regions = new Hashtable<String, ZooKeeperPathRegion>();

	@Override
	public PathRegion buildPathRegion(String regionName, String rootPath) {

		ZooKeeperPathRegion region = regions.get(regionName);
		if (region == null) {
			region = new ZooKeeperPathRegion(zookeeperClient, regionName,
					rootPath);
			regions.put(regionName, region);
		}
		return region;
	}
}
