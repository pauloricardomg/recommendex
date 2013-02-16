package com.paulormg.recommendex.server.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.paulormg.recommendex.shared.exception.InvalidCredentialsException;
import com.paulormg.recommendex.shared.transfer.User;

public class ServerUtils {

	public static User authenticate(HttpServletRequest request, 
									String login) throws InvalidCredentialsException{
		
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidCredentialsException();
		
		Object obj = session.getAttribute("user");
		if (obj == null)
			throw new InvalidCredentialsException();
		
		User user = (User)obj;
		if(!user.getLogin().equals(login))
			throw new InvalidCredentialsException();
		
		return user;
	}
}
