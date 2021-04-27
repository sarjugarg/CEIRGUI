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
import com.gl.ceir.config.model.file.StockImporterFileModel;
import com.gl.ceir.config.service.impl.AlertServiceImpl;
import com.gl.ceir.config.service.impl.StockServiceImpl;
import com.gl.ceir.config.util.CustomMappingStrategy;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

@Component
public class ExportFileStockImporter implements ExportFile{

	private static final Logger logger = LogManager.getLogger(ExportFileStockImporter.class);

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
		
		StockImporterFileModel stockImporterFileModel = null;
		List<StockImporterFileModel> fileRecords = null;
		StatefulBeanToCsvBuilder<StockImporterFileModel> builder = null;
		StatefulBeanToCsv<StockImporterFileModel> csvWriter = null;
		CustomMappingStrategy<StockImporterFileModel> mappingStrategy = new CustomMappingStrategy<>();
		mappingStrategy.setType(StockImporterFileModel.class);

		try {
			fileName = LocalDateTime.now().format(dtf2).replace(" ", "_") + "_Importer_Stock.csv";
			writer = Files.newBufferedWriter(Paths.get(filePath+fileName));

			builder = new StatefulBeanToCsvBuilder<>(writer);
			csvWriter = builder.withMappingStrategy(mappingStrategy).withSeparator(',').withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();

			List<StockMgmt> stockMgmts = stockServiceImpl.getAll(filterRequest, source);

			if(stockMgmts.isEmpty()) {
				csvWriter.write(new StockImporterFileModel());
			}else {
				fileRecords = new ArrayList<>();

				for(StockMgmt stockMgmt : stockMgmts) {
					stockImporterFileModel = new StockImporterFileModel();
					stockImporterFileModel.setStockStatus(stockMgmt.getStateInterp());
					stockImporterFileModel.setTxnId( stockMgmt.getTxnId());
					stockImporterFileModel.setCreatedOn(stockMgmt.getCreatedOn().format(dtf));
					stockImporterFileModel.setModifiedOn( stockMgmt.getModifiedOn().format(dtf));
					stockImporterFileModel.setFileName( stockMgmt.getFileName());
					stockImporterFileModel.setSupplierName(stockMgmt.getSuplierName());
					stockImporterFileModel.setQuantity(stockMgmt.getQuantity());
					stockImporterFileModel.setDeviceQuantity(stockMgmt.getDeviceQuantity());

					fileRecords.add(stockImporterFileModel);
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
