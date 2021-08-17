package com.univeit.kcm.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SongCategoryId implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "song_id")
	private Integer songId;
	
	@Column(name = "category_id")
	private Integer categoryId;

	public SongCategoryId() {}

	public SongCategoryId(Integer songId, Integer categoryId) {
		this.songId = songId;
		this.categoryId = categoryId;
	}

	public Integer getSongId() {
		return songId;
	}

	public void setSongId(Integer songId) {
		this.songId = songId;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(songId, categoryId);
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		
		if(obj == null || getClass() != obj.getClass())
			return false;
		
		SongCategoryId that = (SongCategoryId) obj;
		
		return Objects.equals(songId, that.songId) &&
				Objects.equals(categoryId, that.categoryId);
	}
	
	
}
