package com.univeit.kcm.api;

import java.util.List;

import com.univeit.kcm.model.Song;

public class CategoryResponse {
	private int id;
	private String categoryName;
	
	private List<Song> songs;
	
	public CategoryResponse(int id, String categoryName, List<Song> songs) {
		this.id = id;
		this.categoryName = categoryName;
		this.songs = songs;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<Song> getSongs() {
		return songs;
	}

	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}
	
	
}
