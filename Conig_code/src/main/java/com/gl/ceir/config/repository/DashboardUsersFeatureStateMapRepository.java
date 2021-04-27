package com.gl.ceir.config.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gl.ceir.config.model.DashboardUsersFeatureStateMap;

public interface DashboardUsersFeatureStateMapRepository extends JpaRepository< DashboardUsersFeatureStateMap, Long>{
	public List<DashboardUsersFeatureStateMap> findByUserTypeIdAndFeatureId( Integer userTypeId, Integer featureId );
}
