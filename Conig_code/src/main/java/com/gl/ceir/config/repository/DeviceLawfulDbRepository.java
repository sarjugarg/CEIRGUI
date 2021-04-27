package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.factory.CustomerCareRepo;
import com.gl.ceir.config.model.DeviceLawfulDb;

@Repository
public interface DeviceLawfulDbRepository extends JpaRepository<DeviceLawfulDb, Long>, 
JpaSpecificationExecutor<DeviceLawfulDb>,
CustomerCareRepo<DeviceLawfulDb>{

	public DeviceLawfulDb getByImeiEsnMeid(String imei);
	
//	public DeviceLawfulDb getByImeiEsnMeidAndDeviceStatus(String imei, Integer deviceStatus);
	
	@Query("SELECT r FROM DeviceLawfulDb r WHERE (imeiEsnMeid =:imei OR substr(imeiEsnMeid,1,14) =:imei) and deviceStatus =:deviceStatus")
	public DeviceLawfulDb getByImeiEsnMeidAndDeviceStatus(String imei, Integer deviceStatus);
	
	@Query("SELECT r FROM DeviceLawfulDb r WHERE (imeiEsnMeid =:imei OR substr(imeiEsnMeid,1,14) =:imei) and deviceStatus =:deviceStatus"
			+ " and LOWER(deviceIdType) =:deviceIdType")
	public DeviceLawfulDb getByImeiEsnMeidAndDeviceStatusAndDeviceIdType(String imei, Integer deviceStatus, String deviceIdType);

}
