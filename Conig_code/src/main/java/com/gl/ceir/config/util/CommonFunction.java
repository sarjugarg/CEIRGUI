package com.gl.ceir.config.util;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.ceir.config.controller.RegularizedDeviceController;
import com.gl.ceir.config.model.RegularizeDeviceDb;
import com.gl.ceir.config.repository.RegularizedDeviceDbRepository;

@Component
public class CommonFunction {

	private static final Logger logger = LogManager.getLogger(RegularizedDeviceController.class);
	@Autowired
	RegularizedDeviceDbRepository regularizedDeviceDbRepository;


	public Boolean checkAllImeiOfRegularizedDevice(RegularizeDeviceDb regularizeDeviceDb) {
		try {
			if(Objects.nonNull(regularizeDeviceDb.getFirstImei())) {
				if( regularizeDeviceDb.getFirstImei().length() == 14 && Objects.isNull(regularizedDeviceDbRepository.getByImei(regularizeDeviceDb.getFirstImei()))) {
					return Boolean.FALSE;
				}else if( Objects.isNull(regularizedDeviceDbRepository.getByImei(regularizeDeviceDb.getFirstImei().substring(0, 14)))) {
					return Boolean.FALSE;
				}
			}
			if(Objects.nonNull(regularizeDeviceDb.getSecondImei()) ) {
				if( regularizeDeviceDb.getSecondImei().length() == 14 && Objects.isNull(regularizedDeviceDbRepository.getByImei(regularizeDeviceDb.getSecondImei()))) {
					return Boolean.FALSE;
				}else if( Objects.isNull(regularizedDeviceDbRepository.getByImei(regularizeDeviceDb.getSecondImei().substring(0, 14)))) {
					return Boolean.FALSE;
				}
			}
			if(Objects.nonNull(regularizeDeviceDb.getThirdImei())) {
				if( regularizeDeviceDb.getThirdImei().length() == 14 && Objects.isNull(regularizedDeviceDbRepository.getByImei(regularizeDeviceDb.getThirdImei()))) {
					return Boolean.FALSE;
				}else if( Objects.isNull(regularizedDeviceDbRepository.getByImei(regularizeDeviceDb.getThirdImei().substring(0, 14)))) {
					return Boolean.FALSE;
				}
			}
			if(Objects.nonNull(regularizeDeviceDb.getFourthImei()) ) {
				if( regularizeDeviceDb.getFourthImei().length() == 14 && Objects.isNull(regularizedDeviceDbRepository.getByImei(regularizeDeviceDb.getFourthImei()))) {
					return Boolean.FALSE;
				}else if( Objects.isNull(regularizedDeviceDbRepository.getByImei(regularizeDeviceDb.getFourthImei().substring(0, 14)))) {
					return Boolean.FALSE;
				}
			}
			return Boolean.TRUE;
		}catch (Exception e) {
			// TODO: handle exception
			return Boolean.FALSE;
		}
	}

	public Boolean hasDuplicateImeiInRequest(List<RegularizeDeviceDb> regularizeDeviceDbs) {
		logger.info("regularized device list check for duplicate imei"+regularizeDeviceDbs);
	//	HashSet<String> set = new HashSet<>();
		for(RegularizeDeviceDb device : regularizeDeviceDbs) {
			if(device.getFirstImei()!=null && !device.getFirstImei().isEmpty()) {
				long count = 0;
				if( device.getFirstImei().length() == 14 )
					count=regularizedDeviceDbRepository.countByImei(device.getFirstImei());
				else
					count=regularizedDeviceDbRepository.countByImei(device.getFirstImei().substring(0, 14));
				
				if(count>0) {
					return Boolean.TRUE;
				}
//				if(!set.add(device.getFirstImei())) {
//					return Boolean.TRUE;
//				}
			}
			if(device.getSecondImei()!=null && !device.getSecondImei().isEmpty()) {
//				if(!set.add(device.getSecondImei())) {
//					return Boolean.TRUE;
//				}
				long count = 0;
				if( device.getSecondImei().length() == 14 )
					count=regularizedDeviceDbRepository.countByImei(device.getSecondImei());
				else
					count=regularizedDeviceDbRepository.countByImei(device.getSecondImei().substring(0, 14));
				if(count>0) {
					return Boolean.TRUE;
				}
			}
			if(device.getThirdImei()!=null && !device.getThirdImei().isEmpty()) {
				long count = 0;
				if( device.getThirdImei().length() == 14 )
					count=regularizedDeviceDbRepository.countByImei(device.getThirdImei());
				else
					count=regularizedDeviceDbRepository.countByImei(device.getThirdImei().substring(0, 14));
				if(count>0) {
					return Boolean.TRUE;
				}
//				if(!set.add(device.getThirdImei())) {
//					return Boolean.TRUE;
//				}
			}
			if(device.getFourthImei()!=null && !device.getFourthImei().isEmpty()) {
//				if(!set.add(device.getFourthImei())) {
//					return Boolean.TRUE;
//				}
				long count = 0;
				if( device.getFourthImei().length() == 14 )
					count=regularizedDeviceDbRepository.countByImei(device.getFourthImei());
				else
					count=regularizedDeviceDbRepository.countByImei(device.getFourthImei().substring(0, 14));
				if(count>0) {
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	public int getFeatureIdByTxnId(String txnId) {
		if(Objects.isNull(txnId)) {
			return 0;
		}else {
			if(txnId.startsWith("C")) {
				return 3;
			}else if(txnId.startsWith("S")){
				return 4;
			}
			else {
				return 0;
			}
		}
	}
}
