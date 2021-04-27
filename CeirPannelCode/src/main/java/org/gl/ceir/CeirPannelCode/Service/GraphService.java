package org.gl.ceir.CeirPannelCode.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gl.ceir.CeirPannelCode.Feignclient.GraphFeign;
import org.gl.ceir.CeirPannelCode.Model.LoginGraphFilter;
import org.gl.ceir.CeirPannelCode.Model.UserLoginReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GraphService {
	
	private final Logger log = LoggerFactory.getLogger(getClass());	
	
	@Autowired
	GraphFeign graphFeign;
	
	public ResponseEntity<?> userLoginGraph(LoginGraphFilter filter) {
	log.info("inside login graph service");
	log.info("filter data: "+filter);
	try {
		UserLoginReport pie= new UserLoginReport();
		List<UserLoginReport> data=graphFeign.userLoginGraph(filter);
		List<UserLoginReport> listForPie=new ArrayList<UserLoginReport>();
//pie		
		pie.setNoUserLogged(data
				.stream()
				.mapToInt(x -> x.getNoUserLogged())
				.sum());
		
		pie.setUniqueUserLogged(data
				.stream()
				.mapToInt(y -> y.getUniqueUserLogged())
				.sum());
		String[] labelsName= {"Number Of Users","Unique Users"};
		pie.setLabels(labelsName);
		listForPie.add(pie);
		
		
		
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("line", data);
		map.put("pie", listForPie);
		
		log.info("list data size in response: "+data.size()+"::::::::map::::::::"+map);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	catch(Exception e) {
		log.info("error in get user login graph data");
		log.info(e.toString());
		return new ResponseEntity<>(new ArrayList<UserLoginReport>(), HttpStatus.NO_CONTENT);
			
	}
	}
}
