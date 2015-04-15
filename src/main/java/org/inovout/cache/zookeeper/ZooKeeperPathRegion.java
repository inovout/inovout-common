package org.inovout.cache.zookeeper;

import java.io.IOException;
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
import org.inovout.cache.AccessType;
import org.inovout.cache.PathRegion;
import org.inovout.cache.RegionAccessStrategy;
import org.inovout.util.JsonUtils;
import org.inovout.util.StringUtils;

public class ZooKeeperPathRegion implements PathRegion {

	private static final Log LOG = LogFactory.getLog(ZooKeeperPathRegion.class);

	private static final Hashtable<AccessType, RegionAccessStrategy> regionAccessStrategies = new Hashtable<AccessType, RegionAccessStrategy>();
	private final CuratorFramework zookeeperClient;
	private final String name;
	private final String rootPath;
	private final String regionPath;

	private Hashtable<String, Object> memoryCache = new Hashtable<String, Object>();
	private PathChildrenCache zookeeperCache;

	public ZooKeeperPathRegion(CuratorFramework zookeeperClient, String name,
			String rootPath) {
		this.zookeeperClient = zookeeperClient;
		this.name = name;

		this.rootPath = rootPath;
		this.regionPath = StringUtils.combinePathString(rootPath, name);
	}

	@Override
	public String getName() {
		return name;
	}

	public String getRootPath() {
		return rootPath;
	}

	@Override
	public RegionAccessStrategy buildAccessStrategy(AccessType accessType) {
		RegionAccessStrategy regionAccessStrategy = regionAccessStrategies
				.get(accessType);
		if (regionAccessStrategy == null) {
			switch (accessType) {
			case READ_ONLY:
				regionAccessStrategy = new ReadOnlyZooKeeperRegionAccessStrategy(
						this);
				initZooKeeperCache();
				break;
			case WRITE_ONLY:
				regionAccessStrategy = new WriteOnlyZooKeeperRegionAccessStrategy(
						this);
				initZooKeeperCache();
				break;
			default:
				throw new IllegalArgumentException(
						"unrecognized access strategy type [" + accessType
								+ "]");
			}
			regionAccessStrategies.put(accessType, regionAccessStrategy);
		}
		return regionAccessStrategy;
	}

	private void initZooKeeperCache() {
		synchronized (zookeeperClient) {
			if (zookeeperCache != null) {
				return;
			}

			// 已经在内部实现异步线程
			zookeeperCache = new PathChildrenCache(getZooKeeperClient(),
					getRegionPath(), true);
			zookeeperCache.getListenable().addListener(
					createZooKeeperCacheListener());

			try {
				zookeeperCache.start();
			} catch (Exception e) {
				throw new InovoutException(e);
			}
			initMemoryCache(zookeeperCache);
		}
	}

	private PathChildrenCacheListener createZooKeeperCacheListener() {
		return new PathChildrenCacheListener() {
			public void childEvent(CuratorFramework client,
					PathChildrenCacheEvent event) throws Exception {

				PathChildrenCacheEvent.Type eventType = event.getType();
				switch (eventType) {
				case CONNECTION_RECONNECTED:
					zookeeperCache.rebuild();
					break;
				case CONNECTION_SUSPENDED:
				case CONNECTION_LOST:
					LOG.warn("ZooKeeper Cache Connection Suspended or Lost");
					break;
				default:
					put(event.getData());
				}
			}
		};
	}

	private void put(ChildData data) {
		memoryCache.put(data.getPath(), JsonUtils.getString(data.getData()));
	}

	private void initMemoryCache(PathChildrenCache cache) {
		List<ChildData> datas = cache.getCurrentData();

		for (ChildData data : datas) {
			put(data);
		}

	}

	public String getPath(String path) {
		return StringUtils.combinePathString(getRegionPath(), path);
	}

	public CuratorFramework getZooKeeperClient() {
		return zookeeperClient;
	}

	public String getRegionPath() {
		return regionPath;
	}

	void put(String key, Object value) throws Exception {
		if (contains(key)) {
			getZooKeeperClient().setData().forPath(getPath(key),
					JsonUtils.getBytes(value));
		} else {
			getZooKeeperClient().create().forPath(getPath(key),
					JsonUtils.getBytes(value));
		}

	}

	Object get(String path) {
		Object value = memoryCache.get(getPath(path));
		if (value == null) {
			try {
				value = JsonUtils.getString(
						getZooKeeperClient().getData().forPath(getPath(path)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new InovoutException("从zookeeper中获取值失败！");
			}
		}
		return value;

	}

	boolean contains(String path) {
		try {
			return getZooKeeperClient().checkExists().forPath(getPath(path)) != null;
		} catch (Exception e) {
			throw new InovoutException(e);
		}
	}

	void remove(String path) {
		try {
			getZooKeeperClient().delete().forPath(getPath(path));
		} catch (Exception e) {
			throw new InovoutException(e);
		}
	}

	void removeAll() {
		destroy();
	}

	@Override
	public void destroy() {
		try {
			getZooKeeperClient().delete().forPath(getRegionPath());
		} catch (Exception e) {
			throw new InovoutException(e);

		}
	}

	void dispose() {
		try {
			if (zookeeperCache != null) {
				zookeeperCache.close();
			}
		} catch (IOException e) {
			throw new InovoutException(e);
		}

	}

}
