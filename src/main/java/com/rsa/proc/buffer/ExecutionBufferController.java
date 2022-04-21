package com.rsa.proc.buffer;

import java.util.List;

import com.rsa.proc.buffer.dto.BufferAdditionResult;
import com.rsa.proc.buffer.dto.BufferedDestination;
import com.rsa.proc.dto.Program;

public interface ExecutionBufferController {
	public abstract BufferAdditionResult addExecution(String requestId, Program program);
	public abstract List<BufferedDestination> generateDestinationList(Program program);

}
