package com.univeit.kcm.exception;

public class SongException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5653244707904870910L;

	public SongException(String message) {
		super(message);
	}
	
	public SongException(String message, Throwable cause) {
		super(message, cause);
	}
}
