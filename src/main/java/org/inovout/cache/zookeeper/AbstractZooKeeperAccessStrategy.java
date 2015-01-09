package org.inovout.cache.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.inovout.cache.RegionAccessStrategy;

public class AbstractZooKeeperAccessStrategy implements RegionAccessStrategy {

	protected ZooKeeperPathRegion region;

	public AbstractZooKeeperAccessStrategy(ZooKeeperPathRegion region) {
		this.region = region;
	}

	protected CuratorFramework getZooKeeperClient() {
		return region.getZooKeeperClient();
	}

	protected String getRegionPath() {
		return region.getRegionPath();
	}

	protected String getPath(String key) {
		return region.getPath(key);
	}

	@Override
	public boolean contains(Object key) throws Exception {
	return contains((String)key);
	}
	public boolean contains(String key) throws Exception {
		return getZooKeeperClient().checkExists().forPath(getPath(key)) !=null;

	}
}
