package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.model.DeviceNullDb;

@Repository
public interface DeviceNullDbRepository extends JpaRepository<DeviceNullDb, Long>, 
JpaSpecificationExecutor<DeviceNullDb> {

	public DeviceNullDb findByMsisdn(String msisdn);

}
