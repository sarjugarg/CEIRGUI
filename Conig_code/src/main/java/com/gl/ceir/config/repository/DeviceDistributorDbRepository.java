package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.model.DeviceDistributerDb;

@Repository
public interface DeviceDistributorDbRepository extends JpaRepository<DeviceDistributerDb, Long>, 
JpaSpecificationExecutor<DeviceDistributerDb> {

	@Query("SELECT r FROM DeviceDistributerDb r WHERE (imeiEsnMeid = :imei OR substr(imeiEsnMeid,1,14) =:imei)")
	public DeviceDistributerDb getByImeiEsnMeid(String imei);
	
	@Query("SELECT r FROM DeviceDistributerDb r WHERE (imeiEsnMeid = :imei OR substr(imeiEsnMeid,1,14) =:imei) and LOWER(deviceIdType) =:deviceIdType")
	public DeviceDistributerDb getByImeiEsnMeidAndDeviceIdType(String imei, String deviceIdType);

}
