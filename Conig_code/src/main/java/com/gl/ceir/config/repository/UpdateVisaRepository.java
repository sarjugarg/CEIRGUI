package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.model.VisaUpdateDb;

@Repository
public interface UpdateVisaRepository extends JpaRepository<VisaUpdateDb, Long>,JpaSpecificationExecutor<VisaUpdateDb>{


	public VisaUpdateDb save(VisaUpdateDb visa);
	public VisaUpdateDb findByEndUserDBData_Id(long id);
	public VisaUpdateDb getById(long id);
	public VisaUpdateDb getByEndUserDBData_Id(long id);
	public VisaUpdateDb getByTxnId(String txnId);
}
