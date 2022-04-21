	package com.rsa.proc.buffer.dto;

import com.rsa.proc.dto.Execution;

public class BufferedDestination {
	private String destinationId;
	private Execution executionResult;
	public String getDestinationId() {
		return destinationId;
	}
	public void setDestinationId(String destinationId) {
		this.destinationId = destinationId;
	}
	public Execution getExecutionResult() {
		return executionResult;
	}
	public void setExecutionResult(Execution executionResult) {
		this.executionResult = executionResult;
	}
	@Override
	public String toString() {
		return "BufferedDestination [destinationId=" + destinationId
				+ ", executionResult=" + executionResult + "]";
	}
}
