package com.gl.ceir.config.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.model.DeviceDuplicateDb;

@Repository
public interface DeviceDuplicateDbRepository extends JpaRepository<DeviceDuplicateDb, Long>, 
JpaSpecificationExecutor<DeviceDuplicateDb> {

	public List<DeviceDuplicateDb> findByMsisdn(String msisdn);

	@Query("SELECT r FROM DeviceDuplicateDb r WHERE imei = :imei OR substr(imei,1,14) =:imei")
	public List<DeviceDuplicateDb> findByImei(String imei);
	
	public DeviceDuplicateDb findByImeiAndMsisdn(String imei, String msisdn);

}
