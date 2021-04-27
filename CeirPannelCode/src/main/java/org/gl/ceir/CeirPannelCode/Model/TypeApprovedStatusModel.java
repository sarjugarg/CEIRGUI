package org.gl.ceir.CeirPannelCode.Model;

public class TypeApprovedStatusModel {
		private String adminUserType,txnId,remark;
		private Integer adminUserId,adminApproveStatus,featureId;
		public String publicIp,browser;
		
		
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
		public String getAdminUserType() {
			return adminUserType;
		}
		public void setAdminUserType(String adminUserType) {
			this.adminUserType = adminUserType;
		}
		public String getTxnId() {
			return txnId;
		}
		public void setTxnId(String txnId) {
			this.txnId = txnId;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public Integer getAdminUserId() {
			return adminUserId;
		}
		public void setAdminUserId(Integer adminUserId) {
			this.adminUserId = adminUserId;
		}
		public Integer getAdminApproveStatus() {
			return adminApproveStatus;
		}
		public void setAdminApproveStatus(Integer adminApproveStatus) {
			this.adminApproveStatus = adminApproveStatus;
		}
		public Integer getFeatureId() {
			return featureId;
		}
		public void setFeatureId(Integer featureId) {
			this.featureId = featureId;
		}
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("TypeApprovedStatusModel [adminUserType=");
			builder.append(adminUserType);
			builder.append(", txnId=");
			builder.append(txnId);
			builder.append(", remark=");
			builder.append(remark);
			builder.append(", adminUserId=");
			builder.append(adminUserId);
			builder.append(", adminApproveStatus=");
			builder.append(adminApproveStatus);
			builder.append(", featureId=");
			builder.append(featureId);
			builder.append(", publicIp=");
			builder.append(publicIp);
			builder.append(", browser=");
			builder.append(browser);
			builder.append("]");
			return builder.toString();
		}
		
		
		
		
		
		
		
}
