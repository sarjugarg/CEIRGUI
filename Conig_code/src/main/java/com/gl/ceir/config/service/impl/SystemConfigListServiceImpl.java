package com.gl.ceir.config.service.impl;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.ConfigTags;
import com.gl.ceir.config.configuration.PropertiesReader;
import com.gl.ceir.config.configuration.SortDirection;
import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.model.AuditTrail;
import com.gl.ceir.config.model.FileDetails;
import com.gl.ceir.config.model.FilterRequest;
import com.gl.ceir.config.model.GenricResponse;
import com.gl.ceir.config.model.RuleEngine;
import com.gl.ceir.config.model.SearchCriteria;
import com.gl.ceir.config.model.SystemConfigListDb;
import com.gl.ceir.config.model.SystemConfigurationDb;
import com.gl.ceir.config.model.User;
import com.gl.ceir.config.model.constants.Datatype;
import com.gl.ceir.config.model.constants.Features;
import com.gl.ceir.config.model.constants.GenericMessageTags;
import com.gl.ceir.config.model.constants.SearchOperation;
import com.gl.ceir.config.model.constants.SubFeatures;
import com.gl.ceir.config.model.constants.Usertype;
import com.gl.ceir.config.model.file.SystemConfigListFileModel;
import com.gl.ceir.config.model.file.SystemConfigListFileModel;
import com.gl.ceir.config.repository.AuditTrailRepository;
import com.gl.ceir.config.repository.SystemConfigListRepository;
import com.gl.ceir.config.repository.SystemConfigurationDbRepository;
import com.gl.ceir.config.repository.UserRepository;
import com.gl.ceir.config.specificationsbuilder.GenericSpecificationBuilder;
import com.gl.ceir.config.util.CustomMappingStrategy;
import com.gl.ceir.config.util.InterpSetter;
import com.gl.ceir.config.util.Utility;
import com.opencsv.CSVWriter;
import com.opencsv.bean.MappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

@Service
public class SystemConfigListServiceImpl {

	private static final Logger logger = LogManager.getLogger(SystemConfigListServiceImpl.class);

	@Autowired
	AuditTrailRepository auditTrailRepository;

	@Autowired
	PropertiesReader propertiesReader;

	@Autowired
	Utility utility;

	@Autowired
	InterpSetter interpSetter;

	@Autowired
	ConfigurationManagementServiceImpl configurationManagementServiceImpl;

	@Autowired
	SystemConfigListRepository systemConfigListRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	SystemConfigurationDbRepository systemConfigurationDbRepository;

