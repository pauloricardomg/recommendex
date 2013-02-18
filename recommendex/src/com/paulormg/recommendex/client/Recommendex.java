package com.paulormg.recommendex.client;

import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.paulormg.recommendex.client.services.user.UserService;
import com.paulormg.recommendex.client.services.user.UserServiceAsync;
import com.paulormg.recommendex.client.widgets.admin.AdminScreen;
import com.paulormg.recommendex.client.widgets.common.BaseScreen;
import com.paulormg.recommendex.client.widgets.common.LoginScreen;
import com.paulormg.recommendex.client.widgets.user.UserScreen;
import com.paulormg.recommendex.shared.exception.InvalidCredentialsException;
import com.paulormg.recommendex.shared.transfer.Item;
import com.paulormg.recommendex.shared.transfer.User;
import com.paulormg.recommendex.shared.transfer.UserData;

public class Recommendex implements EntryPoint {

	/* CONSTANTS */
	private static final String DEFAULT_ERROR_MSG = "Error while contacting server. Please try again.";

	/* SERVICES */
	private final UserServiceAsync userService = GWT.create(UserService.class);
	
	/* WIDGETS */
	private RootPanel container = null;
	private Label infoLabel = new Label();
	
	private BaseScreen mainScreen = null;
		
	/* SERVER DATA */
	private User currentUser = null;
	
	/* USER INTERFACE ACTIONS */
	
	public void onModuleLoad() {
		container = RootPanel.get("appContainer");
		RootPanel.get("errorLabelContainer").add(infoLabel);		

		checkLoggedIn();
	}

	public void resetScreen(){
		container.clear();
		clearInfoLabel();
	}
	
	private void showLoggedScreen(Map<Integer, Item> items) {		
		resetScreen();
		if (currentUser.isAdmin()){
			mainScreen = new AdminScreen(this, items);
		} else {
			mainScreen = new UserScreen(this, items);
		}
		container.add(getLoginPanel());
		container.add(mainScreen);
	}

	private void showLoginScreen() {
		resetScreen();
		container.add(new LoginScreen(this));
	}

	public void showInfoMessage(String message){
		infoLabel.setText(message);		
	}
	
	public void clearInfoLabel() {
		infoLabel.setText("");		
	}	
	
	private FlexTable getLoginPanel() {
		FlexTable loginPanel = new FlexTable();
		Anchor disconnectLink = new Anchor("(disconnect)","#");		
		
		loginPanel.setText(0, 1, "Logged in as: ");
		loginPanel.setText(0, 2, getLogin());
		loginPanel.setWidget(0, 3, disconnectLink);
		disconnectLink.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				logout();
			}
		});
		
		return loginPanel;
	}	
	
	/* CLIENT ACTIONS */
	
	public String getLogin() {
		return currentUser.getLogin();
	}

	public void setUser(User user){
		this.currentUser = user;
	}

	public void clearUser(){
		this.currentUser = null;
	}
	
	/* SERVER ACTIONS */
	
	public void login(String username, String password) {
		userService.login(username, password, getLoginCallback());
	}
	
	private void checkLoggedIn() {
		userService.checkLoggedIn(getCheckLoggedInCallback());
	}		
	
	public void logout() {
		userService.logout(currentUser.getLogin(), getLogoutCallback());
	}
	
	/* ASYNC CALLBACKS */

	private AsyncCallback<UserData> getCheckLoggedInCallback() {
		AsyncCallback<UserData> callback = new AsyncCallback<UserData>() {

			@Override
			public void onFailure(Throwable caught) {
				showInfoMessage(DEFAULT_ERROR_MSG);
			}

			@Override
			public void onSuccess(UserData result) {
				if (result != null){
					setUser(result.getUser());
					showLoggedScreen(result.getItems());
				} else {
					showLoginScreen();
				}
			}
		};
		return callback;
	}	
	
	private AsyncCallback<UserData> getLoginCallback() {
		return new AsyncCallback<UserData>() {
			@Override
			public void onFailure(Throwable caught) {
				if(caught.getClass().equals(IllegalArgumentException.class)){
					showInfoMessage("Username or password is invalid.");
				} else if (caught.getClass().equals(InvalidCredentialsException.class)){
					showInfoMessage("Invalid credentials.");
				} else {
					showInfoMessage("Error on server.");
				}
			}

			@Override
			public void onSuccess(UserData result) {
				Window.alert("Succesfully logged in as: " + result.getUser().getLogin());
				setUser(result.getUser());
				showLoggedScreen(result.getItems());
				if (result.isNewUser()){
					showInfoMessage("New user created: " + result.getUser().getLogin());
				}
			}
		};
	}	
	
	private AsyncCallback<Void> getLogoutCallback() {
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error while logging out.");
				clearUser();
				showLoginScreen();
			}

			@Override
			public void onSuccess(Void result) {
				Window.alert("Succesfully logged out.");
				clearUser();
				showLoginScreen();
			}
		};
		return callback;
	}
}
