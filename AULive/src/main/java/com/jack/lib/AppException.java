package com.jack.lib;

/**
 * @author jack.long
 * @version create time：Dec 4, 2012 1:26:48 PM general exception
 */
public class AppException extends Exception {

	private static final long serialVersionUID = 1L;

	public enum ErrorType {
		SyncException, DatabaseException, MigrationException, JsonException, CloudException, CancelException, ConnectionException, FileException, NormalException
	}

	public static final String SYNC_EXCEPTION = "Synchronization error";
	public static final String SYNC_SUCCESS = "SUCCESS";
	public static final String SYNC_ERROR = "ERROR";
	public static final String SYNC_BLOCKED = "BLOCKED";

	private ErrorType error;
	private UserInfo errorInfo;
	private String exception;

	public AppException(ErrorType error, String exception,
			String detailMessage, UserInfo errorInfo) {
		super(detailMessage);
		this.error = error;
		this.errorInfo = errorInfo;
		this.exception = exception;
	}

	public ErrorType getError() {
		return error;
	}

	public UserInfo getErrorInfo() {
		return errorInfo;
	}

	public String getException() {
		return exception;
	}
}
