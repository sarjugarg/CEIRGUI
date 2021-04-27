package com.gl.ceir.config.repository; 

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.gl.ceir.config.model.StakeholderFeature;

@Repository
public interface FeatureRepo extends JpaRepository<StakeholderFeature, Long>,JpaSpecificationExecutor<StakeholderFeature>{
	
	public List<StakeholderFeature> findAll();

}
