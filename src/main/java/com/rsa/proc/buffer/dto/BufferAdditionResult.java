package com.rsa.proc.buffer.dto;

import com.rsa.proc.dto.Program;

public class BufferAdditionResult {
	private boolean newExecution;
	private Program program;
	public boolean isNewExecution() {
		return newExecution;
	}
	public void setNewExecution(boolean newExecution) {
		this.newExecution = newExecution;
	}
	public Program getProgram() {
		return program;
	}
	public void setProgram(Program program) {
		this.program = program;
	}
	@Override
	public String toString() {
		return "BufferAdditionResult [newExecution=" + newExecution
				+ ", program=" + program + "]";
	}
	
}
