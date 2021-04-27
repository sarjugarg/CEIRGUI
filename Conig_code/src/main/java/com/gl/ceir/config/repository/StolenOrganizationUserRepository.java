package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.model.StolenOrganizationUserDB;

@Repository
public interface StolenOrganizationUserRepository extends JpaRepository<StolenOrganizationUserDB, Long>,
JpaSpecificationExecutor<StolenOrganizationUserDB> {

	@Query(value = "SELECT max(id) FROM StolenOrganizationUserDB")
	public Long maxOfId();
}
