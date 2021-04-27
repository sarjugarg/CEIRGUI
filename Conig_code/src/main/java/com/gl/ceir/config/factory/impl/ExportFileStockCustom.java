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
import com.gl.ceir.config.model.file.StockCustomFileModel;
import com.gl.ceir.config.service.impl.AlertServiceImpl;
import com.gl.ceir.config.service.impl.StockServiceImpl;
import com.gl.ceir.config.util.CustomMappingStrategy;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

@Component
public class ExportFileStockCustom implements ExportFile{

	private static final Logger logger = LogManager.getLogger(ExportFileStockCustom.class);

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
		
		StockCustomFileModel stockCustomFileModel  = null;
		List<StockCustomFileModel> fileRecords = null;
		StatefulBeanToCsvBuilder<StockCustomFileModel> builder = null;
		StatefulBeanToCsv<StockCustomFileModel> csvWriter = null;
		CustomMappingStrategy<StockCustomFileModel> mappingStrategy = new CustomMappingStrategy<>();
		mappingStrategy.setType(StockCustomFileModel.class);

		try {
			fileName = LocalDateTime.now().format(dtf2).replace(" ", "_") + "_Custom_Stock.csv";
			writer = Files.newBufferedWriter(Paths.get(filePath+fileName));

			builder = new StatefulBeanToCsvBuilder<>(writer);
			csvWriter = builder.withMappingStrategy(mappingStrategy).withSeparator(',').withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();

			List<StockMgmt> stockMgmts = stockServiceImpl.getAll(filterRequest, source);

			if(stockMgmts.isEmpty()) {
				csvWriter.write(new StockCustomFileModel());
			}else {
				fileRecords = new ArrayList<>();

				for(StockMgmt stockMgmt : stockMgmts) {
					stockCustomFileModel = new StockCustomFileModel();
					
					stockCustomFileModel.setStockStatus(stockMgmt.getStateInterp());
					stockCustomFileModel.setTxnId( stockMgmt.getTxnId());
					stockCustomFileModel.setCreatedOn(stockMgmt.getCreatedOn().format(dtf));
					stockCustomFileModel.setModifiedOn( stockMgmt.getModifiedOn().format(dtf));
					stockCustomFileModel.setFileName( stockMgmt.getFileName());
					stockCustomFileModel.setQuantity(stockMgmt.getQuantity());
					stockCustomFileModel.setDeviceQuantity(stockMgmt.getDeviceQuantity());
					
					if(Objects.nonNull(stockMgmt.getSuplierName())) 
						stockCustomFileModel.setAssigneName(stockMgmt.getSuplierName());
					else 
						stockCustomFileModel.setAssigneName("NA");

					if(Objects.nonNull(stockMgmt.getInvoiceNumber())) 
						stockCustomFileModel.setInvoiceNumber(stockMgmt.getInvoiceNumber());
					else 
						stockCustomFileModel.setInvoiceNumber("NA");

					fileRecords.add(stockCustomFileModel);
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
