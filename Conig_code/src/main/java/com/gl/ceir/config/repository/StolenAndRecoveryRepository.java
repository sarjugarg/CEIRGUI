package com.gl.ceir.config.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gl.ceir.config.factory.CustomerCareRepo;
import com.gl.ceir.config.model.ResponseCountAndQuantity;
import com.gl.ceir.config.model.StolenandRecoveryMgmt;

import io.lettuce.core.dynamic.annotation.Param;

public interface StolenAndRecoveryRepository extends JpaRepository<StolenandRecoveryMgmt, Long>,
JpaSpecificationExecutor<StolenandRecoveryMgmt>,  CustomerCareRepo<StolenandRecoveryMgmt> {

	@Override
	public StolenandRecoveryMgmt getByTxnId(String txnid);
	
	@SuppressWarnings("unchecked")
	public StolenandRecoveryMgmt save(StolenandRecoveryMgmt stolenandRecoveryDetails);

	public List<StolenandRecoveryMgmt> findByUserIdAndRoleType(Long userId,String roleType);

	public void deleteByTxnId(String txnId);
	
	public StolenandRecoveryMgmt getById(Long id);

	public void deleteById(Long id);
	
	
	@Query(value="select new com.gl.ceir.config.model.ResponseCountAndQuantity(count(srm.id) as count) from StolenandRecoveryMgmt srm "
			+ "where srm.userId =:userId and srm.fileStatus in(:fileStatus) and srm.requestType =:requestType")
	public ResponseCountAndQuantity getStolenandRecoveryCount( @Param("userId") long userId, @Param("fileStatus") List<Integer> fileStatus,
			@Param("requestType") Integer requestType);
	
	@Query(value="select new com.gl.ceir.config.model.ResponseCountAndQuantity(count(srm.id) as count) from StolenandRecoveryMgmt srm "
			+ "where srm.fileStatus in(:fileStatus) and srm.requestType =:requestType")
	public ResponseCountAndQuantity getStolenandRecoveryCount( @Param("fileStatus") List<Integer> fileStatus,
			@Param("requestType") Integer requestType);
}
