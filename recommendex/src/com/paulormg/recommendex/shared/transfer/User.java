package com.paulormg.recommendex.shared.transfer;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable {
	
	private int id;
	private String login;
	private boolean admin;
	
	public User(){
		//required by gwt sdk
	}
	
	public User(int id, String login, boolean admin) {
		this.id = id;
		this.login = login;
		this.admin = admin;
	}
	
	public int getId(){
		return id;
	}
	
	public String getLogin() {
		return login;
	}
	
	public boolean isAdmin() {
		return admin;
	}

	@Override
	public String toString() {
		return "User [login=" + login + ", admin=" + admin + "]";
	}
	
}
