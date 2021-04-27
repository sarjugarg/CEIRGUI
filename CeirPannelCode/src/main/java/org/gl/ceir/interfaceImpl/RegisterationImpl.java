package org.gl.ceir.interfaceImpl;

import javax.servlet.http.HttpServletRequest;

import org.gl.ceir.CeirPannelCode.Model.TRCRegisteration;
import org.gl.ceir.interfacepackage.Registeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class RegisterationImpl implements Registeration{
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	
	@Autowired
	TRCRegisteration model;
	
	@Override
	public TRCRegisteration register(HttpServletRequest request,String fileName,String txnId) {
		// TODO Auto-generated method stub
		
		model.setManufacturerId(request.getParameter("manufacturerId"));
		model.setManufacturerName(request.getParameter("manufacturerName"));
		model.setCountry(request.getParameter("country"));
		model.setTac(request.getParameter("tac"));
		model.setApproveStatus(Integer.parseInt(request.getParameter("approveStatus")));
		model.setApproveDisapproveDate(request.getParameter("approveDisapproveDate"));
		model.setRemark(request.getParameter("remark"));
		model.setRequestDate(request.getParameter("requestDate"));
		model.setUserId(Integer.parseInt(request.getParameter("userId")));
		model.setTxnId(txnId);
		model.setFileName(fileName);
		log.info("---------model inside implementation class---------"+model);
		return model;
	}

	@Override
	public TRCRegisteration updateRegister(HttpServletRequest request) {
		// TODO Auto-generated method stub
		model.setManufacturerId(request.getParameter("manufacturerId"));
		model.setManufacturerName(request.getParameter("manufacturerName"));
		model.setCountry(request.getParameter("country"));
		model.setTac(request.getParameter("tac"));
		//model.setStatus(Integer.parseInt(request.getParameter("status")));
		model.setApproveStatus(Integer.parseInt(request.getParameter("status")));
		model.setApproveDisapproveDate(request.getParameter("approveDisapproveDate"));
		model.setRemark(request.getParameter("remark"));
		model.setFileName(request.getParameter("file"));
		model.setId(Integer.parseInt(request.getParameter("id")));
		 model.setUserId(385); 
		 model.setRequestDate(request.getParameter("requestDate"));
		 model.setTxnId(request.getParameter("txnId"));
		log.info("---------modelmodel---------"+request.getParameter("manufacturerId"));
		log.info("---------modelmodel---------"+model);
		return model;
	}
}
