package com.paulormg.recommendex.shared.exception;

public class ExistingUserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExistingUserException(){
		super();
	}
	
	public ExistingUserException(String message){
		super(message);
	}
	
}
