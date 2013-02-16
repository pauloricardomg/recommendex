package com.paulormg.recommendex.shared.transfer;

import java.io.Serializable;
import java.util.Map;

@SuppressWarnings("serial")
public class UserData implements Serializable {

	private User user;
	private Map<Integer, Item> items;
	private boolean newUser;
	
	public UserData(){
		//required by gwt sdk
	}
	
	public UserData(User user, Map<Integer, Item> items, boolean newUser) {
		this.user = user;
		this.items = items;
		this.newUser = newUser;
	}

	public User getUser() {
		return user;
	}

	public Map<Integer, Item> getItems() {
		return items;
	}

	public boolean isNewUser() {
		return newUser;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setUserItems(Map<Integer, Item> items) {
		this.items = items;
	}

	public void setNewUser(boolean newUser) {
		this.newUser = newUser;
	}
	
}
