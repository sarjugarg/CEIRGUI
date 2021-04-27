package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.model.UserProfile;

@Repository
public interface UserProfileRepo extends JpaRepository<UserProfile, Long>, JpaSpecificationExecutor<UserProfile>{
	
	public UserProfile save(UserProfile userprofile); 
	public UserProfile findById(long id); 
	public UserProfile findByUser_Id(long id);                            

}