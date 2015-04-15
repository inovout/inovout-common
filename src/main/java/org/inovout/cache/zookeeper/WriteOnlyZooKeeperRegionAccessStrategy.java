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
	public void remove(Object key) {
		region.remove((String) key);
	}

	@Override
	public void removeAll() {
		region.removeAll();
	}

}
