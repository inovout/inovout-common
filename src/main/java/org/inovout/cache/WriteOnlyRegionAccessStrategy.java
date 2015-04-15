package org.inovout.cache;

public interface WriteOnlyRegionAccessStrategy extends RegionAccessStrategy {
	void put(Object key, Object value);

	void remove(Object key);

	void removeAll();

}
