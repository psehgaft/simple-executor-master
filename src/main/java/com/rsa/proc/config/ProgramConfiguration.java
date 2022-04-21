package com.rsa.proc.config;

import java.io.File;
import java.util.List;

public class ProgramConfiguration {
	private String program;
	private File binaryFile;
	private List<Integer> successfulReturnValues;
	private String cacheDir;
	private String user;
	
	public String getProgram() {
		return program;
	}
	public void setProgram(String program) {
		this.program = program;
	}
	public File getBinaryFile() {
		return binaryFile;
	}
	public void setBinaryFile(File binaryFile) {
		this.binaryFile = binaryFile;
	}
	public List<Integer> getSuccessfulReturnValues() {
		return successfulReturnValues;
	}
	public void setSuccessfulReturnValues(List<Integer> successfulReturnValues) {
		this.successfulReturnValues = successfulReturnValues;
	}
	public String getCacheDir() {
		return cacheDir;
	}
	public void setCacheDir(String cacheDir) {
		this.cacheDir = cacheDir;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "ProgramConfiguration [program=" + program + ", binaryFile="
				+ binaryFile + ", successfulReturnValues="
				+ successfulReturnValues + ", cacheDir=" + cacheDir + ", user="
				+ user + "]";
	}

}
