package org.gl.ceir.CeirPannelCode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertyReader {


	@Value("${apiUrl1}")		
   public String apiUrl1;
	
	@Value("${dashBoardfeignClientPath}")
   public String dashBoardfeignClientPath;
   
	@Value("${grievanceFeignClientPath}")
	public String grievanceFeignClientPath; 
   
	@Value("${feignClientPath}")
	public String feignClientPath;
   
	@Value("${gsmaFeignClientPath}")
	public String gsmaFeignClientPath;
   
	@Value("${sessionLogOutTime}")
	public int sessionLogOutTime;
   
	@Value("${serverId}")
	public Integer serverId;
	
	@Value("${downloadFilePath}")
	public String downloadFilePath;
	
	@Value("${propertiesFileLocation}")
	public String propertiesFileLocation;
}
