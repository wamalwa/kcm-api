package com.univeit.kcm.repo;

import java.util.List;

//import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.univeit.kcm.model.Song;
import com.univeit.kcm.model.SongStatus;

@Repository
@RepositoryRestResource
public interface SongRepository extends JpaRepository<Song, Integer>, JpaSpecificationExecutor<Song> {
	List<Song> findByUploaderId(int uploaderId);
	
	@Override
	public List<Song> findAll();
	
	List<Song> findBySongStatus(SongStatus songStatus);
	
	//public List<Song> findByCategory(Integer categoryId);
	
	@Query("SELECT COUNT(s) FROM Song s WHERE s.uploaderId=?1")
    Long countByUploaderId(Integer uploaderId);

	@Query("SELECT COUNT(s) FROM Song s WHERE s.songStatus=?1")
	Long countBySongStatus(SongStatus songStatus);
	
	//@Query("SELECT COUNT(s) FROM Song s WHERE s.categoryId=?1")
	//Long countByCategoryId(Integer categoryId);
}
