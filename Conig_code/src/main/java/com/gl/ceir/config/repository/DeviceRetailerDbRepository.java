package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.model.DeviceRetailerDb;

@Repository
public interface DeviceRetailerDbRepository extends JpaRepository<DeviceRetailerDb, Long>, 
JpaSpecificationExecutor<DeviceRetailerDb> {

	@Query("SELECT r FROM DeviceRetailerDb r WHERE imeiEsnMeid =:imei OR substr(imeiEsnMeid,1,14) =:imei")
	public DeviceRetailerDb getByImeiEsnMeid(String imei);
	
	@Query("SELECT r FROM DeviceRetailerDb r WHERE (imeiEsnMeid =:imei OR substr(imeiEsnMeid,1,14) =:imei) and LOWER(deviceIdType) =:deviceIdType")
	public DeviceRetailerDb getByImeiEsnMeidAndDeviceIdType(String imei, String deviceIdType);

}
