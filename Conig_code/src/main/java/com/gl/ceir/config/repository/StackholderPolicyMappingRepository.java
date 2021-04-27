package com.gl.ceir.config.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.model.StackholderPolicyMapping;

@Repository
public interface StackholderPolicyMappingRepository extends JpaRepository<StackholderPolicyMapping, Long> {


	public StackholderPolicyMapping getByListType(String listType);

	public List<StackholderPolicyMapping> findByListTypeOrListType(String listType,String greyList);


}
