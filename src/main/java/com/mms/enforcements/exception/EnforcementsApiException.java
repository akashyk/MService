package com.mms.enforcements.exception;

/**
 * Custom exception class for Api related tasks
 * 
 * @author akashyellappa
 *
 */
public class EnforcementsApiException extends Exception {

	public EnforcementsApiException(String message, Exception e) {
		super(message);
	}
}
