package com.univeit.kcm.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.univeit.kcm.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	Category findById(int categoryId);
}
