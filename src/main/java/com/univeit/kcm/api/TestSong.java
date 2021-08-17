package com.univeit.kcm.api;

import java.util.Set;

public class TestSong {
	private String songTitle;
	private String categories;
	private Set<Categ> categs;
	public String getSongTitle() {
		return songTitle;
	}
	public void setSongTitle(String songTitle) {
		this.songTitle = songTitle;
	}
	public String getCategories() {
		return categories;
	}
	public void setCategories(String categories) {
		this.categories = categories;
	}
	public Set<Categ> getCategs() {
		return categs;
	}
	public void setCategs(Set<Categ> categs) {
		this.categs = categs;
	}
	
	
	
}
