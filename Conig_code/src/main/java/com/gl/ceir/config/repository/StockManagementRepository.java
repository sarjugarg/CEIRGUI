package com.gl.ceir.config.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gl.ceir.config.factory.CustomerCareRepo;
import com.gl.ceir.config.model.ResponseCountAndQuantity;
import com.gl.ceir.config.model.StockMgmt;

public interface StockManagementRepository extends JpaRepository<StockMgmt, Long>, 
JpaSpecificationExecutor<StockMgmt>, CustomerCareRepo<StockMgmt>{

	@SuppressWarnings("unchecked")
	public StockMgmt save(StockMgmt distributerManagement);
	
	public StockMgmt getByTxnId(String txnId);

	public List<StockMgmt> findByRoleTypeAndUserId(String moduleType, Long userId);

	public StockMgmt findByRoleTypeAndTxnId(String moduleType, String txnId);
	
	public StockMgmt findByUserTypeAndTxnId(String userType, String txnId);

	public void deleteByTxnId(String txnId);

	@Query(value="select new com.gl.ceir.config.model.ResponseCountAndQuantity(count(sm.id) as count, sum(sm.quantity) as quantity) from StockMgmt sm "
			+ "where sm.userId =:userId and sm.stockStatus in(:stockStatus)")
	public ResponseCountAndQuantity getStockCountAndQuantity( @Param("userId") long userId, @Param("stockStatus") List<Integer> stockStatus);
	
	@Query(value="select new com.gl.ceir.config.model.ResponseCountAndQuantity(count(sm.id) as count, sum(sm.quantity) as quantity) from StockMgmt sm "
			+ "where sm.stockStatus in(:stockStatus)")
	public ResponseCountAndQuantity getStockCountAndQuantity(@Param("stockStatus") List<Integer> stockStatus);
	
}
