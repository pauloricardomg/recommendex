package com.paulormg.recommendex.server.dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import com.paulormg.recommendex.server.exception.DBException;
import com.paulormg.recommendex.server.util.DBUtils;
import com.paulormg.recommendex.shared.exception.ExistingUserException;
import com.paulormg.recommendex.shared.exception.InvalidCredentialsException;
import com.paulormg.recommendex.shared.transfer.User;

/*
 * Ideally all DAOS should be interfaces instantianted and
 * fetched by dependency injection, but let's keep it simple
 * for now.
 */
public class UserDAO {

	/* STATIC STUFF */	
	
	private static final String SQL_GET_USER = "SELECT * FROM User WHERE login=?";
	private static final String SQL_CREATE_USER = "INSERT INTO User VALUES (default, ?, MD5(?), FALSE)";
	
	static Logger logger = Logger.getLogger(UserDAO.class);
	
	// Singleton
	private static UserDAO instance = null;

	public static UserDAO getInstance(){
		if (instance == null){
			instance = new UserDAO();
		}
		
		return instance;
	}
	
	private UserDAO(){
		// Private constructor
	}	
	
	/* DAO METHODS */
	
	public User authenticateUser(String login, String password) throws InvalidCredentialsException, DBException {
		
		Connection conn = DBUtils.getConnection();
		
		ResultSet result = null;
		try {
			PreparedStatement stmt = conn.prepareStatement(SQL_GET_USER);
			stmt.setString(1, login);
			result = stmt.executeQuery();
			stmt.closeOnCompletion();
			if (!result.next()){
				return null; //user does not exist
			}
		} catch (SQLException e) {
			logger.error("Error while executing DB query to retrieve user: " + e.getMessage());
			throw new DBException();
		}
		
		return authenticate(result, password);
	}
	
	public User createUser(String login, String password) throws ExistingUserException, DBException {
		Connection conn = DBUtils.getConnection();
		
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(SQL_CREATE_USER, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, login);
			stmt.setString(2, password);
		} catch (SQLException e) {
			logger.error("Error while creating DB query to create user: " + e.getMessage());
			throw new DBException();
		}
		
		int userId = -1;
		
		try {
			stmt.executeUpdate();

			ResultSet generatedKeys = stmt.getGeneratedKeys();
			stmt.closeOnCompletion();
			
			if (!generatedKeys.next()){
				throw new ExistingUserException();				
			}
			
			userId = generatedKeys.getInt(1);
			generatedKeys.close();
		} catch (SQLException e) {
			logger.error("Error while executing DB query to create user: " + e.getMessage());
			throw new ExistingUserException();
		}
		
		logger.debug(String.format("User %s was created on DB with id %d.", login, userId));		
		
		User user = new User(userId, login, false);
		
		return user;
	}	
	
	/* HELPER METHODS */
	
	private User authenticate(ResultSet result, String password) throws InvalidCredentialsException, DBException {
		int id;
		String login;
		String passwordHash;
		boolean admin;
		try {
			id = result.getInt(1);
			login = result.getString(2);
			passwordHash = result.getString(3);
			admin = result.getBoolean(4);
			result.close();
		} catch(SQLException e){
			logger.error("Error while retrieving user data on DB: " + e.getMessage());
			throw new DBException();
		}
		
		if (!authenticate(password, passwordHash)){
			logger.warn(String.format("Invalid login attempt for user %s.", login));			
			throw new InvalidCredentialsException();
		}
		
		logger.debug(String.format("User %s has succesfully authenticated.", login));
		
		return new User(id, login, admin);
	}

	private boolean authenticate(String password, String passwordHash) {
		String md5Hex = DigestUtils.md5Hex(password);
		return passwordHash.equals(md5Hex);
	}
	
}
