package com.gl.ceir.config.model;

/**
 * @author ravi
 *
 */
public class FileDetails {
	
	private String fileName;
	private String filePath;
	private String url;
	
	public FileDetails( String fileName, String filePath, String url ) {
		this.fileName = fileName;
		this.filePath = filePath;
		this.url = url;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FileDetails [fileName=");
		builder.append(fileName);
		builder.append(", filePath=");
		builder.append(filePath);
		builder.append(", url=");
		builder.append(url);
		builder.append("]");
		return builder.toString();
	}
	
}
