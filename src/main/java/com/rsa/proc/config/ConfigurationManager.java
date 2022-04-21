package com.rsa.proc.config;


public interface ConfigurationManager {
	
	public static final String PROGRAM_BINARY_PATH = "path";
	public static final String PROGRAM_SUCCESS_RETURN_VALUES = "success-return-values";
	public static final String PROGRAM_CACHE_DIR = "cache-dir";
	public static final String PROGRAM_USER = "user";

	public abstract ProgramConfiguration getProgramConfiguration(
			String programName) throws ConfigurationNotFoundException;
	public abstract void reloadConfiguration() throws ConfigurationLoadingException;

}