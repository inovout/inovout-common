package org.inovout.util;

public interface TypeConverter<S, D> {
	public D convert(S source);
}
