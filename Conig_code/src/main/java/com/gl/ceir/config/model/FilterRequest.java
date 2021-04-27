package com.gl.ceir.config.model;

import javax.persistence.Transient;

public class FilterRequest {

	private Long id;
	private Integer userId;
	private Long importerId;
	private String nid;
	private String txnId;
	private String startDate;
	private String endDate;
	private Integer consignmentStatus;
	private String roleType;
	private Integer requestType;
	private Integer sourceType;
	private String userType;
	private String filteredUserType;
	private Integer featureId;
	private String featureName;
	private String subFeatureName;
	private String userName;
	private Integer userTypeId;
	private String searchString;
	private Integer taxPaidStatus;
	private Integer deviceIdType;
	private Integer deviceType;
	private Integer type;
	private Integer channel;

	private Integer status;

	private Integer operatorTypeId;
	private String origin;
	
	private String tac;
	
	// Mapping for parent child tags.
	private String tag;
	private String childTag;
	private Integer parentValue;
	
	private String imei;
	private Long contactNumber;
	private Integer filteredUserId;
	
	private String state;
	
	private String ruleName;
	
	private String remark;
	
	private String displayName;
	
	private String quantity;
	
	public String deviceQuantity;
	
	private String subject;
	
	private String supplierName;
	private String fileName,nationality;
	private String columnName;
	private String sort,blockingTypeFilter;
	
	public String visaType,visaNumber,visaExpiryDate;
	public String order,orderColumnName;
	public String description,name;
	private String publicIp;
	private String browser;
	private String value,field,tagId;
	
	private String graceAction,postGraceAction,ruleOrder,failedRuleActionGrace,failedRuleActionPostGrace,output;
	public String getFilteredUserType() {
		return filteredUserType;
	}
	
