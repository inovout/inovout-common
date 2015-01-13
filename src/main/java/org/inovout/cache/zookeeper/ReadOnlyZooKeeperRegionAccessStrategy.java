package org.inovout.cache.zookeeper;

import org.inovout.cache.ReadOnlyRegionAccessStrategy;


public class ReadOnlyZooKeeperRegionAccessStrategy extends
		AbstractZooKeeperAccessStrategy implements ReadOnlyRegionAccessStrategy {

	public ReadOnlyZooKeeperRegionAccessStrategy(ZooKeeperPathRegion region) {
		super(region);
	}

	@Override
	public Object get(Object key) {
		return region.get((String)key);
	}
}
