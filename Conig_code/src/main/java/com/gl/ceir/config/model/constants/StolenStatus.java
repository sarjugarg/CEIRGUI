package com.gl.ceir.config.model.constants;

public enum StolenStatus {
	
	INIT(0, "INIT"), 
	PROCESSING(1, "Processing"), 
	REJECTED_BY_SYSTEM(2, "Error"),
	PENDING_APPROVAL_FROM_CEIR_ADMIN(3, "Pending Approval From CEIR Admin"), 
	REJECTED_BY_CEIR_ADMIN(4, "Rejected CEIR Admin"),
	APPROVED_BY_CEIR_ADMIN(5, "Approved CEIR Admin");

	private int code;
	private String desc;

	StolenStatus(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public static StolenStatus getActionNames(int code) {
		for (StolenStatus consignmentStatus : StolenStatus.values()) {
			if (consignmentStatus.getCode() == code)
				return consignmentStatus;
		}

		return null;
	}
}
