package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.model.DeviceCustomDb;

@Repository
public interface DeviceCustomDbRepository extends JpaRepository<DeviceCustomDb, Long>, 
JpaSpecificationExecutor<DeviceCustomDb> {

	@Query("SELECT r FROM DeviceCustomDb r WHERE (imeiEsnMeid = :imei OR substr(imeiEsnMeid,1,14) =:imei)")
	public DeviceCustomDb getByImeiEsnMeid(String imei);
	
	@Query("SELECT r FROM DeviceCustomDb r WHERE (imeiEsnMeid = :imei OR substr(imeiEsnMeid,1,14) =:imei) and LOWER(deviceIdType) =:deviceIdType")
	public DeviceCustomDb getByImeiEsnMeidAndDeviceIdType( String imei, String deviceIdType);

}
