package com.gl.ceir.config.validate.impl;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.gl.ceir.config.exceptions.RequestInvalidException;
import com.gl.ceir.config.model.ConsignmentMgmt;
import com.gl.ceir.config.model.ConsignmentUpdateRequest;
import com.gl.ceir.config.model.FilterRequest;
import com.gl.ceir.config.validate.BaseValidator;

@Component
public class ConsigmentValidator extends BaseValidator<ConsignmentMgmt>{

	private static final Logger logger = LogManager.getLogger(ConsigmentValidator.class);

	String target = "consignment_mgmt";

	@Override
	public boolean validateRegister(ConsignmentMgmt t) throws RequestInvalidException {
		RequestInvalidException exception = null;
		String action = "Register";

		if(Objects.isNull(t.getUserId())) {
			exception =  new RequestInvalidException(target, "userId", t.getUserId());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getTxnId())) {
			exception =  new RequestInvalidException(target, "txnId", t.getTxnId());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getUserTypeId())) {
			exception =  new RequestInvalidException(target, "userTypeId", t.getUserTypeId());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getUserType())) {
			exception =  new RequestInvalidException(target, "userType", t.getUserType());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getFeatureId())) {
			exception =  new RequestInvalidException(target, "featureId", t.getFeatureId());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getRoleType())) {
			exception =  new RequestInvalidException(target, "roleType", t.getRoleType());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getSupplierName())) {
			exception =  new RequestInvalidException(target, "supplierName", t.getSupplierName());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getExpectedDispatcheDate())) {
			exception =  new RequestInvalidException(target, "expectedDispatcheDate", t.getExpectedDispatcheDate());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getExpectedArrivaldate())) {
			exception =  new RequestInvalidException(target, "expectedArrivaldate", t.getExpectedArrivaldate());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getOrganisationCountry())) {
			exception =  new RequestInvalidException(target, "organisationCountry", t.getOrganisationCountry());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getExpectedArrivalPort())) {
			exception =  new RequestInvalidException(target, "expectedArrivalPort", t.getExpectedArrivalPort());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getPortAddress())) {
			exception =  new RequestInvalidException(target, "portAddress", t.getPortAddress());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getQuantity())) {
			exception =  new RequestInvalidException(target, "quantity", t.getQuantity());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getDeviceQuantity())) {
			exception =  new RequestInvalidException(target, "deviceQuantity", t.getDeviceQuantity());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getFileName())) {
			exception =  new RequestInvalidException(target, "fileName", t.getFileName());
			return event(exception, action, t.getTxnId());
		}

		return event(exception, action, t.getTxnId());
	}

	@Override
	public boolean validateEdit(ConsignmentMgmt t) throws RequestInvalidException {
		RequestInvalidException exception = null;
		String action = "Edit";

		if(Objects.isNull(t.getUserId())) {
			exception =  new RequestInvalidException(target, "userId", t.getUserId());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getTxnId())) {
			exception =  new RequestInvalidException(target, "txnId", t.getTxnId());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getUserTypeId())) {
			exception =  new RequestInvalidException(target, "userTypeId", t.getUserType());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getUserType())) {
			exception =  new RequestInvalidException(target, "userType", t.getUserType());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getFeatureId())) {
			exception =  new RequestInvalidException(target, "featureId", t.getFeatureId());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getRoleType())) {
			exception =  new RequestInvalidException(target, "roleType", t.getRoleType());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getSupplierName())) {
			exception =  new RequestInvalidException(target, "supplierName", t.getSupplierName());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getExpectedDispatcheDate())) {
			exception =  new RequestInvalidException(target, "expectedDispatcheDate", t.getExpectedDispatcheDate());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getExpectedArrivaldate())) {
			exception =  new RequestInvalidException(target, "expectedArrivaldate", t.getExpectedArrivaldate());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getOrganisationCountry())) {
			exception =  new RequestInvalidException(target, "organisationCountry", t.getOrganisationCountry());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getExpectedArrivalPort())) {
			exception =  new RequestInvalidException(target, "expectedArrivalPort", t.getExpectedArrivalPort());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getPortAddress())) {
			exception =  new RequestInvalidException(target, "portAddress", t.getPortAddress());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getQuantity())) {
			exception =  new RequestInvalidException(target, "quantity", t.getQuantity());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getDeviceQuantity())) {
			exception =  new RequestInvalidException(target, "deviceQuantity", t.getDeviceQuantity());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getFileName())) {
			exception =  new RequestInvalidException(target, "fileName", t.getFileName());
			return event(exception, action, t.getTxnId());
		}

		return event(exception, action, t.getTxnId());
	}

	@Override
	public boolean validateViewById(Object t) throws RequestInvalidException {
		RequestInvalidException exception = null;
		String action = "ViewById";

		FilterRequest c = (FilterRequest) t;

		if(Objects.isNull(c.getUserId())) {
			exception =  new RequestInvalidException(target, "userId", c.getUserId());
			return event(exception, action, c.getTxnId());
		}

		if(Objects.isNull(c.getTxnId())) {
			exception =  new RequestInvalidException(target, "txnId", c.getTxnId());
			return event(exception, action, c.getTxnId());
		}

		if(Objects.isNull(c.getUserTypeId())) {
			exception =  new RequestInvalidException(target, "userTypeId", c.getUserTypeId());
			return event(exception, action, c.getTxnId());
		}

		if(Objects.isNull(c.getUserType())) {
			exception =  new RequestInvalidException(target, "userType", c.getUserType());
			return event(exception, action, c.getTxnId());
		}

		if(Objects.isNull(c.getFeatureId())) {
			exception =  new RequestInvalidException(target, "featureId", c.getFeatureId());
			return event(exception, action, c.getTxnId());
		}

		if(Objects.isNull(c.getRoleType())) {
			exception =  new RequestInvalidException(target, "roleType", c.getRoleType());
			return event(exception, action, c.getTxnId());
		}

		return Boolean.TRUE;
	}

	@Override
	public boolean validateDelete(Object t) throws RequestInvalidException {
		RequestInvalidException exception = null;
		String action = "Delete";

		ConsignmentUpdateRequest c = (ConsignmentUpdateRequest) t;
		if(Objects.isNull(c.getUserId())) {
			exception =  new RequestInvalidException(target, "userId", c.getUserId());
			return event(exception, action, c.getTxnId());
		}

		if(Objects.isNull(c.getTxnId())) {
			exception =  new RequestInvalidException(target, "txnId", c.getTxnId());
			return event(exception, action, c.getTxnId());
		}

		if(Objects.isNull(c.getUserTypeId())) {
			exception =  new RequestInvalidException(target, "userTypeId", c.getUserTypeId());
			return event(exception, action, c.getTxnId());
		}

		if(Objects.isNull(c.getUserType())) {
			exception =  new RequestInvalidException(target, "userType", c.getUserType());
			return event(exception, action, c.getTxnId());
		}

		if(Objects.isNull(c.getFeatureId())) {
			exception =  new RequestInvalidException(target, "featureId", c.getFeatureId());
			return event(exception, action, c.getTxnId());
		}

		if(Objects.isNull(c.getRoleType())) {
			exception =  new RequestInvalidException(target, "roleType", c.getRoleType());
			return event(exception, action, c.getTxnId());
		}

		return Boolean.TRUE;
	}

	@Override
	public boolean validateFilter(Object t) throws RequestInvalidException {
		RequestInvalidException exception = null;
		String action = "Filter";

		FilterRequest c = (FilterRequest) t;
		if(Objects.isNull(c.getUserId())) {
			exception =  new RequestInvalidException(target, "userId", c.getUserId());
			return event(exception, action, c.getTxnId());
		}

		if(Objects.isNull(c.getUserTypeId())) {
			exception =  new RequestInvalidException(target, "userTypeId", c.getUserTypeId());
			return event(exception, action, c.getTxnId());
		}

		if(Objects.isNull(c.getUserType())) {
			exception =  new RequestInvalidException(target, "userType", c.getUserType());
			return event(exception, action, c.getTxnId());
		}

		if(Objects.isNull(c.getFeatureId())) {
			exception =  new RequestInvalidException(target, "featureId", c.getFeatureId());
			return event(exception, action, c.getTxnId());
		}

		if(Objects.isNull(c.getRoleType())) {
			exception =  new RequestInvalidException(target, "roleType", c.getRoleType());
			return event(exception, action, c.getTxnId());
		}

		return Boolean.TRUE;
	}

	@Override
	public boolean validateAcceptReject(Object t) throws RequestInvalidException {
		RequestInvalidException exception = null;
		String action = "Accept/Reject";

		ConsignmentUpdateRequest c = (ConsignmentUpdateRequest) t;

		if(Objects.isNull(c.getRoleType())) {
			exception =  new RequestInvalidException(target, "roleType", c.getRoleType());
			return event(exception, action, c.getTxnId());
		}
		
		if(Objects.isNull(c.getTxnId())) {
			exception =  new RequestInvalidException(target, "txnId", c.getTxnId());
			return event(exception, action, c.getTxnId());
		}

		if(Objects.isNull(c.getFeatureId())) {
			exception =  new RequestInvalidException(target, "featureId", c.getFeatureId());
			return event(exception, action, c.getTxnId());
		}
		
		if(!"CEIRSYSTEM".equalsIgnoreCase(c.getRoleType())) {
			if(Objects.isNull(c.getUserId())) {
				exception =  new RequestInvalidException(target, "userId", c.getUserId());
				return event(exception, action, c.getTxnId());
			}
			
			if(Objects.isNull(c.getUserTypeId())) {
				exception =  new RequestInvalidException(target, "userTypeId", c.getUserTypeId());
				return event(exception, action, c.getTxnId());
			}
			
			if(Objects.isNull(c.getUserType())) {
				exception =  new RequestInvalidException(target, "userType", c.getUserType());
				return event(exception, action, c.getTxnId());
			}
		}

		return Boolean.TRUE;
	}
	
}
