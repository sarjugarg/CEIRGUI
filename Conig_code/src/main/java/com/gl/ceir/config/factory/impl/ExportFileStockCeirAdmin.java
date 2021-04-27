package com.gl.ceir.config.factory.impl;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.ceir.config.configuration.PropertiesReader;
import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.factory.ExportFile;
import com.gl.ceir.config.model.FileDetails;
import com.gl.ceir.config.model.FilterRequest;
import com.gl.ceir.config.model.StockMgmt;
import com.gl.ceir.config.model.SystemConfigurationDb;
import com.gl.ceir.config.model.constants.Alerts;
import com.gl.ceir.config.model.constants.SubFeatures;
import com.gl.ceir.config.model.file.StockCeirAdminFileModel;
import com.gl.ceir.config.service.impl.AlertServiceImpl;
import com.gl.ceir.config.service.impl.StockServiceImpl;
import com.gl.ceir.config.util.CustomMappingStrategy;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

@Component
public class ExportFileStockCeirAdmin implements ExportFile{

	private static final Logger logger = LogManager.getLogger(ExportFileStockCeirAdmin.class);
	
	@Autowired
	StockServiceImpl stockServiceImpl;

	@Autowired
	AlertServiceImpl alertServiceImpl;
	
	@Autowired
	PropertiesReader propertiesReader;

    @Override
	public FileDetails export(FilterRequest filterRequest, String source, DateTimeFormatter dtf, DateTimeFormatter dtf2,
			String filePath, SystemConfigurationDb link) {
		Writer writer   = null;
		String fileName = null;
		
		StockCeirAdminFileModel stockCeirAdminFileModel  = null;
		List<StockCeirAdminFileModel> fileRecords = null;
		StatefulBeanToCsvBuilder<StockCeirAdminFileModel> builder = null;
		StatefulBeanToCsv<StockCeirAdminFileModel> csvWriter = null;
		CustomMappingStrategy<StockCeirAdminFileModel> mappingStrategy = new CustomMappingStrategy<>();
		mappingStrategy.setType(StockCeirAdminFileModel.class);

		try {
			fileName = LocalDateTime.now().format(dtf2).replace(" ", "_") + "_CeirAdmin_Stock.csv";
			writer = Files.newBufferedWriter(Paths.get(filePath+fileName));

			builder = new StatefulBeanToCsvBuilder<>(writer);
			csvWriter = builder.withMappingStrategy(mappingStrategy).withSeparator(',').withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();

			logger.info("Request to export filtered Stocks = " + filterRequest);
			List<StockMgmt> stockMgmts = stockServiceImpl.getAll(filterRequest, source);

			if(stockMgmts.isEmpty()) {
				csvWriter.write(new StockCeirAdminFileModel());
			}else {
				fileRecords = new ArrayList<>();

				for(StockMgmt stockMgmt : stockMgmts) {
					String displayName = null;
					if(Objects.nonNull(stockMgmt.getUser())) {
						displayName = stockMgmt.getUser().getUserProfile().getDisplayName();
					}else {
						displayName = "";
					}
					stockCeirAdminFileModel = new StockCeirAdminFileModel();
					stockCeirAdminFileModel.setStockStatus(stockMgmt.getStateInterp());
					stockCeirAdminFileModel.setTxnId( stockMgmt.getTxnId());
					stockCeirAdminFileModel.setCreatedOn(stockMgmt.getCreatedOn().format(dtf));
					stockCeirAdminFileModel.setModifiedOn( stockMgmt.getModifiedOn().format(dtf));
					stockCeirAdminFileModel.setFileName( stockMgmt.getFileName());
					stockCeirAdminFileModel.setSupplierName(stockMgmt.getSuplierName());
					stockCeirAdminFileModel.setQuantity(stockMgmt.getQuantity());
					stockCeirAdminFileModel.setDeviceQuantity(stockMgmt.getDeviceQuantity());
					stockCeirAdminFileModel.setRoleType(stockMgmt.getUserType());
					stockCeirAdminFileModel.setName(displayName);

					fileRecords.add(stockCeirAdminFileModel);
				}

				csvWriter.write(fileRecords);
			}

			stockServiceImpl.addInAuditTrail(Long.valueOf(filterRequest.getUserId()), "NA", SubFeatures.EXPORT,filterRequest.getRoleType(),filterRequest.getPublicIp(),filterRequest.getBrowser());

			return new FileDetails( fileName, filePath, link.getValue().replace("$LOCAL_IP", propertiesReader.localIp) + fileName ); 
		}catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", "Stock Management");
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.EXPORT);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, filterRequest.getUserId(), bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}finally {
			try {

				if( Objects.nonNull(writer) )
					writer.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
