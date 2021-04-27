package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gl.ceir.config.model.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {

	public UserProfile getByUserId(long userid);

}
