package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.model.DeviceUsageDb;

@Repository
public interface DeviceUsageDbRepository extends JpaRepository<DeviceUsageDb, Long>, 
JpaSpecificationExecutor<DeviceUsageDb> {
	
	@Query("SELECT r FROM DeviceUsageDb r WHERE imei = :imei OR substr(imei,1,14) =:imei")
	public DeviceUsageDb getByImei(String imei);
	
	public DeviceUsageDb getByMsisdn(String msisdn);
	
	public DeviceUsageDb getByImeiAndMsisdn(String imei, String msisdn);

}
