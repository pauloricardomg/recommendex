package com.paulormg.recommendex.client.services.user;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.paulormg.recommendex.shared.transfer.UserData;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface UserServiceAsync {
		
	void login(String login, String password, AsyncCallback<UserData> callback);

	void logout(String login, AsyncCallback<Void> callback);

	void checkLoggedIn(AsyncCallback<UserData> callback);
	
}
