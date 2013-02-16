package com.paulormg.recommendex.server.dao.user;

import java.util.HashMap;

import com.paulormg.recommendex.shared.exception.ExistingUserException;
import com.paulormg.recommendex.shared.exception.InvalidCredentialsException;
import com.paulormg.recommendex.shared.transfer.User;

/*
 * Ideally all DAOS should be interfaces instantianted and
 * fetched by dependency injection, but let's keep it simple
 * for now.
 */
public class UserDAO {

	// Singleton
	private static UserDAO instance = null;

	public static UserDAO getInstance(){
		if (instance == null){
			instance = new UserDAO();
		}
		
		return instance;
	}
	
	private int counter = 1;
	private HashMap<String, User> users;
	
	public UserDAO(){
		this.users = new HashMap<String, User>();
		this.users.put("admin", new User(0, "admin", true));
		this.counter = 1;
	}
	
	public User authenticateUser(String login, String password) throws InvalidCredentialsException {
		
		User user = this.users.get(login);
		
		if (user != null){
			// will authenticate user by comparing
			// password hash with password hash
			// stored on DB
		}
		
		return user;
	}
	
	public User createUser(String login, String password) throws ExistingUserException {
		if (users.containsKey(login)){
			throw new ExistingUserException();
		}
		
		User user = new User(counter++, login, false);
		users.put(login, user);
		
		return user;
	}
	
}
