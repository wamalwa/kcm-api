package com.univeit.kcm.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity(name = "SongCategory")
@Table(name = "song_category")
public class SongCategory {
	@EmbeddedId
	private SongCategoryId id;
	
	@ManyToOne
	@MapsId("songId")
	@JoinColumn(name = "song_id")
	@JsonBackReference
	private Song song;
	
	@ManyToOne
	@MapsId("categoryId")
	@JoinColumn(name = "category_id")
	@JsonBackReference
	private Category category;
	
	@Column(name = "created_on")
	private Date createdOn = new Date();

	public SongCategory() {}

	public SongCategory(Song song, Category category) {
		this.song = song;
		this.category = category;
		this.id = new SongCategoryId(song.getId(), category.getId());
	}

	public SongCategoryId getId() {
		return id;
	}

	public void setId(SongCategoryId id) {
		this.id = id;
	}

	public Song getSong() {
		return song;
	}

	public void setSong(Song song) {
		this.song = song;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		
		if (obj == null || getClass() != obj.getClass())
			return false;
		
		SongCategory that = (SongCategory) obj;
		
		return Objects.equals(song,  that.song) &&
				Objects.equals(category, that.category);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(song, category);
	}
}
