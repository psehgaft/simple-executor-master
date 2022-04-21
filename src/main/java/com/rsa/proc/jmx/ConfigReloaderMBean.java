package com.rsa.proc.jmx;

import com.rsa.proc.config.ConfigurationLoadingException;

public interface ConfigReloaderMBean {
	public abstract void reloadConfiguration() throws ConfigurationLoadingException;
}
