package com.rsa.proc.config;

public class ConfigurationLoadingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1254967973977005760L;


	public ConfigurationLoadingException(String message) {
		super(message);
	}
	
	public ConfigurationLoadingException(String message, Throwable cause) {
		super(message, cause);
	}

}
