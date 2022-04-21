package com.rsa.proc;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rsa.proc.config.ConfigurationEnvironment;
import com.rsa.proc.config.ConfigurationManager;
// import com.rsa.proc.config.ConfigurationEnvironment;
import com.rsa.proc.config.ConfigurationNotFoundException;
import com.rsa.proc.config.ProgramConfiguration;
import com.rsa.proc.dto.Execution;
import com.rsa.proc.dto.Program;
import com.rsa.proc.streams.AccumulatingLogOutputStream;
public class ProCExecutorCommonsExec implements ProgramExecutor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProCExecutorCommonsExec.class);
	
	private static final String[] STRING_ARRAY_ZERO = new String[0]; 
			
	private ConfigurationManager configurationManager;
	private String tempDir;
	private int bufferSize;
	private File environmentPath;
	private String tempLogPath;
//	private ConfigurationEnvironment environmentVal; 
	private static HashMap<String, String> environmentVal;
	
	/* Static parameters sender	
	private static final String[] enviroment = new String[] {
			"ALEA_AYUDA=\\\\10.142.66.54\\Alea10g\\AYUDA",
			"ALEA_BIN=\\\\10.142.66.54\\Alea10g\\BIN",
			"ALEA_C=\\\\10.142.66.54\\Alea10g\\C",
			"ALEA_CIA='SEMA#GROUP'",
			"ALEA_CIAS='SEMA#GROUP'",
			"ALEA_CMD=\\\\10.142.66.54\\Alea10g\\CMD",
			"ALEA_CNF=\\\\10.142.66.54\\Alea10g\\CNF",
			"ALEA_DES=N",
			"ALEA_ENT=X",
			"ALEA_EXP=\\\\10.142.66.54\\Alea10g\\EXPORT",
			"ALEA_FMB=\\\\10.142.66.54\\Alea10g\\FMBS",
			"ALEA_FRM=\\\\10.142.66.54\\Alea10g\\FMXS",
			"ALEA_HOME=\\\\10.142.66.54\\Alea10g\\",
			"ALEA_INS=\\\\10.142.66.54\\Alea10g\\INSTALL\\SQL",
			"ALEA_KOBJEC=\\\\10.142.66.54\\Alea10g\\KOBJEC",
			"ALEA_KO_BIN=\\\\10.142.66.54\\oracle10\\",
			"ALEA_LST=\\\\10.142.66.54\\Alea10g\\LISTADOS",
			"ALEA_PATH=\\\\10.142.66.54\\Alea10g\\;\\\\10.142.66.54\\Alea10g\\CMD;\\\\10.142.66.54\\Alea10g\\UTL;\\\\10.142.66.54\\oracle10\\ora6ir17\\BIN",
			"ALEA_REP=\\\\10.142.66.54\\Alea10g\\REP",
			"ALEA_RPW=\\\\10.142.66.54\\Alea10g\\RPW",
			"ALEA_SQL=\\\\10.142.66.54\\Alea10g\\SQL",
			"ALEA_TAB=\\\\10.142.66.54\\Alea10g\\INSTALL\\TABLA",
			"ALEA_TMP=C:\\\\TMP",
			"ALEA_TRC=S",
			"ALEA_USER=\\",
			"ALEA_UTL=\\\\10.142.66.54\\Alea10g\\UTL",
			"ARCH=%ALEA_TMP%\\BBVLIPEM.TXT",
			"SystemDrive=C:",
			"SystemRoot=C:\\\\Windows",
			"USERDNSDOMAIN=ROYALSUN.MX",
			"USERDOMAIN=ROYALSUN",
			"USERNAME=DCABELLO",
			"USERPROFILE=C:\\\\Users\\",
			"LOGONSERVER=\\MEXSPADIR02",
			"TNS_ADMIN=C:\\\\Program Files\\Common Files\\Quest Shared\\instantclient-basic-win32-10.1.0.5-20060419",
			"PATH=\\\\10.142.66.54\\oracle10\\oracle10\\bin;\\\\10.142.66.54\\oracle10\\ora6ir17\\bin;%SystemRoot%\\system32;%SystemRoot%;%SystemRoot%\\System32\\Wbem;%alea_path%",
			"ORACLE_HOME=\\\\10.142.66.54\\oracle10\\oracle10",
			"NLS_LANG=American_America.WE8ISO8859P1"
			};
	
	
	
	public ProCExecutorCommonsExec(){
		this.bufferSize = 10240;

			for (int i = 0; i < enviroment.length; i++) {
				String env[] = enviroment[i].split("=");
				environmentVal.put(env[0], env[1]);
			}
	}
	*/
	
	/* (non-Javadoc)Manager
	 * @see com.rsa.proc.ProgramExecutor#execute(com.rsa.proc.dto.Program)
	 */
	@Override
	public Execution execute(Program program) throws IOException{
		ProgramConfiguration programConfiguration;
		Execution result = new Execution();
		CommandLine commandLine;
		LinkedList<String> arguments;
		DefaultExecutor executor = new DefaultExecutor();
		PumpStreamHandler streamHandler;
		AccumulatingLogOutputStream stderrAccumulator = new AccumulatingLogOutputStream();
		AccumulatingLogOutputStream stdoutAccumulator = new AccumulatingLogOutputStream();
		Integer returnCode = null;
		StringBuffer tempFileBuffer;
		File tempFile;
		File log;
		String logID = UUID.randomUUID().toString();
		
		ConfigurationEnvironment environment = new ConfigurationEnvironment(this.environmentPath);
		environmentVal = environment.getEnvironmentProperties();
		
		
		ProCExecutorCommonsExec.LOGGER.debug("Executing program: {}", program);
		
		result.setProgram(program);
		result.setExecutionSuccessful(true);
		result.setStartDate(Calendar.getInstance().getTime());
		
		tempFileBuffer = new StringBuffer(this.tempDir);
		tempFileBuffer.append("/");
		tempFileBuffer.append(program.getName());
		tempFileBuffer.append(UUID.randomUUID().toString());
		
		tempFile = new File(tempFileBuffer.toString());
		
		ProCExecutorCommonsExec.LOGGER.debug("Using temp file: {}", tempFile.getAbsoluteFile());
		
		try {
			
			programConfiguration = this.configurationManager.getProgramConfiguration(program.getName());
						
			arguments = new LinkedList<String>();
			arguments.addAll(program.getArguments());
			
			this.appendFixedArguments(arguments, programConfiguration, tempFile);
			
			commandLine = new CommandLine(programConfiguration.getBinaryFile());
			commandLine.addArguments(arguments.toArray(ProCExecutorCommonsExec.STRING_ARRAY_ZERO));
			
			streamHandler = new PumpStreamHandler(stdoutAccumulator, stderrAccumulator);
			executor.setStreamHandler(streamHandler);
			executor.setExitValues(null);
		    
			ProCExecutorCommonsExec.LOGGER.debug("Commencing execution. Program: {}; Config: {}; Real arguments: {}", program, programConfiguration, arguments);
			
				
			/* Example to send parameters to execute proC
			commandLine.addArguments("1 420 M 4341 0 C:/RedHat/tempDir/temp.txt C:/RedHat/tempDir/ OPS$FEADM/Alearoyal1d@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=10.142.74.57)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=ALEAC10G)))");
			*/
			returnCode = executor.execute(commandLine, environmentVal);
			
			

			result.setExecutionSuccessful(programConfiguration.getSuccessfulReturnValues() == null || programConfiguration.getSuccessfulReturnValues().isEmpty() ||
					programConfiguration.getSuccessfulReturnValues().contains(returnCode));
					
			log = new File(tempLogPath + program.getName() + logID + ".log");
			BufferedWriter bw = new BufferedWriter(new FileWriter(log, true));
			bw.write("Return code:" + returnCode +"\r\n");
			bw.close();
			
			/* Cause Out Of Memory in the Hawtio Console
			ProCExecutorCommonsExec.LOGGER.debug("Execution finished. Return code: {}. Recovering file... ", returnCode);
			*/
			
			
			result.setResult(this.readProgramResultFile(tempFile));
			
			ProCExecutorCommonsExec.LOGGER.debug("Recovery complete. Deleting file.");
			
			if(!tempFile.delete()){
				ProCExecutorCommonsExec.LOGGER.error("Could not delete file " + tempFile.getAbsolutePath());
			}
			
		}catch (IOException e) {
			ProCExecutorCommonsExec.LOGGER.error("Exception executing program {}", program, e);
			result.setExecutionSuccessful(false);
			result.setErrorDescription(e.getMessage());
		}catch (ConfigurationNotFoundException e) {
			ProCExecutorCommonsExec.LOGGER.error("Exception executing program {}", program, e);
			result.setExecutionSuccessful(false);
			result.setErrorDescription(e.getMessage());
		}finally{
			result.setEndDate(Calendar.getInstance().getTime());
			result.setReturnCode(returnCode);
			result.setStderr(stderrAccumulator.getStreamContent());
			result.setStdout(stdoutAccumulator.getStreamContent());
		}
		
		ProCExecutorCommonsExec.LOGGER.debug("Returning result: {}", result);
			
		LOGGER.info("Finaliza la ejecucion " + tempLogPath + program.getName() + logID + ".log");
		
		log = new File(tempLogPath + program.getName() + logID + ".log");
		BufferedWriter bw = new BufferedWriter(new FileWriter(log, true));
		bw.write(result.toString() +"\r\n");
		bw.close();
		
		return result;
	}

	private void appendFixedArguments(LinkedList<String> arguments,
				ProgramConfiguration programConfiguration, File tempFile) {
			
			arguments.add(tempFile.getAbsolutePath());
			
			if(programConfiguration.getCacheDir() != null){
				
				arguments.add(programConfiguration.getCacheDir());
			}
			
			if(programConfiguration.getUser() != null){
				arguments.add(programConfiguration.getUser());
			}
			
			
		}

	private byte[] readProgramResultFile(File tempFile) throws IOException{
		byte[] result;
		ByteArrayOutputStream bos = new ByteArrayOutputStream(this.bufferSize);
		FileInputStream fis = new FileInputStream(tempFile);
		int buffer;
		
		while((buffer = fis.read()) >= 0){
			bos.write(buffer);
		}
		
		fis.close();
		bos.flush();
		result = bos.toByteArray();
		
		bos.close();
		
		return result;
	}

	public ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}

	/* (non-Javadoc)
	 * @see com.rsa.proc.ProgramExecutor#setConfigurationManager(com.rsa.proc.config.ConfigurationManager)
	 */
	@Override
	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}
	

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public String getTempDir() {
		return tempDir;
	}

	/* (non-Javadoc)
	 * @see com.rsa.proc.ProgramExecutor#setTempDir(java.lang.String)
	 */
	@Override
	public void setTempDir(String tempDir) {
		this.tempDir = tempDir;
	}
	
	public File getEnvironmentPath() {
		return environmentPath;
	}

	public void setEnvironmentPath(File environmentPath) {
		this.environmentPath = environmentPath;
	}
	
	public String getTempLogPath() {
		return tempLogPath;
	}

	public void setTempLogPath(String tempLogPath) {
		this.tempLogPath = tempLogPath;
	}
	

}
