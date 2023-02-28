package com.univeit.kcm.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.univeit.kcm.exception.SongException;
import com.univeit.kcm.model.Category;
import com.univeit.kcm.model.Song;
import com.univeit.kcm.model.SongCategory;
import com.univeit.kcm.repo.SongCategoryRepository;
import com.univeit.kcm.repo.SongRepository;
@Service
public class SongService {
	@Autowired 
	SongRepository songRepository;
	
	@Autowired
	SongCategoryRepository songCategoryRepository;
	
	private final Path pdfStorageLocation;
	private final Path midiStorageLocation;
	private final Path defaultNullLocation;
	
	private static final String ALPHA_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	
	// private static final String NUMERIC_STRING = "0123456789";
	
	@Autowired
	public SongService() {
		this.pdfStorageLocation = Paths.get("/var/www/kcm-api/files/pdf/")
				.toAbsolutePath().normalize();
		this.midiStorageLocation = Paths.get("/var/www/kcm-api/files/midi/")
				.toAbsolutePath().normalize();
		this.defaultNullLocation = Paths.get("/var/www/kcm-api/files/default/")
				.toAbsolutePath().normalize();
		
		try {
			Files.createDirectories(this.pdfStorageLocation);
			Files.createDirectories(this.midiStorageLocation);
			Files.createDirectories(this.defaultNullLocation);
		} catch (Exception ex) {
			throw new SongException("Could not create the directory where the uploaded files will be stored.", ex);
		}
	}
	
	public void getPdf(String fileName, HttpServletResponse response) throws IOException {
		File file = new File(pdfStorageLocation + fileName);
		
		if(file.exists()) {
			response.setContentType("application/pdf");
			
			response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
			response.setContentLength((int) file.length());
			
			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

			FileCopyUtils.copy(inputStream, response.getOutputStream());
		}
	}
	
	@Transactional 
	public ResponseEntity<Object> createSong(Song song, String categories) {
		System.out.println("Composer--song-object::" + song.getComposer().getId());
		System.out.println("Choir--song-object::" + song.getChoir().getId());
		
		Song newSong = new Song();
		newSong = songRepository.save(song);
		
		if(!(categories == null || categories.isEmpty())) {
			String[] categoryIds = categories.split(",");
			
			for (String categoryId : categoryIds) {
				if(categoryId != null) {
					songCategoryRepository.save(new SongCategory(newSong, new Category(Integer.parseInt(categoryId),"")));
				}
			}
		}
		
		if(songRepository.findById(newSong.getId()).isPresent()) {
			return ResponseEntity.ok(newSong);
		}
			
		else return ResponseEntity.unprocessableEntity().body("Failed to add song " + newSong.getSongTitle() + ".");
	}
	
	public String uploadFile(MultipartFile file, String renameTo) {
		// Normalize file name
		String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
		
		String fileLocation = "";
		
		try {
			// Check if the file name contains invalid characters
			if(originalFileName.contains("..")) {
				throw new SongException("Sorry! Filename contains invalid path sequence " + originalFileName);
			}
			
			String fileExtension = "";
			
			try {
				fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
			} catch(Exception e) {
				fileExtension = "";
			}
			
			fileLocation = randomString(5) + "_" + renameTo.replace('-', '_')
					.replace(".", "_")
					.replace(" ", "_")
					.replace(",", "_") 
					+ fileExtension;
			
			// Copy file to target location (Replaces existing file with same name)
			
			Path targetLocation = 
					fileExtension.equalsIgnoreCase(".pdf") ? this.pdfStorageLocation.resolve(fileLocation) 
							: (fileExtension.equalsIgnoreCase(".mid") ? this.midiStorageLocation.resolve(fileLocation) 
									: this.defaultNullLocation.resolve(fileLocation));
					
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			System.out.println("TARGET_LOCATION::::::::::::::::::::::" + targetLocation);
			
			return fileLocation;
		} catch (IOException ex) {
            throw new SongException("Could not store file " + file.getOriginalFilename() + ". Please try again!", ex);
        }
	}
	
	private String randomString(int size) {
		StringBuilder builder = new StringBuilder();
		
		while(size-- != 0) {
			int character = (int)(Math.random()*ALPHA_STRING.length());
			builder.append(ALPHA_STRING.charAt(character));
		}
		return builder.toString();
	}
}
