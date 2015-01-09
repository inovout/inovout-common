package org.inovout;

public class InovoutException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2758132079894718833L;

	/**
	 * Constructs a HibernateException using the given exception message.
	 *
	 * @param message The message explaining the reason for the exception
	 */
	public InovoutException(String message) {
		super( message );
	}

	/**
	 * Constructs a HibernateException using the given message and underlying cause.
	 *
	 * @param cause The underlying cause.
	 */
	public InovoutException(Throwable cause) {
		super( cause );
	}

	/**
	 * Constructs a HibernateException using the given message and underlying cause.
	 *
	 * @param message The message explaining the reason for the exception.
	 * @param cause The underlying cause.
	 */
	public InovoutException(String message, Throwable cause) {
		super( message, cause );
	}
}
