package org.inovout.cache.zookeeper;

import java.util.Hashtable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.inovout.InovoutException;
import org.inovout.cache.ReadOnlyRegionAccessStrategy;

public class ReadOnlyZooKeeperRegionAccessStrategy extends
		AbstractZooKeeperAccessStrategy implements ReadOnlyRegionAccessStrategy {
	
	private static final Log LOG = LogFactory.getLog(ReadOnlyZooKeeperRegionAccessStrategy.class);

	private Hashtable<String, Object> memoryCache = new Hashtable<String, Object>();

	@SuppressWarnings("resource")
	public ReadOnlyZooKeeperRegionAccessStrategy(ZooKeeperPathRegion region) {
		super(region);

		final PathChildrenCache cache = new PathChildrenCache(
				getZooKeeperClient(), getRegionPath(), true);
		cache.getListenable().addListener(new PathChildrenCacheListener() {
			public void childEvent(CuratorFramework client,
					PathChildrenCacheEvent event) throws Exception {

				PathChildrenCacheEvent.Type eventType = event.getType();
				switch (eventType) {
				case CONNECTION_RECONNECTED:
					cache.rebuild();
					break;
				case CONNECTION_SUSPENDED:
				case CONNECTION_LOST:
					LOG.warn("ZooKeeper Cache Connection Suspended or Lost");
					System.out.println("222Connection error,waiting...");
					break;
				default:
					put(event.getData());
				}
			}
		});
		try {
			cache.start();
		} catch (Exception e) {
			throw new InovoutException(e);
		}
		initMemoryCache(cache);
	}
	private void put(ChildData data){
		memoryCache.put(data.getPath(), data.getData());
	}
	private void initMemoryCache(PathChildrenCache cache) {
		List<ChildData> datas = cache.getCurrentData();

		for (ChildData data : datas) {
			put(data);
		}

	}

	@Override
	public boolean contains(Object key) {
		return memoryCache.contains(key);
	}

	@Override
	public Object get(Object key) {
		return memoryCache.get(key);
	}

}
