package com.gl.ceir.config.service.impl;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.ConfigTags;
import com.gl.ceir.config.EmailSender.EmailUtil;
import com.gl.ceir.config.configuration.PropertiesReader;
import com.gl.ceir.config.configuration.SortDirection;
import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.model.AuditTrail;
import com.gl.ceir.config.model.FileDetails;
import com.gl.ceir.config.model.FilterRequest;
import com.gl.ceir.config.model.SearchCriteria;
import com.gl.ceir.config.model.SystemConfigurationDb;
import com.gl.ceir.config.model.constants.Datatype;
import com.gl.ceir.config.model.constants.Features;
import com.gl.ceir.config.model.constants.SearchOperation;
import com.gl.ceir.config.model.constants.SubFeatures;
import com.gl.ceir.config.model.file.AuditTrailFileModel;
import com.gl.ceir.config.model.file.ConsignmentFileModel;
import com.gl.ceir.config.repository.AuditTrailRepository;
import com.gl.ceir.config.specificationsbuilder.GenericSpecificationBuilder;
import com.gl.ceir.config.util.CustomMappingStrategy;
import com.gl.ceir.config.util.InterpSetter;
import com.gl.ceir.config.util.Utility;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

@Service
public class AuditTrailServiceImpl {

	private static final Logger logger = LogManager.getLogger(AuditTrailServiceImpl.class);

	@Autowired
	AuditTrailRepository auditTrailRepository;

	@Autowired
	PropertiesReader propertiesReader;

	@Autowired
	Utility utility;

	@Autowired	
	EmailUtil emailUtil;

	@Autowired
	InterpSetter interpSetter;
	
	@Autowired
	ConfigurationManagementServiceImpl configurationManagementServiceImpl;