	public void setFilteredUserType(String filteredUserType) {
		this.filteredUserType = filteredUserType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Integer getFilteredUserId() {
		return filteredUserId;
	}
	public void setFilteredUserId(Integer filteredUserId) {
		this.filteredUserId = filteredUserId;
	}
	public String getSubFeatureName() {
		return subFeatureName;
	}
	public void setSubFeatureName(String subFeatureName) {
		this.subFeatureName = subFeatureName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFeatureName() {
		return featureName;
	}
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public Long getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(Long contactNumber) {
		this.contactNumber = contactNumber;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	public String getTxnId() {
		return txnId;
	}
	public FilterRequest setTxnId(String txnId) {
		this.txnId = txnId;
		return this;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Integer getTaxPaidStatus() {
		return taxPaidStatus;
	}
	public void setTaxPaidStatus(Integer taxPaidStatus) {
		this.taxPaidStatus = taxPaidStatus;
	}
	public Integer getSourceType() {
		return sourceType;
	}
	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}
	public String getRoleType() {
		return roleType;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	public Integer getConsignmentStatus() {
		return consignmentStatus;
	}
	public void setConsignmentStatus(Integer consignmentStatus) {
		this.consignmentStatus = consignmentStatus;
	}
	public Integer getRequestType() {
		return requestType;
	}
	public void setRequestType(Integer requestType) {
		this.requestType = requestType;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public Integer getFeatureId() {
		return featureId;
	}
	public void setFeatureId(Integer featureId) {
		this.featureId = featureId;
	}
	public Integer getUserTypeId() {
		return userTypeId;
	}
	public void setUserTypeId(Integer userTypeId) {
		this.userTypeId = userTypeId;
	}
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	public Integer getDeviceIdType() {
		return deviceIdType;
	}
	public void setDeviceIdType(Integer deviceIdType) {
		this.deviceIdType = deviceIdType;
	}
	public Integer getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getChannel() {
		return channel;
	}
	public void setChannel(Integer channel) {
		this.channel = channel;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getOperatorTypeId() {
		return operatorTypeId;
	}
	public void setOperatorTypeId(Integer operatorTypeId) {
		this.operatorTypeId = operatorTypeId;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getChildTag() {
		return childTag;
	}
	public void setChildTag(String childTag) {
		this.childTag = childTag;
	}
	public Integer getParentValue() {
		return parentValue;
	}
	public void setParentValue(Integer parentValue) {
		this.parentValue = parentValue;
	}
	public String getTac() {
		return tac;
	}
	public void setTac(String tac) {
		this.tac = tac;
	}
	public Long getImporterId() {
		return importerId;
	}
	public void setImporterId(Long importerId) {
		this.importerId = importerId;
	}

	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getDeviceQuantity() {
		return deviceQuantity;
	}

	public void setDeviceQuantity(String deviceQuantity) {
		this.deviceQuantity = deviceQuantity;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getBlockingTypeFilter() {
		return blockingTypeFilter;
	}

	public void setBlockingTypeFilter(String blockingTypeFilter) {
		this.blockingTypeFilter = blockingTypeFilter;
	}

	public String getVisaType() {
		return visaType;
	}

	public void setVisaType(String visaType) {
		this.visaType = visaType;
	}

	public String getVisaNumber() {
		return visaNumber;
	}

	public void setVisaNumber(String visaNumber) {
		this.visaNumber = visaNumber;
	}

	public String getVisaExpiryDate() {
		return visaExpiryDate;
	}

	public void setVisaExpiryDate(String visaExpiryDate) {
		this.visaExpiryDate = visaExpiryDate;
	}

	public String getPublicIp() {
		return publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getOrderColumnName() {
		return orderColumnName;
	}

	public void setOrderColumnName(String orderColumnName) {
		this.orderColumnName = orderColumnName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getGraceAction() {
		return graceAction;
	}

	public void setGraceAction(String graceAction) {
		this.graceAction = graceAction;
	}

	public String getPostGraceAction() {
		return postGraceAction;
	}

	public void setPostGraceAction(String postGraceAction) {
		this.postGraceAction = postGraceAction;
	}

	public String getRuleOrder() {
		return ruleOrder;
	}

	public void setRuleOrder(String ruleOrder) {
		this.ruleOrder = ruleOrder;
	}

	public String getFailedRuleActionGrace() {
		return failedRuleActionGrace;
	}

	public void setFailedRuleActionGrace(String failedRuleActionGrace) {
		this.failedRuleActionGrace = failedRuleActionGrace;
	}

	public String getFailedRuleActionPostGrace() {
		return failedRuleActionPostGrace;
	}

	public void setFailedRuleActionPostGrace(String failedRuleActionPostGrace) {
		this.failedRuleActionPostGrace = failedRuleActionPostGrace;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FilterRequest [id=");
		builder.append(id);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", importerId=");
		builder.append(importerId);
		builder.append(", nid=");
		builder.append(nid);
		builder.append(", txnId=");
		builder.append(txnId);
		builder.append(", startDate=");
		builder.append(startDate);
		builder.append(", endDate=");
		builder.append(endDate);
		builder.append(", consignmentStatus=");
		builder.append(consignmentStatus);
		builder.append(", roleType=");
		builder.append(roleType);
		builder.append(", requestType=");
		builder.append(requestType);
		builder.append(", sourceType=");
		builder.append(sourceType);
		builder.append(", userType=");
		builder.append(userType);
		builder.append(", filteredUserType=");
		builder.append(filteredUserType);
		builder.append(", featureId=");
		builder.append(featureId);
		builder.append(", featureName=");
		builder.append(featureName);
		builder.append(", subFeatureName=");
		builder.append(subFeatureName);
		builder.append(", userName=");
		builder.append(userName);
		builder.append(", userTypeId=");
		builder.append(userTypeId);
		builder.append(", searchString=");
		builder.append(searchString);
		builder.append(", taxPaidStatus=");
		builder.append(taxPaidStatus);
		builder.append(", deviceIdType=");
		builder.append(deviceIdType);
		builder.append(", deviceType=");
		builder.append(deviceType);
		builder.append(", type=");
		builder.append(type);
		builder.append(", channel=");
		builder.append(channel);
		builder.append(", status=");
		builder.append(status);
		builder.append(", operatorTypeId=");
		builder.append(operatorTypeId);
		builder.append(", origin=");
		builder.append(origin);
		builder.append(", tac=");
		builder.append(tac);
		builder.append(", tag=");
		builder.append(tag);
		builder.append(", childTag=");
		builder.append(childTag);
		builder.append(", parentValue=");
		builder.append(parentValue);
		builder.append(", imei=");
		builder.append(imei);
		builder.append(", contactNumber=");
		builder.append(contactNumber);
		builder.append(", filteredUserId=");
		builder.append(filteredUserId);
		builder.append(", state=");
		builder.append(state);
		builder.append(", ruleName=");
		builder.append(ruleName);
		builder.append(", remark=");
		builder.append(remark);
		builder.append(", displayName=");
		builder.append(displayName);
		builder.append(", quantity=");
		builder.append(quantity);
		builder.append(", deviceQuantity=");
		builder.append(deviceQuantity);
		builder.append(", subject=");
		builder.append(subject);
		builder.append(", supplierName=");
		builder.append(supplierName);
		builder.append(", fileName=");
		builder.append(fileName);
		builder.append(", nationality=");
		builder.append(nationality);
		builder.append(", columnName=");
		builder.append(columnName);
		builder.append(", sort=");
		builder.append(sort);
		builder.append(", blockingTypeFilter=");
		builder.append(blockingTypeFilter);
		builder.append(", visaType=");
		builder.append(visaType);
		builder.append(", visaNumber=");
		builder.append(visaNumber);
		builder.append(", visaExpiryDate=");
		builder.append(visaExpiryDate);
		builder.append(", order=");
		builder.append(order);
		builder.append(", orderColumnName=");
		builder.append(orderColumnName);
		builder.append(", description=");
		builder.append(description);
		builder.append(", name=");
		builder.append(name);
		builder.append(", publicIp=");
		builder.append(publicIp);
		builder.append(", browser=");
		builder.append(browser);
		builder.append(", value=");
		builder.append(value);
		builder.append(", field=");
		builder.append(field);
		builder.append(", tagId=");
		builder.append(tagId);
		builder.append(", graceAction=");
		builder.append(graceAction);
		builder.append(", postGraceAction=");
		builder.append(postGraceAction);
		builder.append(", ruleOrder=");
		builder.append(ruleOrder);
		builder.append(", failedRuleActionGrace=");
		builder.append(failedRuleActionGrace);
		builder.append(", failedRuleActionPostGrace=");
		builder.append(failedRuleActionPostGrace);
		builder.append(", output=");
		builder.append(output);
		builder.append("]");
		return builder.toString();
	}

 	 
	
	
}


