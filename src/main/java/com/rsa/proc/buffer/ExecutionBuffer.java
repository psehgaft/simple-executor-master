package com.rsa.proc.buffer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rsa.proc.ProgramExecutor;
import com.rsa.proc.buffer.dto.BufferedDestination;
import com.rsa.proc.dto.Execution;
import com.rsa.proc.dto.Program;

public class ExecutionBuffer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionBuffer.class);
	
	private ExecutionBufferController controller;
	private ProgramExecutor executor;
	private String tempLogPath;
	
	public List<BufferedDestination> execute(Program program) throws IOException{
		List<BufferedDestination> destinations;
		Execution execution;
		File log;
		String logID = UUID.randomUUID().toString();
		
		LOGGER.info("Commencing execution for program: " + program);
		
		execution = this.executor.execute(program);
		
		destinations = this.controller.generateDestinationList(program);
		
		log = new File(tempLogPath + program.getName() + logID + ".log");
		BufferedWriter bw = new BufferedWriter(new FileWriter(log, true));
		bw.write("Assigning response: " + execution + " to " + destinations +"\r\n");
		bw.close();
		
		LOGGER.info("Finaliza buffer " + tempLogPath + program.getName() + logID + ".log");	

		
		if(destinations != null){
			for (BufferedDestination d : destinations){
				d.setExecutionResult(execution);
			}
		}
		
		return destinations;
	}
	
	public ExecutionBufferController getController() {
		return controller;
	}

	public void setController(ExecutionBufferController controller) {
		this.controller = controller;
	}

	public ProgramExecutor getExecutor() {
		return executor;
	}

	public void setExecutor(ProgramExecutor executor) {
		this.executor = executor;
	}
	
	public String getTempLogPath() {
		return tempLogPath;
	}

	public void setTempLogPath(String tempLogPath) {
		this.tempLogPath = tempLogPath;
	}
	
}
