package com.gl.ceir.config.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	
	public User getByUsername(String userName);
	public User getById(long id);
	public List<User> getByUsertype_Id(long id);
 
}
