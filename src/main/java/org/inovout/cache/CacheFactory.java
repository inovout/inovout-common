package org.inovout.cache;

import org.inovout.cache.zookeeper.ZooKeeperRegionFactory;
import org.inovout.config.Configuration;
import org.inovout.util.ReflectionUtils;

public class CacheFactory {

	public static PathBuilder builderPathCache() {
		return new PathBuilder();
	}

	public static class PathBuilder {
		private static final String PATH_REGION_FACTORY_CLASS = ZooKeeperRegionFactory.class
				.getName();
		private String regionName;

		public PathBuilder setRegionName(String regionName) {
			this.regionName = regionName;
			return this;
		}

		private AccessType accessType;

		public PathBuilder setAccessType(AccessType accessType) {
			this.accessType = accessType;
			return this;
		}

		private String rootPath;

		public PathBuilder setRootPath(String rootPath) {
			this.rootPath = rootPath;
			return this;
		}

		private static final String PATH_CACHE_ROOT_PATH_KEY = "path_cache.root.path";
		private static final String DEFAULT_PATH_CACHE_ROOT_PATH = "/3cloud/cache/";
		private static final Configuration configuration;
		static {
			configuration = new Configuration();
			configuration.addResource("cache.xml");
		}

		public PachCache build() {
			
			rootPath = configuration.get(PATH_CACHE_ROOT_PATH_KEY,
					rootPath == null ? DEFAULT_PATH_CACHE_ROOT_PATH : rootPath);	
			
			RegionFactory regionFactory = (RegionFactory) ReflectionUtils.newInstance(PATH_REGION_FACTORY_CLASS);
			regionFactory.start();
			return new PachCache(regionFactory.buildPathRegion(regionName,
					rootPath).buildAccessStrategy(accessType));
		}
	}
}
