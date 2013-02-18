package com.paulormg.recommendex.client.services.user;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.paulormg.recommendex.shared.exception.InvalidCredentialsException;
import com.paulormg.recommendex.shared.exception.ServerError;
import com.paulormg.recommendex.shared.transfer.UserData;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("login")
public interface UserService extends RemoteService {

	UserData checkLoggedIn() throws ServerError;

	UserData login(String login, String password) throws IllegalArgumentException, InvalidCredentialsException, ServerError;

	void logout(String login) throws InvalidCredentialsException;

}
