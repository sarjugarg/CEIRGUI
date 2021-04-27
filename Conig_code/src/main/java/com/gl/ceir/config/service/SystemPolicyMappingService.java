package com.gl.ceir.config.service;

import java.util.List;

import com.gl.ceir.config.model.Rules;
import com.gl.ceir.config.model.SystemPolicyMapping;
import com.gl.ceir.config.model.constants.Period;

public interface SystemPolicyMappingService extends RestServices<SystemPolicyMapping> {

	public SystemPolicyMapping getSystemPolicies(Rules rule, Period period);

	public List<SystemPolicyMapping> getSystemPoliciesByPeriod(Period period);
}
