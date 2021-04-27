package com.gl.ceir.config.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.model.Rules;
import com.gl.ceir.config.model.SystemPolicyMapping;
import com.gl.ceir.config.model.constants.Period;
import com.gl.ceir.config.repository.SystemPolicyMappingRepository;
import com.gl.ceir.config.service.SystemPolicyMappingService;

@Service
public class SystemPolicyMappingServiceImpl implements SystemPolicyMappingService {

	@Autowired
	private SystemPolicyMappingRepository systemPolicyMappingRepository;

	@Override
	public List<SystemPolicyMapping> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SystemPolicyMapping save(SystemPolicyMapping t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SystemPolicyMapping get(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SystemPolicyMapping update(SystemPolicyMapping t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Long t) {
		// TODO Auto-generated method stub

	}

	@Override
	public SystemPolicyMapping getSystemPolicies(Rules rule, Period period) {
		return systemPolicyMappingRepository.findByRuleAndPeriodOrderByPriority(rule, period);
	}

	@Override
	public List<SystemPolicyMapping> getSystemPoliciesByPeriod(Period period) {
		return systemPolicyMappingRepository.findByPeriodOrderByPriority(period);
	}

	@Override
	public SystemPolicyMapping get(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
