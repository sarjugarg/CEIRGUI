package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.model.DeviceImporterDb;

@Repository
public interface DeviceImporterDbRepository extends JpaRepository<DeviceImporterDb, Long>, 
JpaSpecificationExecutor<DeviceImporterDb> {

	@Query("SELECT r FROM DeviceImporterDb r WHERE imeiEsnMeid =:imei OR substr(imeiEsnMeid,1,14) =:imei")
	public DeviceImporterDb getByImeiEsnMeid(String imei);
	
	@Query("SELECT r FROM DeviceImporterDb r WHERE (imeiEsnMeid =:imei OR substr(imeiEsnMeid,1,14) =:imei) and LOWER(deviceIdType) =:deviceIdType")
	public DeviceImporterDb getByImeiEsnMeidAndDeviceIdType(String imei, String deviceIdType);

}
