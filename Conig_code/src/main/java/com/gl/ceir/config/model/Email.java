package com.gl.ceir.config.model;

public class Email {

	private String body;
	private String subject;
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	
	public Email(String body, String subject) {
		super();
		this.body = body;
		this.subject = subject;
	}
	public Email() {
		super();
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Email [body=");
		builder.append(body);
		builder.append(", subject=");
		builder.append(subject);
		builder.append("]");
		return builder.toString();
	}
	
	
}
