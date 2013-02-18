package com.paulormg.recommendex.shared.exception;

public class ServerError extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServerError(){
		super();
	}
	
	public ServerError(String message){
		super(message);
	}
	
}
