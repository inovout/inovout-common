package org.inovout.cache.zookeeper;

import org.inovout.InovoutException;
import org.inovout.cache.WriteOnlyRegionAccessStrategy;
import org.inovout.util.JsonUtils;

public class WriteOnlyZooKeeperRegionAccessStrategy extends
		AbstractZooKeeperAccessStrategy implements
		WriteOnlyRegionAccessStrategy {

	public WriteOnlyZooKeeperRegionAccessStrategy(ZooKeeperPathRegion region) {
		super(region);
	}

	@Override
	public void put(Object key, Object value) {
		put(key, value);
	}

	public void put(String key, Object value) throws Exception {
		getZooKeeperClient().setData().forPath(getPath(key),
				JsonUtils.getBytes(value));
	}

	@Override
	public void remvoe(Object key) {
		try {
			getZooKeeperClient().delete().forPath(getPath((String) key));
		} catch (Exception e) {
			throw new InovoutException(e);
		}
	}

	@Override
	public void removeAll()  {
		try {
			getZooKeeperClient().delete().forPath(getRegionPath());
		} catch (Exception e) {
			throw new InovoutException(e);

		}

	}

}
