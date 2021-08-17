package com.univeit.kcm.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.univeit.kcm.model.SongCategory;

@Repository
public interface SongCategoryRepository extends JpaRepository<SongCategory, Integer> {
	List<SongCategory> findByCategoryId(int categoryId);
}
