package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.factory.CustomerCareRepo;
import com.gl.ceir.config.model.DeviceLawfulDb;
import com.gl.ceir.config.model.DeviceOperatorDb;

@Repository
public interface DeviceOperatorDbRepository extends JpaRepository<DeviceLawfulDb, Long>, 
JpaSpecificationExecutor<DeviceLawfulDb>,
CustomerCareRepo<DeviceOperatorDb>{

	public DeviceOperatorDb getByImeiEsnMeid(String imei);
	
	@Query("SELECT r FROM DeviceOperatorDb r WHERE (imeiEsnMeid =:imei OR substr(imeiEsnMeid,1,14) =:imei) and deviceStatus =:deviceStatus")
	public DeviceOperatorDb getByImeiEsnMeidAndDeviceStatus(String imei, Integer deviceStatus);
	
	@Query("SELECT r FROM DeviceOperatorDb r WHERE (imeiEsnMeid =:imei OR substr(imeiEsnMeid,1,14) =:imei) and deviceStatus =:deviceStatus"
			+ " and LOWER(deviceIdType) =:deviceIdType")
	public DeviceOperatorDb getByImeiEsnMeidAndDeviceStatusAndDeviceIdType(String imei, Integer deviceStatus, String deviceIdType);

}
