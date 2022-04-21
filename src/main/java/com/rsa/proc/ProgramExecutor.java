package com.rsa.proc;

import java.io.IOException;

import com.rsa.proc.config.ConfigurationManager;
import com.rsa.proc.dto.Execution;
import com.rsa.proc.dto.Program;

public interface ProgramExecutor {

	public abstract Execution execute(Program program) throws IOException;
	
	public abstract void setConfigurationManager(
			ConfigurationManager configurationManager);

	public abstract void setTempDir(String tempDir);

}