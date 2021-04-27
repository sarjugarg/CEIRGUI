package com.gl.ceir.config.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gl.ceir.config.model.FileDumpMgmt;
import com.gl.ceir.config.model.ResponseCountAndQuantity;

import io.lettuce.core.dynamic.annotation.Param;

public interface FileDumpMgmtRepository extends JpaRepository<FileDumpMgmt, Long>, JpaSpecificationExecutor<FileDumpMgmt> {

	public List<FileDumpMgmt> getByServiceDump(String serviceDump);

	@Query(value="select new com.gl.ceir.config.model.ResponseCountAndQuantity(count(f.id) as count) from FileDumpMgmt f "
			+ "where f.serviceDump =:serviceDump")
	public ResponseCountAndQuantity getFileDumpCount( @Param("serviceDump") Integer serviceDump);

	public FileDumpMgmt findTopByDumpTypeOrderByIdDesc(String dumpType);
	
	public FileDumpMgmt save(FileDumpMgmt fileDumpMgmt);
}