	public AuditTrail findById(long id){
		try {
			return auditTrailRepository.getById(id);
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public List<AuditTrail> getAll(FilterRequest filterRequest) {

		try {
			List<AuditTrail> auditTrails = auditTrailRepository.findAll( buildSpecification(filterRequest).build());

//			for(AuditTrail auditTrail : auditTrails ) {
//				setInterp(auditTrail);
//			}

			return auditTrails;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}

	}

	public Page<AuditTrail> filterAuditTrail(FilterRequest filterRequest, Integer pageNo, 
			Integer pageSize) {

		try {
			String orderColumn=null;
			orderColumn = "0".equalsIgnoreCase(filterRequest.getColumnName()) ? "createdOn"
					: "1".equalsIgnoreCase(filterRequest.getColumnName()) ? "txnId"
							: "2".equalsIgnoreCase(filterRequest.getColumnName()) ? "userId"
									: "3".equalsIgnoreCase(filterRequest.getColumnName()) ? "userType"
											: "4".equalsIgnoreCase(filterRequest.getColumnName())? "roleType"
													:"5".equalsIgnoreCase(filterRequest.getColumnName()) ? "featureName" 
													    : "6".equalsIgnoreCase(filterRequest.getColumnName()) ? "subFeature"
													    	: "7".equalsIgnoreCase(filterRequest.getColumnName()) ? "publicIp"
													    		: "8".equalsIgnoreCase(filterRequest.getColumnName()) ? "browser":"modifiedOn";
			
			Sort.Direction direction;
			if("modifiedOn".equalsIgnoreCase(orderColumn)) {
				direction=Sort.Direction.DESC;
			}
			else {
				direction= SortDirection.getSortDirection(filterRequest.getSort());
			}
			logger.info("final column name=="+orderColumn+" and sorting order== "+direction);
			Pageable pageable = PageRequest.of(pageNo, pageSize, new Sort(direction, orderColumn));
			
			Page<AuditTrail> page = auditTrailRepository.findAll( buildSpecification(filterRequest).build(), pageable );
			
			logger.info("response data "+page.getContent());
			
			for(AuditTrail auditTrail : page.getContent()) {
				setInterp(auditTrail);
			}

			return page;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}

	}

	public FileDetails getFilteredAuditTrailInFile(FilterRequest filterRequest) {
		String fileName = null;
		Writer writer   = null;
		AuditTrailFileModel atfm = null;
		
		DateTimeFormatter dtf  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter dtf2  = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

		SystemConfigurationDb filepath = configurationManagementServiceImpl.findByTag(ConfigTags.file_download_dir);
		logger.info("CONFIG : file_consignment_download_dir [" + filepath + "]");
		SystemConfigurationDb link = configurationManagementServiceImpl.findByTag(ConfigTags.file_download_link);
		logger.info("CONFIG : file_consignment_download_link [" + link + "]");
		
		String filePath = filepath.getValue();
		StatefulBeanToCsvBuilder<AuditTrailFileModel> builder = null;
		StatefulBeanToCsv<AuditTrailFileModel> csvWriter = null;
		List< AuditTrailFileModel > fileRecords = null;
		CustomMappingStrategy<AuditTrailFileModel> mappingStrategy = new CustomMappingStrategy<>();
		try {
			List<AuditTrail> auditTrails = getAll(filterRequest);
			if( !auditTrails.isEmpty() ) {
				if(Objects.nonNull(filterRequest.getUserId()) && (filterRequest.getUserId() != -1 && filterRequest.getUserId() != 0)) {
					fileName = LocalDateTime.now().format(dtf2).replace(" ", "_") + "_AuditTrails.csv";
				}else {
					fileName = LocalDateTime.now().format(dtf2).replace(" ", "_") + "_AuditTrails.csv";
				}
			}else {
				fileName = LocalDateTime.now().format(dtf2).replace(" ", "_") + "_AuditTrails.csv";
			}

			writer = Files.newBufferedWriter(Paths.get(filePath+fileName));
			mappingStrategy.setType(AuditTrailFileModel.class);
			builder = new StatefulBeanToCsvBuilder<>(writer);
			csvWriter = builder.withMappingStrategy(mappingStrategy).withSeparator(',').withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();

			if( !auditTrails.isEmpty() ) {
				fileRecords = new ArrayList<>(); 
				for(AuditTrail auditTrail : auditTrails ) {
					atfm = new AuditTrailFileModel();
					atfm.setCreatedOn(auditTrail.getCreatedOn().toString());
					atfm.setTxnId(auditTrail.getTxnId());
					atfm.setRoleType(auditTrail.getRoleType());
					atfm.setFeatureName(auditTrail.getFeatureName());
					atfm.setSubFeatureName(auditTrail.getSubFeature());
					atfm.setUserName(auditTrail.getUserName());
					atfm.setUserType(auditTrail.getUserType());
					atfm.setPublicIP(auditTrail.getPublicIp());
					atfm.setBrowser(auditTrail.getBrowser());
					logger.debug(atfm);

					fileRecords.add(atfm);
				}

				csvWriter.write(fileRecords);
			}
			else
			{
				csvWriter.write( new AuditTrailFileModel());	
			}
			
			return new FileDetails( fileName, filePath, link.getValue().replace("$LOCAL_IP",
					propertiesReader.localIp)  + fileName ); 

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}finally {
			try {
				if( Objects.nonNull(writer) )
					writer.close();
			} catch (IOException e) {}
		}
	}

	private GenericSpecificationBuilder<AuditTrail> buildSpecification(FilterRequest filterRequest){
		GenericSpecificationBuilder<AuditTrail> cmsb = new GenericSpecificationBuilder<>(propertiesReader.dialect);

		if (!"SystemAdmin".equalsIgnoreCase(filterRequest.getUserType())) {
			if(Objects.nonNull(filterRequest.getUserId()))
				cmsb.with(new SearchCriteria("userId", filterRequest.getUserId(), SearchOperation.EQUALITY, Datatype.STRING));
		}
		
		if(Objects.nonNull(filterRequest.getStartDate()) && !filterRequest.getStartDate().isEmpty())
			cmsb.with(new SearchCriteria("createdOn", filterRequest.getStartDate() , SearchOperation.GREATER_THAN, Datatype.DATE));

		if(Objects.nonNull(filterRequest.getEndDate()) && !filterRequest.getEndDate().isEmpty())
			cmsb.with(new SearchCriteria("createdOn", filterRequest.getEndDate() , SearchOperation.LESS_THAN, Datatype.DATE));

		if(Objects.nonNull(filterRequest.getTxnId()) && !filterRequest.getTxnId().isEmpty())
			cmsb.with(new SearchCriteria("txnId", filterRequest.getTxnId(), SearchOperation.LIKE, Datatype.STRING));
		
		if(Objects.nonNull(filterRequest.getFeatureName()) && !filterRequest.getFeatureName().isEmpty())
			cmsb.with(new SearchCriteria("featureName", filterRequest.getFeatureName(), SearchOperation.EQUALITY, Datatype.STRING));

		if(Objects.nonNull(filterRequest.getUserType()) && !filterRequest.getUserType().isEmpty())
			cmsb.with(new SearchCriteria("userType", filterRequest.getUserType(), SearchOperation.EQUALITY, Datatype.STRING));

		if(Objects.nonNull(filterRequest.getSubFeatureName()) && !filterRequest.getSubFeatureName().isEmpty())
			cmsb.with(new SearchCriteria("subFeature", filterRequest.getSubFeatureName(), SearchOperation.LIKE, Datatype.STRING));

		if(Objects.nonNull(filterRequest.getUserName()) && !filterRequest.getUserName().isEmpty())
			cmsb.with(new SearchCriteria("userName", filterRequest.getUserName(), SearchOperation.LIKE, Datatype.STRING));
		
		if(Objects.nonNull(filterRequest.getRoleType()) && !filterRequest.getRoleType().isEmpty())
			cmsb.with(new SearchCriteria("roleType", filterRequest.getRoleType(), SearchOperation.EQUALITY, Datatype.STRING));

		
		if(Objects.nonNull(filterRequest.getPublicIp()) && !filterRequest.getPublicIp().isEmpty())
			cmsb.with(new SearchCriteria("publicIp", filterRequest.getPublicIp(), SearchOperation.LIKE, Datatype.STRING));
		
		if(Objects.nonNull(filterRequest.getBrowser()) && !filterRequest.getBrowser().isEmpty())
			cmsb.with(new SearchCriteria("browser", filterRequest.getBrowser(), SearchOperation.LIKE, Datatype.STRING));
		
		if(Objects.nonNull(filterRequest.getSearchString()) && !filterRequest.getSearchString().isEmpty()){
			cmsb.orSearch(new SearchCriteria("txnId", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
			cmsb.orSearch(new SearchCriteria("userName", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
			cmsb.orSearch(new SearchCriteria("featureName", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
			cmsb.orSearch(new SearchCriteria("subFeature", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
			cmsb.orSearch(new SearchCriteria("userType", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
			cmsb.orSearch(new SearchCriteria("roleType", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
		}
		return cmsb;
	}

	private void setInterp(AuditTrail auditTrail) {
		/*if(Objects.nonNull(consignmentMgmt.getExpectedArrivalPort()))
			consignmentMgmt.setExpectedArrivalPortInterp(interpSetter.setConfigInterp(Tags.CUSTOMS_PORT, consignmentMgmt.getExpectedArrivalPort()));
		 */
	}

}
