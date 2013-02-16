package com.paulormg.recommendex.shared.util;

public class FieldVerifier {


	public static boolean isValidLogin(String login) {
		if (login == null) {
			return false;
		}
		return login.length() > 3;
	}
	
	public static boolean isValidPassword(String password) {
		if (password == null) {
			return false;
		}
		return password.length() > 3;
	}
}
