package org.inovout.cache;

public interface ReadOnlyRegionAccessStrategy extends RegionAccessStrategy{
	Object get(Object key);
}
