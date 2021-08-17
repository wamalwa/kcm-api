package com.univeit.kcm.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.univeit.kcm.model.Choir;

@Repository
public interface ChoirRepository extends JpaRepository<Choir, Integer> {

}
