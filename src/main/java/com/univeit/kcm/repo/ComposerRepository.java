package com.univeit.kcm.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.univeit.kcm.model.Composer;

@Repository
public interface ComposerRepository extends JpaRepository<Composer, Integer> {

}
