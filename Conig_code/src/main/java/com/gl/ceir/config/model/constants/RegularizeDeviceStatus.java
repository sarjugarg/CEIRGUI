package com.gl.ceir.config.model.constants;

public enum RegularizeDeviceStatus {
	NEW(0, "New"), 
	PROCESSING(1, "Processing"), 
	REJECTED_BY_SYSTEM(2, "Rejected By System"), 
	PENDING_APPROVAL_FROM_CEIR_ADMIN(3, "Pending Approval From CEIR Admin"), 
	WITHDRAWN_BY_USER(4, "Withdrawn By User"), 
	APPROVED(6, "Approved"),
	REJECTED_BY_CEIR_ADMIN(7, "Rejected by CEIR Admin"), 
	WITHDRAWN_BY_CEIR_ADMIN(8, "Withdrawn By CEIR Admin");

	private int code;
	private String desc;

	RegularizeDeviceStatus(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public static RegularizeDeviceStatus getActionNames(int code) {
		for (RegularizeDeviceStatus consignmentStatus : RegularizeDeviceStatus.values()) {
			if (consignmentStatus.getCode() == code)
				return consignmentStatus;
		}

		return null;
	}
}
