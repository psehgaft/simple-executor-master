package com.rsa.proc.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationEnvironment {
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationEnvironment.class);
	    
		private File file;
			
		private HashMap<String, String> environment;
		
		public ConfigurationEnvironment() throws IOException{
			this.loadProperties(this.file);
		}
		
		public ConfigurationEnvironment(File file) throws IOException{
			this.file = file;
		    this.loadProperties(file);
		}

		private void loadProperties(File file) throws IOException{
			/**Creamos un Objeto de tipo Properties*/
			Properties properties= new Properties();
			
			/**Creamos un Objeto de tipo Set<Entry> para recorrer las propiedades*/
			Set<Entry<Object, Object>> propertiesSet;
			
			/**Cargamos el archivo desde la ruta especificada*/
			properties.load(new FileInputStream(file));
			
			/**Creamos un HashMap para almacenar las variables de entorno*/
			environment = new HashMap<String, String>();
			
			ConfigurationEnvironment.LOGGER.debug("Properties: {}", properties);
			
			propertiesSet = properties.entrySet();
			for(Entry<Object,Object> entry : propertiesSet)			
				environment.put((String) entry.getKey(), (String) entry.getValue());
			
			ConfigurationEnvironment.LOGGER.debug("Configuration: {}", environment);	
			
		}
				
		public void reloadConfiguration() throws ConfigurationLoadingException {
			try{
				this.loadProperties(this.file);
			}catch(IOException ioexc){
				throw new ConfigurationLoadingException("Error reloading configuration using file " + this.file, ioexc);
			}
		}
		
		public HashMap<String, String> getEnvironmentProperties(){
			return this.environment;
		}

		public File getFile() {
			return file;
		}

		public void setFile(File file) {
			this.file = file;
		}
}