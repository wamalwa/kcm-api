package com.univeit.kcm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "composer")
public class Composer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "composer_id")
	private int id;
	
	@Column(name = "composer_name")
	private String composerName;
	
	@Column(name = "user_id")
	private Integer addedBy;
	
	public Composer() {}
	
	public Composer(String composerName) {
		this.composerName = composerName;
	}
	
	public Composer(String composerName, Integer addedBy) {
		this.composerName = composerName;
		this.addedBy = addedBy;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getComposerName() {
		return composerName;
	}

	public void setComposerName(String composerName) {
		this.composerName = composerName;
	}
	
	public Integer getAddedBy() {
		return addedBy;
	}
	
	public void setAddedBy(Integer addedBy) {
		this.addedBy = addedBy;
	}
	
}
