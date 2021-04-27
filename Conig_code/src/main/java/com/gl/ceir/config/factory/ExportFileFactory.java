package com.gl.ceir.config.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.ceir.config.factory.impl.ExportFileStockCeirAdmin;
import com.gl.ceir.config.factory.impl.ExportFileStockCustom;
import com.gl.ceir.config.factory.impl.ExportFileStockDistributor;
import com.gl.ceir.config.factory.impl.ExportFileStockImporter;
import com.gl.ceir.config.factory.impl.ExportFileStockManufacturer;
import com.gl.ceir.config.factory.impl.ExportFileStockRetailer;

@Component
public class ExportFileFactory {

	private static final Logger logger = LogManager.getLogger(ExportFileFactory.class);

	@Autowired
	ExportFileStockImporter exportFileStockImporter;
	
	@Autowired
	ExportFileStockDistributor exportFileStockDistributor;
	
	@Autowired
	ExportFileStockRetailer exportFileStockRetailer;
	
	@Autowired
	ExportFileStockCustom exportFileStockCustom;
	
	@Autowired
	ExportFileStockManufacturer exportFileStockManufacturer;
	
	@Autowired
	ExportFileStockCeirAdmin exportFileStockCeirAdmin;

	public ExportFile getObject(String userType, int featureId) {
		
		logger.info("Export Stock factory received usertype [" + userType + "] and feature id [" + featureId + "]");
		
		if(featureId == 4) {
			switch (userType) {
			case "IMPORTER":
				return exportFileStockImporter;
			case "DISTRIBUTOR":
				return exportFileStockDistributor;
			case "RETAILER":
				return exportFileStockRetailer;
			case "CUSTOM":
				return exportFileStockCustom;
			case "MANUFACTURER":
				return exportFileStockManufacturer;
			case "CEIRADMIN":
				return exportFileStockCeirAdmin;
				
			default:
				break;
			}
		}
		
		return null;
	}
}
