package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.model.RuleEngineMapping;

@Repository
public interface RuleEngineMappingRepository extends JpaRepository<RuleEngineMapping, Long>, 
JpaSpecificationExecutor<RuleEngineMapping> {

	public RuleEngineMapping getById(long id);

	public RuleEngineMapping getByName(String name);

	public void deleteById(long id);
	public RuleEngineMapping findByNameAndFeatureAndUserType(String name,String feature,String userType);
	
	public RuleEngineMapping findByNameAndFeatureAndUserTypeAndRuleOrder(String name,String feature,String userType,Integer ruleOrder);
	
}
