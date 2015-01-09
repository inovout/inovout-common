package org.inovout.cache;

public interface RegionFactory {
	void start();

	void stop();

	public PathRegion buildPathRegion(String regionName,String rootPath);

	
	
}
