package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.model.DeviceEndUserDb;

@Repository
public interface DeviceEndUserDbRepository extends JpaRepository<DeviceEndUserDb, Long>, 
JpaSpecificationExecutor<DeviceEndUserDb> {

	@Query("SELECT r FROM DeviceEndUserDb r WHERE imeiEsnMeid = :imei OR substr(imeiEsnMeid,1,14) =:imei")
	public DeviceEndUserDb getByImeiEsnMeid(String imei);
	
	@Query("SELECT r FROM DeviceEndUserDb r WHERE (imeiEsnMeid = :imei OR substr(imeiEsnMeid,1,14) =:imei) and LOWER(deviceIdType) =:deviceIdType")
	public DeviceEndUserDb getByImeiEsnMeidAndDeviceIdType(String imei, String deviceIdType);

}
