package org.inovout.cache;

public class PathCache {

	private final RegionAccessStrategy regionAccessStrategy;

	public PathCache(RegionAccessStrategy regionAccessStrategy) {
		this.regionAccessStrategy = regionAccessStrategy;
	}

	private WriteOnlyRegionAccessStrategy getWriteOnlyRegionAccessStrategy() {
		return (WriteOnlyRegionAccessStrategy) regionAccessStrategy;
	}

	private ReadOnlyRegionAccessStrategy getReaderOnlyRegionAccessStrategy() {
		return (ReadOnlyRegionAccessStrategy) regionAccessStrategy;
	}

	public void put(String key, Object value) {
		getWriteOnlyRegionAccessStrategy().put(key, value);
	}

	public Object get(String key) {
		return getReaderOnlyRegionAccessStrategy().get(key);
	}

	public String getRegionPath() {
		return ((PathRegion) this.regionAccessStrategy.getRegion())
				.getRegionPath();
	}
}
