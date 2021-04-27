package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.model.VisaDb;

@Repository
public interface VisaDbRepository extends JpaRepository<VisaDb, Long>{
	
	public VisaDb save(VisaDb visadb);
	
}
