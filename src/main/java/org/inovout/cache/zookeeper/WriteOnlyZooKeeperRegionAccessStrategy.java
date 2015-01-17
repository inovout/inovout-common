package org.inovout.cache.zookeeper;

import org.inovout.InovoutException;
import org.inovout.cache.WriteOnlyRegionAccessStrategy;

public class WriteOnlyZooKeeperRegionAccessStrategy extends
		AbstractZooKeeperAccessStrategy implements
		WriteOnlyRegionAccessStrategy {

	public WriteOnlyZooKeeperRegionAccessStrategy(ZooKeeperPathRegion region) {
		super(region);
	}

	@Override
	public void put(Object key, Object value) {
			try {
				region.put((String)key, value);
			} catch (Exception e) {
				throw new InovoutException(e);
			}
		
	}

	@Override
	public void remvoe(Object key) {
		region.remvoe((String) key);
	}

	@Override
	public void removeAll() {
		region.removeAll();
	}

}
