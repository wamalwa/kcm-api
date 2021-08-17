package com.univeit.kcm.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sipios.springsearch.anotation.SearchSpec;
import com.univeit.kcm.api.CategoryResponse;
import com.univeit.kcm.api.TestSong;
import com.univeit.kcm.model.Category;
import com.univeit.kcm.model.Song;
import com.univeit.kcm.model.SongCategory;
import com.univeit.kcm.repo.CategoryRepository;
import com.univeit.kcm.repo.SongCategoryRepository;
import com.univeit.kcm.repo.SongRepository;

@RestController
@RequestMapping("/pub")
public class PublicController {
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	SongRepository songRepository;
	
	@Autowired
	SongCategoryRepository songCategoryRepository;
	
	
	@RequestMapping(value = "songs/search", method = RequestMethod.GET)
    public ResponseEntity<List<Song>> searchForSongs(@SearchSpec Specification<Song> specs) {
        return new ResponseEntity<>(songRepository.findAll(Specification.where(specs)), HttpStatus.OK);
    }
	
	@RequestMapping(value = "category", method = RequestMethod.GET)
	public ResponseEntity<?> allCategories(){
		List<Category> categories = categoryRepository.findAll();
		
		return ResponseEntity.ok(categories);
	}
	
	@RequestMapping(value = "category/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> categoryById(@PathVariable("id") Integer categoryId){
		Optional<Category> category = categoryRepository.findById(categoryId);
		
		return ResponseEntity.ok(category);
	}
	
	@RequestMapping(value = "category/{id}/songs", method = RequestMethod.GET)
	public ResponseEntity<?> songsByCategory(@PathVariable("id") Integer categoryId) {
		Category category = categoryRepository.getOne(categoryId);
		
		System.out.println("Category id::" + categoryId);
		
		List<SongCategory> songs = songCategoryRepository.findByCategoryId(category.getId());
		
		List<Song> catSongs = new ArrayList<>();
		
		for (SongCategory song : songs) {
			catSongs.add(songRepository.getOne(song.getSong().getId()));
		}
		
		CategoryResponse categoryResponse = new CategoryResponse(category.getId(), category.getCategoryName(), catSongs);
		
		return ResponseEntity.ok(categoryResponse);
	}
	
	@RequestMapping(value = "song", method = RequestMethod.GET)
	public ResponseEntity<?> getSongs(){
		List<Song> songs = songRepository.findAll();
		
	    return ResponseEntity.ok(songs);
	}
	
	@RequestMapping(value = "category/{id}/count", method = RequestMethod.GET)
	public ResponseEntity<?> countByCategory(@PathParam("id") Integer categoryId) {
		//Long songCount = songRepository.countByCategoryId(categoryId);
		return ResponseEntity.ok(0);
	}
	
	@RequestMapping(value = "test_song", method = RequestMethod.POST)
	public String addSongTest(@RequestBody TestSong song) {
		Song newSong = new Song();
		newSong.setSongTitle(song.getSongTitle());
		songRepository.save(newSong);
		return "Success";
	}
}
