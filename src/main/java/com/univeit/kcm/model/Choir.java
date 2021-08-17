package com.univeit.kcm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "choir")
public class Choir {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "choir_id")
	private int id;
	
	private String choirName;
	
	public Choir() {}

	public Choir(int id, String choirName) {
		this.id = id;
		this.choirName = choirName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getChoirName() {
		return choirName;
	}

	public void setChoirName(String choirName) {
		this.choirName = choirName;
	}
}
