package com.univeit.kcm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "user")
public class KcmUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private int id;
	
	@Column(name = "full_name")
	private String fullName;
	
	@Column
	private String username;
	
	@Column
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	
	@Column(name = "type_id")
	//@JsonIgnore
	private int role;
	
	@Column(name = "activation_code")
	@JsonIgnore
	private String activationCode;
	
	@Column(name = "accept_terms")
	private Integer acceptTerms;
	
	public KcmUser() {
		
	}
	
	public KcmUser(String fullName, String username, int role, String password, Integer acceptTerms) {
		this.fullName = fullName;
		this.username = username;
		this.role = role;
		this.password = password;
		this.acceptTerms = acceptTerms;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getActivationCode() {
		return this.activationCode;
	}
	
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	public Integer getAcceptTerms() {
		return acceptTerms;
	}

	public void setAcceptTerms(Integer acceptTerms) {
		this.acceptTerms = acceptTerms;
	}
	
}
