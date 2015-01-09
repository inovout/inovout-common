package org.inovout.cache;

public interface RegionAccessStrategy {
	boolean contains(Object key) throws Exception;
}
