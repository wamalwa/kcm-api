package com.univeit.kcm.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_token")
public class ConfirmationToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "token_id")
	private int tokenId;
	
	@Column(name = "confirmation_token")
	private String confirmationToken;
	
	@Column(name = "token_type")
	private int tokenType;
	
	@Column(name = "date_created")
	private Date dateCreated;
	
	@OneToOne(targetEntity = KcmUser.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user_id")
	private KcmUser kcmUser;
	
	@Column(name = "token_status")
	private int tokenStatus;
	
	public ConfirmationToken() {
		
	}
	
	public ConfirmationToken(KcmUser kcmUser) {
		this.kcmUser = kcmUser;
		dateCreated = new Date();
		confirmationToken = UUID.randomUUID().toString();
	}

	public int getTokenId() {
		return tokenId;
	}

	public void setTokenId(int tokenId) {
		this.tokenId = tokenId;
	}

	public String getConfirmationToken() {
		return confirmationToken;
	}

	public void setConfirmationToken(String confirmationToken) {
		this.confirmationToken = confirmationToken;
	}

	public int getTokenType() {
		return tokenType;
	}

	public void setTokenType(int tokenType) {
		this.tokenType = tokenType;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public KcmUser getKcmUser() {
		return kcmUser;
	}

	public void setKcmUser(KcmUser kcmUser) {
		this.kcmUser = kcmUser;
	}

	public int getTokenStatus() {
		return tokenStatus;
	}

	public void setTokenStatus(int tokenStatus) {
		this.tokenStatus = tokenStatus;
	}
	
	
	
}
