package com.rsa.proc.config.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rsa.proc.config.ConfigurationLoadingException;
import com.rsa.proc.config.ConfigurationManager;
import com.rsa.proc.config.ConfigurationNotFoundException;
import com.rsa.proc.config.ProgramConfiguration;

public class ConfigurationManagerPropertiesImpl implements ConfigurationManager {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationManagerPropertiesImpl.class);
	
	private File file;

	private HashMap<String,ProgramConfiguration> configuration;
	
	/* (non-Javadoc)
	 * @see com.rsa.proc.config.ConfigurationManager#getProgramConfiguration(java.lang.String)
	 */
	
	public ConfigurationManagerPropertiesImpl() throws IOException{
		this.loadProperties(this.file);
	}
	
	public ConfigurationManagerPropertiesImpl(File file) throws IOException{
		this.file = file;
		this.loadProperties(file);
	}
	
	private void loadProperties(File file) throws IOException{
		Properties properties= new Properties();
		Set<Entry<Object, Object>> propertiesSet;
		String propertyBuffer;
		ProgramConfiguration configurationBuffer;
		properties.load(new FileInputStream(file));
		HashMap<String,ProgramConfiguration> configuration;
		
		
		configuration = new HashMap<String,ProgramConfiguration>();
		
		ConfigurationManagerPropertiesImpl.LOGGER.debug("Properties: {}", properties);
		
		propertiesSet = properties.entrySet();
		for(Entry<Object,Object> entry : propertiesSet){
			propertyBuffer = (String) entry.getKey();
			propertyBuffer = propertyBuffer.substring(0, propertyBuffer.indexOf("."));
			
			if(!configuration.containsKey(propertyBuffer)){
				configurationBuffer = new ProgramConfiguration();
				configurationBuffer.setProgram(propertyBuffer);
				configuration.put(propertyBuffer, configurationBuffer);
			}else{
				configurationBuffer = configuration.get(propertyBuffer);
			}
			
			this.setProgramConfigurationProperty(configurationBuffer,(String) entry.getKey(), (String) entry.getValue());
		}
		
		this.configuration = configuration;
		ConfigurationManagerPropertiesImpl.LOGGER.debug("Configuration: {}", this.configuration);
		
	}
	
	private void setProgramConfigurationProperty(
			ProgramConfiguration configurationBuffer, String javaProperty, String value) {
		String property;
		LinkedList<Integer> codes;
		String [] stringCodes;
		
		property = javaProperty.substring(javaProperty.indexOf(".") + 1);
		
		if(property.equals(ConfigurationManager.PROGRAM_BINARY_PATH)){
			configurationBuffer.setBinaryFile(new File(value));
		}else if(property.equals(ConfigurationManager.PROGRAM_SUCCESS_RETURN_VALUES)){
			stringCodes = value.split(",");
			codes = new LinkedList<Integer>();
			if(stringCodes.length > 0){
				for(String code : stringCodes){
					codes.add(Integer.valueOf(code));
				}
				configurationBuffer.setSuccessfulReturnValues(
						codes);
			}
		}else if(property.equals(ConfigurationManager.PROGRAM_CACHE_DIR)){
			configurationBuffer.setCacheDir(value);
		}else if(property.equals(ConfigurationManager.PROGRAM_USER)){
			configurationBuffer.setUser(value);
		}
		
	}

	@Override
	public ProgramConfiguration getProgramConfiguration(String programName) throws ConfigurationNotFoundException{
		ProgramConfiguration configuration;
		StringBuffer messageBuffer;
		
		if((configuration = this.configuration.get(programName)) == null){
			messageBuffer = new StringBuffer("Configuration for program ");
			messageBuffer.append(programName);
			messageBuffer.append(" not found.");
			throw new ConfigurationNotFoundException(messageBuffer.toString());
		}
		
		return configuration;
	}
	
	@Override
	public void reloadConfiguration() throws ConfigurationLoadingException{
		try{
			this.loadProperties(this.file);
		}catch(IOException ioexc){
			throw new ConfigurationLoadingException("Error reloading configuration using file " + this.file, ioexc);
		}
		
	}
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	

}
