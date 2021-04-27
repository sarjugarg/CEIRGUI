package org.gl.ceir.Class.HeadersTitle;

public class DatatableHeaderModel {
private String title;


public DatatableHeaderModel(String title) {
	super();
	this.title = title;
}

@Override
public String toString() {
	return "DatatableHeaderModel [title=" + title + "]";
}

public String getTitle() {
	return title;
}

public void setTitle(String title) {
	this.title = title;
}
}
