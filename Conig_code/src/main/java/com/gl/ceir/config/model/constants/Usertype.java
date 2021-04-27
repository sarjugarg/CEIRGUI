package com.gl.ceir.config.model.constants;

public enum Usertype {
	ADMIN(1L, "admin"),
	IMPORTER(4L, "Importer"),
	DISTRIBUTOR(5L, "Distributor"),
	RETAILER(6L, "Retailer"),
	CUSTOMS(7L, "Custom"),
	CEIR_ADMIN(8L, "CEIRAdmin"),
	OPERATOR(9L, "Operator"),
	TRC(10L, "TRC"),
	MANUFACTURER(12L, "Manufacturer"),
	SYSTEM_ADMIN(13L, "SystemAdmin"),
	LAWFUL_AGENCY(14L, "Lawful Agency"),
	END_USER(17L, "End User"),
	IMMIGRATION(18L, "Immigration"),
	ANONYMOUS(19L, "Anonymous"),
	;
	
	private Long code;
	private String name;
	
	Usertype(Long code, String name){
		this.code = code;
		this.name = name;
	}
	
	public Long getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

}
