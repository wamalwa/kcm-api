package com.univeit.kcm.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.univeit.kcm.model.Category;
import com.univeit.kcm.model.Choir;
import com.univeit.kcm.model.Composer;
import com.univeit.kcm.model.KcmUser;
import com.univeit.kcm.model.Song;
import com.univeit.kcm.model.SongStatus;
import com.univeit.kcm.repo.CategoryRepository;
import com.univeit.kcm.repo.ChoirRepository;
import com.univeit.kcm.repo.ComposerRepository;
import com.univeit.kcm.repo.SongRepository;
import com.univeit.kcm.repo.UserRepository;
import com.univeit.kcm.service.SongService;

@RestController
public class KcmController {

	private static final String EXTERNAL_FILE_PATH = "/var/www/kcm-api/files/pdf/";
	private static final String EXTERNAL_MIDI_PATH = "/var/www/kcm-api/files/midi/";
	
	private static final String DEFAULT_FILE_NAME = "kcm_filenotfound.pdf";
	
	@Autowired
	SongRepository songRepository;
	
	@Autowired
	ComposerRepository composerRepository;
	
	@Autowired
	ChoirRepository choirRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	SongService songService;
	
	
	// Song actions
	@RequestMapping(value = "song", method = RequestMethod.GET)
	public ResponseEntity<?> index(Authentication authentication){
		int userId = userRepository.findByUsername(authentication.getName()).getId();
		List<Song> songs = songRepository.findByUploaderId(userId);
		
	    return ResponseEntity.ok(songs);
	}
	
	/*Get Unapproved songs*/
	@RequestMapping(value = "song/status/{songStatus}", method = RequestMethod.GET)
	public ResponseEntity<?> unapprovedSongs(@PathVariable("songStatus") SongStatus songStatus){
		List<Song> songs = songRepository.findBySongStatus(songStatus);
		
		return ResponseEntity.ok(songs);
	}
	
	@RequestMapping(value = "song/pdf/{fileName:.+}", method = RequestMethod.GET)
	public void displayPdf(HttpServletRequest request, HttpServletResponse response, @PathVariable("fileName") String fileName) throws IOException {

		File file = new File(EXTERNAL_FILE_PATH + fileName);
		
		if(!file.exists()) {
			file = new File(EXTERNAL_FILE_PATH + DEFAULT_FILE_NAME);
		}
		
		response.setContentType("application/pdf");
		
		response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
		response.setContentLength((int) file.length());
		
		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

		FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping(value = "song/pdf/dld/{fileName:.+}", method = RequestMethod.GET)
	public void downloadPdf(HttpServletRequest request, HttpServletResponse response, @PathVariable("fileName") String fileName) throws IOException {

		File file = new File(EXTERNAL_FILE_PATH + fileName);
		
		if(!file.exists()) {
			file = new File(EXTERNAL_FILE_PATH + DEFAULT_FILE_NAME);
		}
		
		response.setContentType("application/pdf");
		
		response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));
		response.setContentLength((int) file.length());
		
		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

		FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping(value = "song/midi/{fileName:.+}", method = RequestMethod.GET)
	public void getMidi(HttpServletRequest request, HttpServletResponse response, @PathVariable("fileName") String fileName) throws IOException {
		File file = new File(EXTERNAL_MIDI_PATH + fileName);
		
		response.setContentType("audio/midi");
		
		response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
		response.setContentLength((int) file.length());
		
		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

		FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	@RequestMapping(value = "song/midi/dld/{fileName:.+}", method = RequestMethod.GET)
	public void downloadMidi(HttpServletRequest request, HttpServletResponse response, @PathVariable("fileName") String fileName) throws IOException {
		File file = new File(EXTERNAL_MIDI_PATH + fileName);
		
		response.setContentType("audio/midi");
		
		response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));
		response.setContentLength((int) file.length());
		
		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

		FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	/** Upload Song */
	@RequestMapping(value = "song", method = RequestMethod.POST)
	public ResponseEntity<?> uploadSong(
			Authentication authentication,
			@RequestParam("pdf") MultipartFile pdf,
			@RequestParam(value = "midi", required = false) MultipartFile midi,
			@RequestParam("song_title") String songTitle,
			@RequestParam("lyrics") String displayText,
			@RequestParam("composer_id") Integer composerId,
			@RequestParam("choir_id") Integer choirId,
			@RequestParam("youtube_id") String youtubeId,
			@RequestParam("categories") String categories){
		
		int userId = userRepository.findByUsername(authentication.getName()).getId();
		
		String pdfLocation = songService.uploadFile(pdf, songTitle);
		String midiLocation = songService.uploadFile(midi, songTitle);
		
		Composer composer = new Composer();
		Choir choir = new Choir();
		
		composer.setId(composerId);
		choir.setId(choirId);
		
		Song upload_song = new Song(songTitle, displayText, pdfLocation, midiLocation, composer, userId, choir, youtubeId, SongStatus.UPLOADED);
		
		//songRepository.save(upload_song);
		
		//return ResponseEntity.ok(upload_song);
		
		return songService.createSong(upload_song, categories);
	}
	
	// Count Uploader Songs
	@RequestMapping(value = "song/count/uploader")
	public ResponseEntity<?> getUploaderSongCount(Authentication authentication){
		int userId = userRepository.findByUsername(authentication.getName()).getId();
		Long songCount = songRepository.countByUploaderId(userId);
		
		return ResponseEntity.ok(songCount);
	}
	
	//Count By Status
	@RequestMapping(value = "song/count/status/{songStatus}")
	public ResponseEntity<?> getUploaderSongCount(@PathVariable("songStatus") SongStatus songStatus){
		Long songCount = songRepository.countBySongStatus(songStatus);
		
		return ResponseEntity.ok(songCount);
	}
	
	// Composer Actions
	@RequestMapping(value = "composer", method = RequestMethod.GET)
	public ResponseEntity<?> getComposers(){
		
		List<Composer> composers = composerRepository.findAll(Sort.by(Sort.Direction.ASC, "composerName"));
		return ResponseEntity.ok(composers);
	}
	
	@RequestMapping(value = "composer", method = RequestMethod.POST)
	public ResponseEntity<?> addComposer(Authentication authentication, @RequestBody Composer composer) {
		composer.setAddedBy(userRepository.findByUsername(authentication.getName()).getId());
		
		Composer newComposer = new Composer();
		newComposer = composerRepository.save(composer);
		
		return ResponseEntity.ok(newComposer);
	}
	
	
	// Choir Actions
	@RequestMapping(value = "choir", method = RequestMethod.GET)
	public ResponseEntity<?> getChoirs(){
		
		List<Choir> choirs = choirRepository.findAll(Sort.by(Sort.Direction.ASC, "choirName"));
		return ResponseEntity.ok(choirs);
	}
	
	@RequestMapping(value = "choir", method = RequestMethod.POST)
	public ResponseEntity<?> addChoir(@RequestBody Choir choir) {
		Choir newChoir = new Choir();
		newChoir = choirRepository.save(choir);
		return ResponseEntity.ok(newChoir);
	}
	
	
	// Category Actions
	@RequestMapping(value = "category", method = RequestMethod.GET)
	public ResponseEntity<?> getCategories(){
		
		List<Category> categories = categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "categoryName"));
		return ResponseEntity.ok(categories);
	}
	
	// Users
	// Get User
	@RequestMapping(value = "users", method = RequestMethod.GET)
	public ResponseEntity<?> getUsers() {
		List<KcmUser> users = userRepository.findAll();
		return ResponseEntity.ok(users);
	}
}
