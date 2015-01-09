package org.inovout.cache.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.inovout.cache.AccessType;
import org.inovout.cache.PathRegion;
import org.inovout.cache.RegionAccessStrategy;
import org.inovout.util.StringUtils;

public class ZooKeeperPathRegion implements PathRegion {

	private CuratorFramework zookeeperClient;
	private final String name;
	private final String rootPath;
	private final String regionPath;

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

	@Override
	public void destroy() throws Exception {
		zookeeperClient.delete().forPath(rootPath);

	}

	@Override
	public RegionAccessStrategy buildAccessStrategy(AccessType accessType) {
		switch (accessType) {
		case READ_ONLY:
			return new ReadOnlyZooKeeperRegionAccessStrategy(this);
		case WRITE_ONLY:
			return new WriteOnlyZooKeeperRegionAccessStrategy(this);
		default:
			throw new IllegalArgumentException(
					"unrecognized access strategy type [" + accessType + "]");

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

}
