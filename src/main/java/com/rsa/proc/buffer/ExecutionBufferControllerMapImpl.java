package com.rsa.proc.buffer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rsa.proc.buffer.dto.BufferAdditionResult;
import com.rsa.proc.buffer.dto.BufferedDestination;
import com.rsa.proc.dto.Program;

public class ExecutionBufferControllerMapImpl implements ExecutionBufferController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionBufferControllerMapImpl.class);
	
	private HashMap<Program,List<BufferedDestination>> bufferList;
	
	public ExecutionBufferControllerMapImpl() {
		this.bufferList = new HashMap<Program, List<BufferedDestination>>();
	}
	
	@Override
	public synchronized BufferAdditionResult addExecution(String requestId, Program program) {
		List<BufferedDestination> destinations;
		BufferAdditionResult result = new BufferAdditionResult();
		BufferedDestination bufferedDestination = new BufferedDestination();
		
		ExecutionBufferControllerMapImpl.LOGGER.info("Received: " + requestId + "; " + program);
		
		bufferedDestination.setDestinationId(requestId);
		result.setProgram(program);
		
		destinations = this.bufferList.get(program);
		
		if(destinations != null){
			result.setNewExecution(false);
			ExecutionBufferControllerMapImpl.LOGGER.info("Program register found. Destination list: " + destinations);
		}else{
			destinations = new LinkedList<BufferedDestination>();
			this.bufferList.put(program, destinations);
			result.setNewExecution(true);
			ExecutionBufferControllerMapImpl.LOGGER.info("Generating new destination list, since the program was not yet registered");
		}
		
		destinations.add(bufferedDestination);
		ExecutionBufferControllerMapImpl.LOGGER.info("Final destinations list: " + destinations);
		
		return result;
	}

	@Override
	public synchronized List<BufferedDestination> generateDestinationList(Program program) {
		List<BufferedDestination> destinations;
		
		destinations = this.bufferList.get(program);
		this.bufferList.remove(program);
		
		return destinations;
	}

}
