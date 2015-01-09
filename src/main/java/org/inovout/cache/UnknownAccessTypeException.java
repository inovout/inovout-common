package org.inovout.cache;

import org.inovout.InovoutException;

public class UnknownAccessTypeException extends InovoutException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 151556131033765931L;

	/**
	 * Constructs the UnknownAccessTypeException.
	 *
	 * @param accessTypeName The external name that could not be resolved.
	 */
	public UnknownAccessTypeException(String accessTypeName) {
		super( "Unknown access type [" + accessTypeName + "]" );
	}
}
