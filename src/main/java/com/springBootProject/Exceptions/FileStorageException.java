package com.springBootProject.Exceptions;

public class FileStorageException extends RuntimeException {

	/**
	 * The serialVersionUID used for verification of serialization
	 */
	private static final long serialVersionUID = 1L;

	public FileStorageException(String message) {
		super(message);
	}

	public FileStorageException(String message, Throwable cause) {
		super(message, cause);
	}

}
