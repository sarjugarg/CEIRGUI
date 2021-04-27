package com.gl.ceir.config.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gl.ceir.config.model.Rules;
import com.gl.ceir.config.model.SystemPolicyMapping;
import com.gl.ceir.config.model.constants.Period;

public interface SystemPolicyMappingRepository extends JpaRepository<SystemPolicyMapping, Long> {

	public SystemPolicyMapping findByRuleAndPeriodOrderByPriority(Rules rule, Period period);
	
	public List<SystemPolicyMapping> findByPeriodOrderByPriority(Period period);
}
