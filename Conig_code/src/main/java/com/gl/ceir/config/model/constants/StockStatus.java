package com.gl.ceir.config.model.constants;

public enum StockStatus {
	NEW(0), 
	PROCESSING(1), 
	REJECTED_BY_SYSTEM(2), 
	PENDING_APPROVAL_FROM_CEIR_ADMIN(3), 
	WITHDRAWN_BY_USER(4), 
	RECOVERY(5), 
	APPROVED_BY_CEIR_ADMIN(6),
	REJECTED_BY_CEIR_ADMIN(7),
	WITHDRAWN_BY_CEIR_ADMIN(8),
	STOLEN(10);
	
	private int code;

	StockStatus(int code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public static StockStatus getActionNames(int code) {
		for (StockStatus codes : StockStatus.values()) {
			if (codes.getCode() == code)
				return codes;
		}

		return null;
	}
}
