package com.gl.ceir.config.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.model.BlackList;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList, Long> , JpaSpecificationExecutor<BlackList> {
	
	@SuppressWarnings("unchecked")
	public BlackList save(BlackList blackList);
	
	public BlackList findByMsisdn(Long msisdn);

	@Query("SELECT r FROM BlackList r WHERE imei = :imei OR substr(imei,1,14) =:imei")
	public BlackList findByImei(String imei);
	
	@Query("SELECT r FROM BlackList r WHERE (imei = :imei OR substr(imei,1,14) =:imei) and LOWER(deviceIdType) =:deviceIdType")
	public BlackList findByImeiAndDeviceIdType(String imei, String deviceIdType);
	
	public Optional<BlackList> findById(Long id);
	 
}