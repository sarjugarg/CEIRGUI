package com.gl.ceir.config.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gl.ceir.config.factory.CustomerCareRepo;
import com.gl.ceir.config.model.ResponseCountAndQuantity;
import com.gl.ceir.config.model.TypeApprovedDb;


public interface TypeApproveRepository extends JpaRepository<TypeApprovedDb, Long>, JpaSpecificationExecutor<TypeApprovedDb>{

	public TypeApprovedDb save(TypeApprovedDb typeApprovedDb);
	public TypeApprovedDb findById(long id);
	public TypeApprovedDb getByTxnId(String txnId);
	
	public TypeApprovedDb getByTac(String tac);
	
	@Query(value="select new com.gl.ceir.config.model.ResponseCountAndQuantity(count(t.id) as count) from TypeApprovedDb t "
			+ "where t.approveStatus in (:approveStatus) and t.userId =:userId and t.featureId =:featureId")
	public ResponseCountAndQuantity getTypeApproveCount( @Param("approveStatus")List<Integer> approveStatus, @Param("userId")Long userId,
			@Param("featureId")long featureId);
	
	@Query(value="select new com.gl.ceir.config.model.ResponseCountAndQuantity(count(t.id) as count) from TypeApprovedDb t "
			+ "where t.adminApproveStatus IS NULL and t.featureId =:featureId")
	public ResponseCountAndQuantity getAdminTypeApproveCount( @Param("featureId")long featureId);
	
	@Query(value="select new com.gl.ceir.config.model.ResponseCountAndQuantity(count(t.id) as count) from TypeApprovedDb t "
			+ "where t.approveStatus in (:approveStatus) and t.featureId =:featureId")
	public ResponseCountAndQuantity getAdminTypeApproveCount( @Param("approveStatus")List<Integer> approveStatus, @Param("featureId")long featureId);
}
