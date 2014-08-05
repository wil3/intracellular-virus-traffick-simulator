package edu.stevens.cs.virustraffic.graph;

public class GraphConfigurationException extends Exception{

	public GraphConfigurationException(){
		super();
	}
	public GraphConfigurationException(String message){
		super(message);
	}
	public GraphConfigurationException(Throwable throwable){
		super(throwable);
	}
	public GraphConfigurationException(String message,Throwable throwable){
		super(message,throwable);
	}
}
