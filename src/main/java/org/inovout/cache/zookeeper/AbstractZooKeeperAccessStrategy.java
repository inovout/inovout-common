package org.inovout.cache.zookeeper;

import org.inovout.cache.Region;
import org.inovout.cache.RegionAccessStrategy;

public class AbstractZooKeeperAccessStrategy implements RegionAccessStrategy {

	protected ZooKeeperPathRegion region;

	public AbstractZooKeeperAccessStrategy(ZooKeeperPathRegion region) {
		this.region = region;
	}


	@Override
	public boolean contains(Object key) throws Exception {
	return this.region.contains((String)key);
	}


	@Override
	public Region getRegion() {
		return this.region;
	}

}
