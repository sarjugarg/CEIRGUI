package com.gl.ceir.config.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.gl.ceir.config.model.SystemConfigListDb;

public interface SystemConfigListRepository extends CrudRepository<SystemConfigListDb, Long>, 
JpaRepository<SystemConfigListDb, Long>, JpaSpecificationExecutor<SystemConfigListDb>{
	
	public List<SystemConfigListDb> findByTag(String tag, Sort sort);
	
	public SystemConfigListDb findByTagAndInterpIgnoreCase(String tag, String interp);
	
	@Query("SELECT DISTINCT a.tag FROM SystemConfigListDb a")
	List<String> findDistinctTags();
	
	//@Query("SELECT NEW com.gl.ceir.config.model.SystemConfigListDb(a.tag, a.description, a.displayName) FROM SystemConfigListDb a group by a.tag, a.description, a.displayName")
	@Query("SELECT NEW com.gl.ceir.config.model.SystemConfigListDb(a.tag, a.displayName) FROM SystemConfigListDb a group by a.tag, a.displayName")
	List<SystemConfigListDb> findDistinctTagsWithDescription();
	
	public SystemConfigListDb getById(long id);
	
}
