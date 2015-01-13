package org.inovout.util;

import org.inovout.InovoutException;

public class UnknownEnumExternalNameException extends InovoutException {

	private static final long serialVersionUID = 151556131033765931L;


	public UnknownEnumExternalNameException(String enumName) {
		super( "Unknown access type [" + enumName + "]" );
	}
}
