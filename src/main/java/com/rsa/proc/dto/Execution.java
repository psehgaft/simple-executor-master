package com.rsa.proc.dto;

import java.util.Arrays;
import java.util.Date;

public class Execution {
	private Program program;
	private Date startDate;
	private Date endDate;
	private byte[] result;
	private Integer returnCode;
	private boolean executionSuccessful;
	private String errorDescription;
	private String stdout;
	private String stderr;
	

	public Program getProgram() {
		return program;
	}
	public void setProgram(Program program) {
		this.program = program;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public byte[] getResult() {
		return result;
	}
	public void setResult(byte[] result) {
		this.result = result;
	}
	public Integer getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(Integer returnCode) {
		this.returnCode = returnCode;
	}
	public boolean isExecutionSuccessful() {
		return executionSuccessful;
	}
	public void setExecutionSuccessful(boolean executionSuccessful) {
		this.executionSuccessful = executionSuccessful;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	public String getStdout() {
		return stdout;
	}
	public void setStdout(String stdout) {
		this.stdout = stdout;
	}
	public String getStderr() {
		return stderr;
	}
	public void setStderr(String stderr) {
		this.stderr = stderr;
	}
	
	@Override
	public String toString() {
		return "Execution [program=" + program + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", result="
				+ Arrays.toString(result) + ", returnCode=" + returnCode
				+ ", executionSuccessful=" + executionSuccessful
				+ ", errorDescription=" + errorDescription + ", stdout="
				+ stdout + ", stderr=" + stderr + "]";
	}	
}