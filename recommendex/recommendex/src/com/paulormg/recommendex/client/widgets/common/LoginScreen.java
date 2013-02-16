package com.paulormg.recommendex.client.widgets.common;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.paulormg.recommendex.client.Recommendex;
import com.paulormg.recommendex.shared.util.FieldVerifier;

public class LoginScreen extends Composite {

	private final VerticalPanel mainPanel = new VerticalPanel();
	
	private final Label info = new Label("Please enter your credentials to log into the web store:");
	private final Grid grid = new Grid(2, 2);
	private final Button loginButton = new Button("Login or Create new user");
	private final TextBox loginField = new TextBox();
	private final PasswordTextBox passField = new PasswordTextBox();

	private Recommendex parent;
	
	public LoginScreen(Recommendex parent){
		this.parent = parent;
		initWidget(mainPanel);
		
		loginField.setWidth("150px");
		passField.setWidth("150px");
		
		//Init widgets
		grid.setText(0, 0, "login:");
		grid.setWidget(0, 1, loginField);

		grid.setText(1, 0, "password:");
		grid.setWidget(1, 1, passField);

		//some browsers do not support column formatter
		grid.getCellFormatter().addStyleName(0, 0, "formColumnStyle");
		grid.getCellFormatter().addStyleName(1, 0, "formColumnStyle");

		//Place widgets
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.add(info);
		mainPanel.add(grid);
		mainPanel.setSpacing(10);
		mainPanel.add(loginButton);		

		// Focus the cursor on the login field when the app loads
		loginField.setFocus(true);
		loginField.selectAll();

		// Set Click Handlers
		loginButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				login();
			}
		});
	}

	private void login() {
		String login = loginField.getText().trim();
		if (!FieldVerifier.isValidLogin(login)){
			Window.alert("Login must be longer than 3 characters.");
			return;
		}

		String password = passField.getText().trim();
		if (!FieldVerifier.isValidPassword(password)){
			Window.alert("Password must be longer than 3 characters.");
			return;
		}

		parent.login(login, password);
	}

}
