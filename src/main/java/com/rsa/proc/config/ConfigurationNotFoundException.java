package com.rsa.proc.config;

public class ConfigurationNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1254967973977005760L;


	public ConfigurationNotFoundException(String message) {
		super(message);
	}
	
	public ConfigurationNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
