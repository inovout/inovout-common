package org.inovout.cache;

import org.inovout.util.UnknownEnumExternalNameException;

public enum AccessType {
	READ_ONLY("read-only"), WRITE_ONLY("write-only"), READ_WRITE("read-write");

	private final String externalName;

	private AccessType(String externalName) {
		this.externalName = externalName;
	}

	/**
	 * Get the corresponding externalized name for this value.
	 *
	 * @return The corresponding externalized name.
	 */
	public String getExternalName() {
		return externalName;
	}

	@Override
	public String toString() {
		return "AccessType[" + externalName + "]";
	}

	/**
	 * Resolve an AccessType from its external name.
	 *
	 * @param externalName
	 *            The external representation to resolve
	 *
	 * @return The access type.
	 *
	 * @throws UnknownEnumExternalNameException
	 *             If the externalName was not recognized.
	 *
	 * @see #getExternalName()
	 */
	public static AccessType fromExternalName(String externalName) {
		if (externalName == null) {
			return null;
		}
		for (AccessType accessType : AccessType.values()) {
			if (accessType.getExternalName().equals(externalName)) {
				return accessType;
			}
		}
		throw new UnknownEnumExternalNameException(externalName);
	}
}
