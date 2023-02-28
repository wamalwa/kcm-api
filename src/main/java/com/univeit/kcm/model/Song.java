package com.univeit.kcm.model;

import java.util.Iterator;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "song")
public class Song {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="song_id")
	private int id;
	
	@Column(name="song_title")
	private String songTitle;
	
	@Column(name="lyrics")
	private String displayText;
	
	@Column(name = "pdf_location")
	private String pdfLocation;
	
	@Column(name = "midi_location")
	private String midiLocation;

	private String thumbnail;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "composer_id")
	private Composer composer;
	
	@Column(name = "uploader_id")
	private Integer uploaderId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "choir_id")
	private Choir choir;
	
	@Column(name = "youtube_id")
	private String youtubeId;
	
	@OneToMany(mappedBy = "song")
	@JsonManagedReference
	private Set<SongCategory> categories;
	
	/*@Column(name = "upload_dir")
	@Value("${file.upload-dir}")
	private String uploadDir;*/
	
	@Column(name = "song_status")
	private SongStatus songStatus;
	
	public Song() {}
	
	public Song(String songTitle, String displayText, String pdfLocation, String midiLocation, Composer composer, Integer uploaderId, Choir choir, String youtubeId, SongStatus songStatus) {
		this.songTitle = songTitle;
		this.displayText = displayText;
		this.pdfLocation = pdfLocation;
		this.midiLocation = midiLocation;
		this.composer = composer;
		this.uploaderId = uploaderId;
		this.choir = choir;
		this.youtubeId = youtubeId;
		this.songStatus = songStatus;
 	}
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSongTitle() {
		return songTitle;
	}

	public void setSongTitle(String songTitle) {
		this.songTitle = songTitle;
	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}
	
	public String getPdfLocation() {
		return pdfLocation;
	}
	
	public void setPdfLocation(String pdfLocation) {
		this.pdfLocation = pdfLocation;
	}
	
	public String getMidiLocation() {
		return midiLocation;
	}
	
	public void setMidiLocation(String midiLocation) {
		this.midiLocation = midiLocation;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Composer getComposer() {
		return composer;
	}
	
	public void setComposer(Composer composer) {
		this.composer = composer;
	}
	
	public Integer getUploaderId() {
		return uploaderId;
	}
	
	public void setUploaderId(Integer uploaderId) {
		this.uploaderId = uploaderId;
	}
	
	public Choir getChoir() {
		return choir;
	}
	
	public void setChoir(Choir choir) {
		this.choir = choir;
	}
	
	public String getYoutubeId() {
		return youtubeId;
	}
	
	public void setYoutubeId(String youtubeId) {
		this.youtubeId = youtubeId;
	}
	
	/*
	public String getUploadDir() {
		return pdfLocation;
	}
	
	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}*/
	
	public Set<SongCategory> getCategories() {
		return categories;
	}

	public void setCategories(Set<SongCategory> categories) {
		this.categories = categories;
	}
	
	
	
	public SongStatus getSongStatus() {
		return songStatus;
	}

	public void setSongStatus(SongStatus songStatus) {
		this.songStatus = songStatus;
	}

	public void addCategory(Category category) {
		SongCategory songCategory = new SongCategory(this, category);
		categories.add(songCategory);
		//category.getSongs().add(songCategory);
	}
	
	public void removeCategory(Category category) {
		for (Iterator<SongCategory> iterator = categories.iterator();
				iterator.hasNext();
				) {
			SongCategory songCategory = iterator.next();
			
			if (songCategory.getSong().equals(this) && 
					songCategory.getCategory().equals(category)) {
				iterator.remove();
				//songCategory.getCategory().getSongs().remove(songCategory);
				songCategory.setSong(null);
				songCategory.setCategory(null);
			}
		}
	}

	@Override
    public String toString() {
        return "Song {" +
                "id=" + id +
                ", song_title='" + songTitle + '\'' +
                ", display_text='" + displayText + '\'' +
                ", pdf_location='" + pdfLocation + '\'' +
                '}';
    }
	
	
}
