package com.gl.ceir.config.factory;

import java.time.format.DateTimeFormatter;

import com.gl.ceir.config.model.FileDetails;
import com.gl.ceir.config.model.FilterRequest;
import com.gl.ceir.config.model.SystemConfigurationDb;

public interface ExportFile {
	public FileDetails export(FilterRequest filterRequest, String source, DateTimeFormatter dtf, DateTimeFormatter dtf2,
			String filePath, SystemConfigurationDb link);
}
