package org.inovout.cache;

public interface WriteOnlyRegionAccessStrategy extends RegionAccessStrategy {
	void put(Object key, Object value);

	void remvoe(Object key);

	void removeAll();

}
