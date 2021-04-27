package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.model.GsmaBlackList;

@Repository
public interface GsmaBlacklistRepository extends JpaRepository<GsmaBlackList, Long>, 
JpaSpecificationExecutor<GsmaBlackList> {

	public GsmaBlackList getByDeviceid(String imei);

}
