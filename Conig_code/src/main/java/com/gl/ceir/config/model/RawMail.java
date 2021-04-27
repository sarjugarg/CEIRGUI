package com.gl.ceir.config.model;

import java.io.Serializable;
import java.util.Map;

public class RawMail implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String tag;
	private UserProfile userProfile;
	private long featureId;
	private String featureName;
	private String subFeature;
	private String featureTxnId;
	private String txnId; 
	private String referTable;
	private Map<String, String> placeholders;
	private String roleType;
	private String receiverUserType;
	
	public RawMail(String tag, UserProfile userProfile, long featureId, String featureName, String subFeature,
			String featureTxnId, String txnId, Map<String, String> placeholders, String referTable, 
			String roleType, String receiverUserType) {
		super();
		this.tag = tag;
		this.userProfile = userProfile;
		this.featureId = featureId;
		this.featureName = featureName;
		this.subFeature = subFeature;
		this.featureTxnId = featureTxnId;
		this.txnId = txnId;
		this.placeholders = placeholders;
		this.referTable = referTable;
		this.roleType = roleType;
		this.receiverUserType = receiverUserType;
	}
	
	public RawMail(String tag, long userId, long featureId, String featureName, String subFeature,
			String featureTxnId, String txnId, Map<String, String> placeholders, String referTable,
			 String roleType, String receiverUserType) {
		super();
		this.tag = tag;
		User user=new User();
		user.setId(userId);
		UserProfile profile=new UserProfile();
		profile.setUser(user);
		this.userProfile = profile;
		this.featureId = featureId;
		this.featureName = featureName;
		this.subFeature = subFeature;
		this.featureTxnId = featureTxnId;
		this.txnId = txnId;
		this.placeholders = placeholders;
		this.referTable = referTable;
		this.roleType = roleType;
		this.receiverUserType = receiverUserType;
	}

	
	public String getReceiverUserType() {
		return receiverUserType;
	}

	public void setReceiverUserType(String receiverUserType) {
		this.receiverUserType = receiverUserType;
	}

	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public UserProfile getUserProfile() {
		return userProfile;
	}
	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}
	public long getFeatureId() {
		return featureId;
	}
	public void setFeatureId(long featureId) {
		this.featureId = featureId;
	}
	public String getFeatureName() {
		return featureName;
	}
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
	public String getSubFeature() {
		return subFeature;
	}
	public void setSubFeature(String subFeature) {
		this.subFeature = subFeature;
	}
	public String getFeatureTxnId() {
		return featureTxnId;
	}
	public void setFeatureTxnId(String featureTxnId) {
		this.featureTxnId = featureTxnId;
	}
	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public Map<String, String> getPlaceholders() {
		return placeholders;
	}
	public void setPlaceholders(Map<String, String> placeholders) {
		this.placeholders = placeholders;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getReferTable() {
		return referTable;
	}

	public void setReferTable(String referTable) {
		this.referTable = referTable;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RawMail [tag=");
		builder.append(tag);
		builder.append(", userProfile=");
		builder.append(userProfile);
		builder.append(", featureId=");
		builder.append(featureId);
		builder.append(", featureName=");
		builder.append(featureName);
		builder.append(", subFeature=");
		builder.append(subFeature);
		builder.append(", featureTxnId=");
		builder.append(featureTxnId);
		builder.append(", subject=");
		builder.append(txnId);
		builder.append(", placeholders=");
		builder.append(placeholders);
		builder.append("]");
		return builder.toString();
	}
	
	

}
