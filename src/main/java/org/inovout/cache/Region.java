package org.inovout.cache;

public interface Region {
	String getName();

/*
  	Map<?, ?> toMap();
*/
	void destroy() throws Exception;

	RegionAccessStrategy buildAccessStrategy(AccessType accessType);
}