	public GenricResponse saveSystemConfigList(SystemConfigListDb systemConfigListDb) {
		try {
			if (Objects.isNull(systemConfigListDb.getTag())) {
				return new GenricResponse(1, GenericMessageTags.NULL_REQ.getTag(),
						GenericMessageTags.NULL_REQ.getMessage(), null);
			}

			systemConfigListDb.setListOrder(0);
			systemConfigListRepository.save(systemConfigListDb);
			auditTrailRepository.save(new AuditTrail(systemConfigListDb.getUserId(), systemConfigListDb.getUsername(), 0L,"SystemAdmin", 0L, 
					Features.FIELD_MANGEMENT, SubFeatures.Save, "","NA","SystemAdmin",systemConfigListDb.getPublicIp(),systemConfigListDb.getBrowser()));
			
			return new GenricResponse(0);

		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public GenricResponse updateSystemConfigList(SystemConfigListDb systemConfigListDb) {
		try {
			if (Objects.isNull(systemConfigListDb.getId())) {
				return new GenricResponse(1, GenericMessageTags.NULL_REQ.getTag(),
						GenericMessageTags.NULL_REQ.getMessage(), null);
			}
			SystemConfigListDb systemConfigListDb2 = systemConfigListRepository.getById(systemConfigListDb.getId());
			systemConfigListDb2.setDescription(systemConfigListDb.getDescription());
			systemConfigListDb2.setTagId(systemConfigListDb.getTagId());
			systemConfigListDb2.setInterp(systemConfigListDb.getInterp());
			logger.info("username when updating" +systemConfigListDb.getUsername()); 
			systemConfigListDb2.setModifiedBy(systemConfigListDb.getUsername());
			systemConfigListRepository.save(systemConfigListDb2);
			auditTrailRepository.save(new AuditTrail(systemConfigListDb.getUserId(), systemConfigListDb.getUsername(), 0L, "SystemAdmin", 0L, 
					Features.FIELD_MANGEMENT, SubFeatures.UPDATE, "","NA","SystemAdmin",systemConfigListDb.getPublicIp(),systemConfigListDb.getBrowser()));
			return new GenricResponse(0);

		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public GenricResponse getTagsList(FilterRequest filterRequest) {
		try {
			if (Objects.isNull(filterRequest.getUserId())) {
				return new GenricResponse(1, GenericMessageTags.NULL_REQ.getTag(),
						GenericMessageTags.NULL_REQ.getMessage(), null);
			}
			User user = userRepository.getById(filterRequest.getUserId());

			auditTrailRepository.save(new AuditTrail(filterRequest.getUserId(), user.getUsername(), 0L, "SystemAdmin", 0L,
					Features.FIELD_MANGEMENT, SubFeatures.VIEW, "", "NA", "SystemAdmin",filterRequest.getPublicIp(),filterRequest.getBrowser()));
//				Features.CONFIG_LIST, SubFeatures.VIEW, ""));
			/*
			 * auditTrailRepository.save(new AuditTrail(filterRequest.getUserId(),
			 * user.getUsername(), 0L, "System", 0L, Features.FIELD_MANGEMENT,
			 * SubFeatures.VIEW, "","NA","System"));
			 */
			logger.info("AUDIT : Unique Tags list saved in audit_trail.");

			List<String> result = systemConfigListRepository.findDistinctTags();

			return new GenricResponse(0, "Sucess", "", result);

		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public GenricResponse findDistinctTagsWithDescription(FilterRequest filterRequest) {
		try {
			if (Objects.isNull(filterRequest.getUserId())) {
				return new GenricResponse(1, GenericMessageTags.NULL_REQ.getTag(),
						GenericMessageTags.NULL_REQ.getMessage(), null);
			}
			User user = userRepository.getById(filterRequest.getUserId());
			
			// auditTrailRepository.save(new AuditTrail(filterRequest.getUserId(),
			// user.getUsername(), 0L, "System", 0L,
			// Features.CONFIG_LIST, SubFeatures.VIEW, ""));

			/*
			 * auditTrailRepository.save(new AuditTrail(filterRequest.getUserId(),
			 * user.getUsername(), 0L, "SystemAdmin", 0L, Features.FIELD_MANGEMENT,
			 * SubFeatures.VIEW, "", "NA",
			 * "SystemAdmin",filterRequest.getPublicIp(),filterRequest.getBrowser()));
			 */

			logger.info("AUDIT : Unique Tags list saved in audit_trail.");

			List<SystemConfigListDb> systemConfigListDbs = systemConfigListRepository.findDistinctTagsWithDescription();
			systemConfigListDbs = systemConfigListDbs.stream()
					.sorted(Comparator.comparing(SystemConfigListDb::getDisplayName)).collect(Collectors.toList());
			return new GenricResponse(0, "Sucess", "", systemConfigListDbs);

		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public GenricResponse findById(FilterRequest filterRequest) {
		try {
			if (Objects.isNull(filterRequest.getId())) {
				return new GenricResponse(1, GenericMessageTags.NULL_REQ.getTag(),
						GenericMessageTags.NULL_REQ.getMessage(), null);
			}
			User user = userRepository.getById(filterRequest.getUserId());

			auditTrailRepository
					.save(new AuditTrail(filterRequest.getUserId(), user.getUsername(), Usertype.SYSTEM_ADMIN.getCode(),
							Usertype.SYSTEM_ADMIN.getName(), 0L, Features.FIELD_MANGEMENT, SubFeatures.VIEW, "","NA","SystemAdmin",filterRequest.getPublicIp(),filterRequest.getBrowser()));
			logger.info("AUDIT :  findById saved in audit_trail.");

			SystemConfigListDb systemConfigListDb = systemConfigListRepository.getById(filterRequest.getId());
			return new GenricResponse(0, "SUCCESS", "SUCCESS", systemConfigListDb);

		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public List<SystemConfigListDb> getAll(FilterRequest filterRequest) {

		try {
			List<SystemConfigListDb> systemConfigListDbs = systemConfigListRepository
					.findAll(buildSpecification(filterRequest).build());

			return systemConfigListDbs;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}

	}

	public Page<SystemConfigListDb> filter(FilterRequest filterRequest, Integer pageNo, Integer pageSize) {

		try {
			
			String orderColumn =null;
			Sort.Direction direction;
//			createdOn,taxPaidStatus,quantity,deviceQuantity,supplierName,consignmentStatus
			if(Objects.nonNull(filterRequest.getColumnName()))
			{
				logger.info("column Name :: " + filterRequest.getColumnName());
			
			orderColumn = "Created On".equalsIgnoreCase(filterRequest.getColumnName()) ? "createdOn"
					          : "Modified On".equalsIgnoreCase(filterRequest.getColumnName()) ? "modifiedOn"
					        		  : "Field".equalsIgnoreCase(filterRequest.getColumnName()) ? "tag"
					        				  : "Display Name".equalsIgnoreCase(filterRequest.getColumnName()) ? "interp"
					        						  : "Field ID".equalsIgnoreCase(filterRequest.getColumnName()) ? "tagId"
					        								  : "Description".equalsIgnoreCase(filterRequest.getColumnName()) ? "description"
					        								  	 : "modifiedOn";
			
			
			if("modifiedOn".equalsIgnoreCase(orderColumn)) {
				direction=Sort.Direction.DESC;
				}
				else {
				direction= SortDirection.getSortDirection(filterRequest.getSort());
				}
				if("modifiedOn".equalsIgnoreCase(orderColumn) && SortDirection.getSortDirection(filterRequest.getSort()).equals(Sort.Direction.ASC)) {
				direction=Sort.Direction.ASC;
				}
			}
			else {
				orderColumn="modifiedOn";
				direction=Sort.Direction.DESC;
			}
			Pageable pageable = PageRequest.of(pageNo, pageSize, new Sort(direction, orderColumn));
			logger.info("column Name :: " + orderColumn+"---direction : "+filterRequest.getSort());
			//Pageable pageable = PageRequest.of(pageNo, pageSize, new Sort(Sort.Direction.DESC, "modifiedOn"));
			Page<SystemConfigListDb> page;
			if ("-1".equals(filterRequest.getTag()) && filterRequest.getDisplayName().isEmpty()) {
				page = systemConfigListRepository.findAll(pageable);
				
			}
			 if("-1".equals(filterRequest.getTag()) && filterRequest.getDisplayName().isEmpty()) {
				page = systemConfigListRepository.findAll(buildSpecification(filterRequest).build(), pageable);
			}
			else {

				page = systemConfigListRepository.findAll(buildSpecification(filterRequest).build(), pageable);
			}

			return page;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}

	}

	public FileDetails getFilteredAuditTrailInFile(FilterRequest filterRequest) {

		logger.info("inside system configlist");
		logger.info("filterRequest data:  " + filterRequest);
		String fileName = null;
		Writer writer = null;
		SystemConfigListFileModel uPFm = null;
		SystemConfigurationDb dowlonadDir = systemConfigurationDbRepository.getByTag("file.download-dir");
		SystemConfigurationDb dowlonadLink = systemConfigurationDbRepository.getByTag("file.download-link");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		Integer pageNo = 0;
		Integer pageSize = Integer.valueOf(systemConfigurationDbRepository.getByTag("file.max-file-record").getValue());
		String filePath = dowlonadDir.getValue();
		StatefulBeanToCsvBuilder<SystemConfigListFileModel> builder = null;
		StatefulBeanToCsv<SystemConfigListFileModel> csvWriter = null;
		List<SystemConfigListFileModel> fileRecords = null;
		CustomMappingStrategy<SystemConfigListFileModel> mapStrategy = new CustomMappingStrategy<>();

		try {

			mapStrategy.setType(SystemConfigListFileModel.class);
			List<SystemConfigListDb> list = filter(filterRequest, pageNo, pageSize).getContent();

			if (list.size() > 0) {
				fileName = LocalDateTime.now().format(dtf).replace(" ", "_") + "_Field.csv";
			} else {
				fileName = LocalDateTime.now().format(dtf).replace(" ", "_") + "_Field.csv";
			}
			logger.info(" file path plus file name: " + Paths.get(filePath + fileName));
			writer = Files.newBufferedWriter(Paths.get(filePath + fileName));
//			builder = new StatefulBeanToCsvBuilder<UserProfileFileModel>(writer);
//			csvWriter = builder.withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER).build();
//			
			builder = new StatefulBeanToCsvBuilder<>(writer);
			
			csvWriter = builder.withMappingStrategy(mapStrategy).withSeparator(',')
					.withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER).build();
			
			if (list.size() > 0) {
				// List<SystemConfigListDb> systemConfigListDbs =
				// configurationManagementServiceImpl.getSystemConfigListByTag("GRIEVANCE_CATEGORY");
				fileRecords = new ArrayList<SystemConfigListFileModel>();
				for (SystemConfigListDb systemConfigListDb : list) {
					uPFm = new SystemConfigListFileModel();
					uPFm.setCreatedOn(systemConfigListDb.getCreatedOn().format(dtf));
					uPFm.setModifiedOn(systemConfigListDb.getModifiedOn().format(dtf));
					uPFm.setField(systemConfigListDb.getInterp());
					uPFm.setDisplayName(systemConfigListDb.getDisplayName());
					uPFm.setFieldID(systemConfigListDb.getTagId());
					uPFm.setDescription(systemConfigListDb.getDescription());

					fileRecords.add(uPFm);
				}
				csvWriter.write(fileRecords);
			}
			logger.info("fileName::" + fileName);
			logger.info("filePath::::" + filePath);
			logger.info("link:::" + dowlonadLink.getValue());

			return new FileDetails(fileName, filePath,
					dowlonadLink.getValue().replace("$LOCAL_IP", propertiesReader.localIp) + fileName);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		} finally {
			try {

				if (writer != null)
					writer.close();
			} catch (IOException e) {
			}
		}

		/*
		 * String fileName = null; Writer writer = null; SystemConfigListFileModel
		 * fileModel = null;
		 * 
		 * DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		 * DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
		 * 
		 * SystemConfigurationDb filepath =
		 * configurationManagementServiceImpl.findByTag(ConfigTags.file_download_dir);
		 * logger.info("CONFIG : file_consignment_download_dir [" + filepath + "]");
		 * SystemConfigurationDb link =
		 * configurationManagementServiceImpl.findByTag(ConfigTags.file_download_link);
		 * logger.info("CONFIG : file_consignment_download_link [" + link + "]");
		 * 
		 * if(Objects.isNull(filepath) || Objects.isNull(link)) { logger.
		 * info("CONFIG: MISSING : file_system_config_list_download_dir or file_system_config_list_download_link not found."
		 * ); return null; } String filePath = filepath.getValue();
		 * StatefulBeanToCsvBuilder<SystemConfigListFileModel> builder = null;
		 * StatefulBeanToCsv<SystemConfigListFileModel> csvWriter = null; List<
		 * SystemConfigListFileModel > fileRecords = null;
		 * 
		 * try { List<SystemConfigListDb> configListDbs = getAll(filterRequest); if(
		 * !configListDbs.isEmpty() ) { if(Objects.nonNull(filterRequest.getUserId()) &&
		 * (filterRequest.getUserId() != -1 && filterRequest.getUserId() != 0)) {
		 * fileName = LocalDateTime.now().format(dtf2).replace(" ", "_") +
		 * "_Config_Tag.csv"; }else { fileName =
		 * LocalDateTime.now().format(dtf2).replace(" ", "_") + "_ConfigTag.csv"; }
		 * }else { fileName = LocalDateTime.now().format(dtf2).replace(" ", "_") +
		 * "_Configtag.csv"; }
		 * 
		 * writer = Files.newBufferedWriter(Paths.get(filePath+fileName)); builder = new
		 * StatefulBeanToCsvBuilder<SystemConfigListFileModel>(writer); csvWriter =
		 * builder.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();
		 * 
		 * if( !configListDbs.isEmpty() ) { fileRecords = new ArrayList<>();
		 * 
		 * for(SystemConfigListDb systemConfigListDb : configListDbs ) { fileModel = new
		 * SystemConfigListFileModel();
		 * 
		 * // fileModel.setUserId(auditTrail.getUserId());
		 * 
		 * logger.debug(fileModel); fileRecords.add(fileModel); }
		 * 
		 * csvWriter.write(fileRecords); }else { csvWriter.write(new
		 * SystemConfigListFileModel()); } return new FileDetails( fileName, filePath,
		 * link.getValue().replace("$LOCAL_IP", propertiesReader.localIp) + fileName );
		 * 
		 * } catch (Exception e) { logger.error(e.getMessage(), e); throw new
		 * ResourceServicesException(this.getClass().getName(), e.getMessage());
		 * }finally { try { if( Objects.nonNull(writer) ) writer.close(); } catch
		 * (IOException e) {} }
		 */}

	private GenericSpecificationBuilder<SystemConfigListDb> buildSpecification(FilterRequest filterRequest) {
		GenericSpecificationBuilder<SystemConfigListDb> cmsb = new GenericSpecificationBuilder<>(
				propertiesReader.dialect);
		auditTrailRepository.save(new AuditTrail(filterRequest.getUserId(), filterRequest.getUserName(), 0L,filterRequest.getUserType(), 0L, 
				Features.FIELD_MANGEMENT, SubFeatures.VIEW_ALL, "","NA",filterRequest.getUserType(),filterRequest.getPublicIp(),filterRequest.getBrowser()));
		
		if (Objects.nonNull(filterRequest.getStartDate()) && !filterRequest.getStartDate().isEmpty())
			cmsb.with(new SearchCriteria("createdOn", filterRequest.getStartDate(), SearchOperation.GREATER_THAN,
					Datatype.DATE));

		if (Objects.nonNull(filterRequest.getEndDate()) && !filterRequest.getEndDate().isEmpty())
			cmsb.with(new SearchCriteria("createdOn", filterRequest.getEndDate(), SearchOperation.LESS_THAN,
					Datatype.DATE));

		
		if (Objects.nonNull(filterRequest.getTag()) && !"-1".equals(filterRequest.getTag())) {
			cmsb.with(new SearchCriteria("tag", filterRequest.getTag(), SearchOperation.EQUALITY, Datatype.STRING));
		}

		if (Objects.nonNull(filterRequest.getDisplayName()) && filterRequest.getDisplayName()!="") {
			cmsb.with(new SearchCriteria("interp", filterRequest.getDisplayName(), SearchOperation.LIKE, Datatype.STRING));
		}
		
		if (Objects.nonNull(filterRequest.getDescription()) && filterRequest.getDescription()!="") {
			cmsb.with(new SearchCriteria("description", filterRequest.getDescription(), SearchOperation.LIKE, Datatype.STRING));
		}
		
		if (Objects.nonNull(filterRequest.getField()) && filterRequest.getField()!="") {
			cmsb.with(new SearchCriteria("tagId", filterRequest.getField(), SearchOperation.LIKE, Datatype.STRING));
		}
	
		if (Objects.nonNull(filterRequest.getSearchString()) && !filterRequest.getSearchString().isEmpty()) {
			cmsb.orSearch(new SearchCriteria("description", filterRequest.getSearchString(), SearchOperation.LIKE,
					Datatype.STRING));
			cmsb.orSearch(new SearchCriteria("interp", filterRequest.getSearchString(), SearchOperation.LIKE,
					Datatype.STRING));
			cmsb.orSearch(
					new SearchCriteria("tag", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
			cmsb.orSearch(new SearchCriteria("tagId", filterRequest.getSearchString(), SearchOperation.LIKE,
					Datatype.STRING));
		}

		return cmsb;
	}

	public GenricResponse deleteValue(FilterRequest filterRequest) {
		try {
			if (Objects.isNull(filterRequest.getUserId())) {
				return new GenricResponse(1, GenericMessageTags.NULL_REQ.getTag(),
						GenericMessageTags.NULL_REQ.getMessage(), null);
			}
			User user = userRepository.getById(filterRequest.getUserId());

			auditTrailRepository.save(new AuditTrail(user.getId(), user.getUsername(), 0L, "SystemAdmin", 0L,
					Features.FIELD_MANGEMENT, SubFeatures.DELETE, "","NA","SystemAdmin",filterRequest.getPublicIp(),filterRequest.getBrowser()));
			logger.info("AUDIT : Delete Tags list saved in audit_trail.");

			systemConfigListRepository.deleteById(filterRequest.getId());

			return new GenricResponse(0, "Sucess", "", "");

		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	/*
	 * private void setInterp(AuditTrail auditTrail) {
	 * if(Objects.nonNull(consignmentMgmt.getExpectedArrivalPort()))
	 * consignmentMgmt.setExpectedArrivalPortInterp(interpSetter.setConfigInterp(
	 * Tags.CUSTOMS_PORT, consignmentMgmt.getExpectedArrivalPort()));
	 * 
	 * }
	 */

}
