package com.gl.ceir.config.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gl.ceir.config.model.SystemConfigurationDb;

public interface SystemConfigurationDbRepository extends JpaRepository<SystemConfigurationDb, Long>, JpaSpecificationExecutor<SystemConfigurationDb> {


	public SystemConfigurationDb getByTag(String tag);

	public SystemConfigurationDb getById(Long id);

	public List<SystemConfigurationDb> getByType(Integer type);

}
