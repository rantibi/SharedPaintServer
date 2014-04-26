package com.sharedpaint.beans;

/**
 * Base project exception
 */
public class SharedPaintException extends Exception {
	private static final long serialVersionUID = 1L;

	public SharedPaintException() {
		super();
	}

	public SharedPaintException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SharedPaintException(String message, Throwable cause) {
		super(message, cause);
	}

	public SharedPaintException(String message) {
		super(message);
	}

	public SharedPaintException(Throwable cause) {
		super(cause);
	}

	
	
}
