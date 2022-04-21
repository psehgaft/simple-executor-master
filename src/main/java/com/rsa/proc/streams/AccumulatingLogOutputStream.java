package com.rsa.proc.streams;

import java.util.LinkedList;

import org.apache.commons.exec.LogOutputStream;

public class AccumulatingLogOutputStream extends LogOutputStream {
    private final LinkedList<String> buffer;
    
    public AccumulatingLogOutputStream(){
    	this.buffer = new LinkedList<String>();
    }
    
    @Override 
    protected void processLine(String line, int level) {
        this.buffer.add(line);
    }   
    
    public String getStreamContent(){
    	StringBuffer stream = new StringBuffer();
    	for(String line : this.buffer){
    		stream.append(line);
    		stream.append("\n");
    	}
    	if(stream.length() > 0){
    		stream.deleteCharAt(stream.length() - 1);
    	}
    	return stream.toString();
    }
}