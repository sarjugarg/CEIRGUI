package com.gl.ceir.config.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.factory.CustomerCareRepo;
import com.gl.ceir.config.model.ConsignmentMgmt;
import com.gl.ceir.config.model.ResponseCountAndQuantity;
import com.gl.ceir.config.model.Usertype;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface ConsignmentRepository extends JpaRepository<ConsignmentMgmt, Long>, 
JpaSpecificationExecutor<ConsignmentMgmt>, CustomerCareRepo<ConsignmentMgmt>{

	@SuppressWarnings("unchecked")
	public ConsignmentMgmt save(ConsignmentMgmt consignment);

	public ConsignmentMgmt getByConsignmentNumber(String consignmEntNumber);

	public List<ConsignmentMgmt> getByUserIdOrderByIdDesc(Long userId);

	public ConsignmentMgmt getByTxnId(String txnId);

	public List<ConsignmentMgmt> findByUser_id(int id);

	@Query(value="select new com.gl.ceir.config.model.ResponseCountAndQuantity(count(c.id) as count, sum(c.quantity) as quantity) from ConsignmentMgmt c "
			+ "where c.userId =:userId and c.consignmentStatus in (:consignmentStatus)")
	public ResponseCountAndQuantity getConsignmentCountAndQuantity( @Param("userId") Integer userId, @Param("consignmentStatus") List< Integer > consignmentStatus);
	
	@Query(value="select new com.gl.ceir.config.model.ResponseCountAndQuantity(count(c.id) as count, sum(c.quantity) as quantity) from ConsignmentMgmt c "
			+ "where c.consignmentStatus in (:consignmentStatus)")
	public ResponseCountAndQuantity getConsignmentCountAndQuantity( @Param("consignmentStatus") List< Integer > consignmentStatus);

	public Usertype getById(Integer userId);
	
	//public ConsignmentMgmt updateStatusBeforeDelete(ConsignmentMgmt consignmentMgmt);
}
