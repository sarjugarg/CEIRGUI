package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.model.DeviceManufacturerDb;

@Repository
public interface DeviceManufacturerDbRepository extends JpaRepository<DeviceManufacturerDb, Long>, 
JpaSpecificationExecutor<DeviceManufacturerDb> {

	@Query("SELECT r FROM DeviceManufacturerDb r WHERE imeiEsnMeid =:imei OR substr(imeiEsnMeid,1,14) =:imei")
	public DeviceManufacturerDb findByImeiEsnMeid(String imei);
	
	@Query("SELECT r FROM DeviceManufacturerDb r WHERE (imeiEsnMeid =:imei OR substr(imeiEsnMeid,1,14) =:imei) and  LOWER(deviceIdType) =:deviceIdType")
	public DeviceManufacturerDb findByImeiEsnMeidAndDeviceIdType(String imei, String deviceIdType);

}
