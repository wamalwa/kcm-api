package com.univeit.kcm.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.univeit.kcm.model.KcmUser;

@Repository
public interface UserRepository extends JpaRepository<KcmUser, Integer> {
	KcmUser findByUsername(String username);
}
