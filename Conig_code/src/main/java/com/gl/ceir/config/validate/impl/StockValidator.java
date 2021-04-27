package com.gl.ceir.config.validate.impl;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.gl.ceir.config.exceptions.RequestInvalidException;
import com.gl.ceir.config.model.ConsignmentUpdateRequest;
import com.gl.ceir.config.model.FilterRequest;
import com.gl.ceir.config.model.StockMgmt;
import com.gl.ceir.config.validate.BaseValidator;

@Component
public class StockValidator extends BaseValidator<StockMgmt>{
	private static final Logger logger = LogManager.getLogger(StockValidator.class);

	String target = "Stock_mgmt";

	@Override
	public boolean validateRegister(StockMgmt t) throws RequestInvalidException {
		RequestInvalidException exception = null;
		String action = "Register";

		return Boolean.TRUE;
	}

	@Override
	public boolean validateEdit(StockMgmt t) throws RequestInvalidException {
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

		if(Objects.isNull(t.getUserType())) {
			exception =  new RequestInvalidException(target, "userType", t.getUserType());
			return event(exception, action, t.getTxnId());
		}

		if(Objects.isNull(t.getRoleType())) {
			exception =  new RequestInvalidException(target, "roleType", t.getRoleType());
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

		if(Objects.isNull(t.getSuplierName())) {
			exception =  new RequestInvalidException(target, "suplierName", t.getSuplierName());
			return event(exception, action, t.getTxnId());
		}

		return Boolean.TRUE;
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
