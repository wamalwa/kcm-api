package com.univeit.kcm.model;

import java.io.Serializable;

public class JwtRequest implements Serializable {
	private static final long serialVersionUID = 5926468583005150707L;
	
	private String username;
	private String password;
	
	//default constructor needed for JSON parsing
	public JwtRequest() {
		
	}
	
	public JwtRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
