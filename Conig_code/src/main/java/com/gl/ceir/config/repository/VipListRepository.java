package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gl.ceir.config.model.ImeiMsisdnIdentity;
import com.gl.ceir.config.model.VipList;

public interface VipListRepository extends JpaRepository<VipList, ImeiMsisdnIdentity> {
	public VipList findByImeiMsisdnIdentityMsisdn(String msisdn);

	@Query("SELECT r FROM VipList r WHERE imeiMsisdnIdentity.imei = :imei OR substr(imeiMsisdnIdentity.imei,1,14) =:imei")
	public VipList findByImeiMsisdnIdentityImei(String imei);

}
