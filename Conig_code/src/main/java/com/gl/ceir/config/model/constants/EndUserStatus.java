package com.gl.ceir.config.model.constants;

public enum EndUserStatus {

	Init(0), PROCESSING(1), REJECTED_BY_SYSTEM(2),
	PENDING_APPROVAL_ON_CEIR_ADMIN(3), APPROVED(4), REJECTED_BY_CEIR_ADMIN(5); 

	private int code;
	private String desc;

	EndUserStatus(int code) {
		this.code = code;

	}

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public static EndUserStatus getActionNames(int code) {
		for (EndUserStatus consignmentStatus : EndUserStatus.values()) {
			if (consignmentStatus.getCode() == code)
				return consignmentStatus;
		}

		return null;
	}
}
